package com.aspark.carebuddynurse.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.aspark.carebuddynurse.databinding.ActivityLoginBinding
import com.aspark.carebuddynurse.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel : LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = getSharedPreferences(packageName, MODE_PRIVATE)
        val isSignedIn = preferences.getBoolean("is_signed_in", false)

        if (isSignedIn) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btNurseLogin.setOnClickListener {

            val firebaseToken = preferences.getString("firebase_token", null)

            val email = binding.etEmail.text.toString()
            val password  = binding.etPassword.text.toString()

            Log.d("LoginActivity", "onCreate: token= $firebaseToken")
            viewModel.loginClickListener(email, password, firebaseToken!!)
        }


        binding.etEmail.doAfterTextChanged { hideErrorLogin() }
        binding.etPassword.doAfterTextChanged { hideErrorLogin() }


        viewModel.callActivity.observe(this) {

            Log.d("NurseLoginActivity", "onCreate: callActivity observer called")

            it?.let {
                if (it){
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        viewModel.loginErrorMessage.observe(this){

            it?.let {

                if (it.isNotEmpty())
                    showErrorLogin(it)
            }
        }

        viewModel.showNetworkError.observe(this){

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
        Toast.makeText(this, "Something went wrong! Please try again later", Toast.LENGTH_SHORT).show()
    }

}