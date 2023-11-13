package com.aspark.carebuddynurse.ui.auth

import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.aspark.carebuddynurse.databinding.FragmentLoginBinding
import com.aspark.carebuddynurse.model.Nurse.Companion.currentNurse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFrag : Fragment() {

    private lateinit var preferences : SharedPreferences
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        preferences = requireContext().getSharedPreferences(requireContext().packageName,
            AppCompatActivity.MODE_PRIVATE)

        val isSignedIn = preferences.getBoolean("isSignedIn", false)
        val nurseId = preferences.getInt("nurseId", -1)

        Log.i("LoginFrag", "onViewCreated: isSignedIn-$isSignedIn")

        if (isSignedIn && nurseId != -1) {

            Log.i("LoginFrag", "onViewCreated: nurseID-$nurseId")
            viewModel.getNurseById(nurseId)
            val action = LoginFragDirections.actionLoginFragToHomeFrag()
            navController.navigate(action)
        }

        binding.btNurseLogin.setOnClickListener {

            val firebaseToken = preferences.getString("firebase_token", null)

            val email = binding.etEmail.text.toString()
            val password  = binding.etPassword.text.toString()

            Log.d("LoginFrag", "onCreate: token= $firebaseToken")
            viewModel.login(email, password, firebaseToken!!)
        }

        binding.tvSignup.setOnClickListener {
            val action = LoginFragDirections.actionLoginFragToSignUpFrag()
            navController.navigate(action)
        }

        binding.etEmail.doAfterTextChanged { hideErrorLogin() }
        binding.etPassword.doAfterTextChanged { hideErrorLogin() }

        viewModel.loginSuccess.observe(viewLifecycleOwner) {

            Log.d("LoginFrag", "onCreate: callActivity observer called")

            it?.let {
                if (it){
                    setSignedIn()
                    val action = LoginFragDirections.actionLoginFragToHomeFrag()
                    navController.navigate(action)
                }
            }
        }

        viewModel.loginErrorMessage.observe(viewLifecycleOwner){

            it?.let {

                if (it.isNotEmpty())
                    showErrorLogin(it)
            }
        }

        viewModel.showNetworkError.observe(viewLifecycleOwner){

            Log.d("LoginFrag", "onCreate: showNetwork observer called")

            it?.let {

                if (it)
                    showNetworkError()
            }
        }
    }

    private fun showErrorLogin(s: String) {

        binding.cvLoginError.visibility = View.VISIBLE
        binding.tvLoginError.text = s
    }

    private fun hideErrorLogin() {
        binding.cvLoginError.visibility = View.INVISIBLE
    }

    private fun showNetworkError() {
        Toast.makeText(requireContext(), "Something went wrong!",
            Toast.LENGTH_SHORT).show()
    }

    private fun setSignedIn() {

        val editor = preferences.edit()

        Log.i("LoginFrag", "setSignedIn - Signed In nurseId - ${currentNurse.id}")

        editor.putBoolean("isSignedIn", true)
        editor.putInt("nurseId", currentNurse.id)
        editor.apply()
    }

    override fun onDestroy() {

        viewModel.setLoginSuccessFalse()
        super.onDestroy()
    }

}