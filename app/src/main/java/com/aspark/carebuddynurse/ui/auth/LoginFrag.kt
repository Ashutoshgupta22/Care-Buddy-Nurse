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
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.aspark.carebuddynurse.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFrag : Fragment() {

    private val viewModel: AuthViewModel by viewModels()
    private lateinit var binding: FragmentLoginBinding

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

        val navController = view.findNavController()

        val preferences = requireContext().getSharedPreferences(requireContext().packageName,
            AppCompatActivity.MODE_PRIVATE)
        val isSignedIn = preferences.getBoolean("is_signed_in", false)

        if (isSignedIn) {

            val action = LoginFragDirections.actionLoginFragToHomeFrag()
            navController.navigate(action)
            //finish()
        }

        binding.btNurseLogin.setOnClickListener {

            val firebaseToken = preferences.getString("firebase_token", null)

            val email = binding.etEmail.text.toString()
            val password  = binding.etPassword.text.toString()

            Log.d("LoginActivity", "onCreate: token= $firebaseToken")
            viewModel.login(email, password, firebaseToken!!)
        }

        binding.etEmail.doAfterTextChanged { hideErrorLogin() }
        binding.etPassword.doAfterTextChanged { hideErrorLogin() }

        viewModel.callActivity.observe(viewLifecycleOwner) {

            Log.d("NurseLoginActivity", "onCreate: callActivity observer called")

            it?.let {
                if (it){
                    val action = LoginFragDirections.actionLoginFragToHomeFrag()
                    navController.navigate(action)
                   // finish()
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
}