package com.aspark.carebuddynurse.ui.auth

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
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.aspark.carebuddynurse.databinding.FragmentLoginBinding
import com.aspark.carebuddynurse.model.Nurse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFrag : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val authViewModel: AuthViewModel by activityViewModels()

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

        val preferences = requireContext().getSharedPreferences(requireContext().packageName,
            AppCompatActivity.MODE_PRIVATE)

        binding.btNurseLogin.setOnClickListener {

            val firebaseToken = preferences.getString("firebase_token", null)

            val email = binding.etEmail.text.toString()
            val password  = binding.etPassword.text.toString()

            Log.d("LoginActivity", "onCreate: token= $firebaseToken")
            authViewModel.login(email, password, firebaseToken!!)
        }

        binding.tvSignup.setOnClickListener {
            val action = LoginFragDirections.actionLoginFragToSignUpFrag()
            navController.navigate(action)
        }

        binding.etEmail.doAfterTextChanged { hideErrorLogin() }
        binding.etPassword.doAfterTextChanged { hideErrorLogin() }

        authViewModel.callActivity.observe(viewLifecycleOwner) {

            Log.d("NurseLoginActivity", "onCreate: callActivity observer called")

            it?.let {
                if (it){
                    setIsNurseSignedIn(true)
                    navController.popBackStack()
                }
            }
        }

        authViewModel.loginErrorMessage.observe(viewLifecycleOwner){

            it?.let {

                if (it.isNotEmpty())
                    showErrorLogin(it)
            }
        }

        authViewModel.showNetworkError.observe(viewLifecycleOwner){

            Log.d("NurseLoginActivity", "onCreate: showNetwork observer called")

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
        Toast.makeText(requireContext(), "Something went wrong! Please try again later",
            Toast.LENGTH_SHORT).show()
    }

    private fun setIsNurseSignedIn(b: Boolean) {

        val preferences = requireContext().getSharedPreferences(requireContext().packageName,
            AppCompatActivity.MODE_PRIVATE)
        val editor = preferences.edit()

        Log.i("nurseHomeActivity", "setNurseSignedIn: isSignedIn $b")

        editor.putBoolean("is_signed_in", b)

        if (b)
            editor.putString("nurseEmail", Nurse.currentNurse.email)

        editor.apply()
    }

}