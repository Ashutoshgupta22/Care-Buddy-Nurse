package com.aspark.carebuddynurse.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aspark.carebuddynurse.databinding.FragmentSignupBinding
import com.aspark.carebuddynurse.model.Nurse.Companion.currentNurse
import dagger.hilt.android.AndroidEntryPoint
import java.util.logging.Level
import java.util.logging.Logger

@AndroidEntryPoint
class SignUpFrag: Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private val authViewModel : AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val navController = findNavController()

        authViewModel.startActivity.observe(viewLifecycleOwner){
            it?.let {

                if (it) {

                    val action = SignUpFragDirections.actionSignUpFragToLoginFrag()
                    navController.navigate(action)
                    //navController.popBackStack()
                }
            }
        }

        authViewModel.signUpFailedError.observe(viewLifecycleOwner){

            it?.let { showSignUpFailed(it) }
        }


        binding.btnSignup.setOnClickListener {

            val sFirstName = binding.etSignupFirstName.text.toString().trim()
            val sLastName = binding.etSignupLastName.text.toString().trim()
            val sAge = binding.etSignupAge.text.toString().trim()
            val sBiography = binding.etSignupBiography.text.toString().trim()
            val sQualification = binding.etSignupQualifications.text.toString().trim()
            val sExperience = binding.etSignupExperience.text.toString().trim()
            val sEmail = binding.etSignupEmail.text.toString().trim()
            val sPassword = binding.etSignupPassword.text.toString().trim()
            val sConfirmPassword = binding.etSignupConfirmPassword.text.toString().trim()

            if (verifyInput(sFirstName, sAge,sBiography, sQualification,
                    sExperience, sEmail, sPassword, sConfirmPassword)) {

                val nurse = currentNurse
                nurse.firstName = sFirstName
                nurse.lastName = sLastName
                nurse.age = sAge.toInt()
                nurse.biography = sBiography
                nurse.experience = sExperience.toInt()
                nurse.qualifications = sQualification
                nurse.specialities = arrayListOf("Elder Care", "Post surgery","Elder care")
                nurse.email = sEmail
                nurse.password = sPassword

                authViewModel.signup(nurse)
            }
        }

    }

    private fun showSignUpFailed( s: String) {

        Toast.makeText(requireContext(), s, Toast.LENGTH_LONG).show()
        Logger.getLogger(SignUpFrag::class.java.name).log(
            Level.SEVERE,
            "Error Occurred")
    }

    private fun verifyInput(
        sFirstName: String, sAge: String, sBiography: String, sQualification: String,
        sExperience: String,sEmail: String,
        sPassword: String, sConfirmPassword: String): Boolean {

        var validInput = true

        if (sFirstName == "") {
            binding.etSignupFirstName.error = "First Name can not be empty"
            validInput =  false
        }
        if (sAge == "") {
            binding.etSignupAge.error = "Age can not be empty"
            validInput = false
        }

        if (sBiography == "") {
            binding.etSignupBiography.error = "Biography can not be empty"
            validInput = false
        }
        if (sQualification == "") {
            binding.etSignupQualifications.error = "Qualification can not be empty"
            validInput = false
        }
        if (sExperience == "") {
            binding.etSignupExperience.error = "Experience can not be empty"
            validInput = false
        }

        if (sEmail == "") {
            binding.etSignupEmail.error = "Email can not be empty"
            validInput = false
        }
        if (sPassword == "") {
            binding.etSignupPassword.error = "Password can not be empty"
            validInput = false
        } else {
            if (sPassword.length < 6) {
                binding.etSignupPassword.error = "Password should contain at least 6 characters "
                validInput = false
            } else {
                if (sPassword != sConfirmPassword) {
                    binding.etSignupConfirmPassword.error = "Confirm Password did not match"
                    validInput = false
                }
            }
        }
        return validInput
    }
}