package com.aspark.carebuddynurse.ui.auth

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aspark.carebuddynurse.databinding.FragmentSignupBinding
import com.aspark.carebuddynurse.model.Nurse
import com.aspark.carebuddynurse.model.Nurse.Companion.currentNurse
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.Arrays
import java.util.logging.Level
import java.util.logging.Logger

@AndroidEntryPoint
class SignUpFrag: Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private val authViewModel : AuthViewModel by activityViewModels()
    private var selectedSpecialitiesList = arrayListOf<String>()

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

        val specialitiesList = arrayOf("Covid Care","Critical Care","Diabetic Care",
            "Elder Care","General Care","Mother & Baby Care",
            "Post-surgical Care", "Wound Care")

        var checkedCount = 0
        var isCheckedList = booleanArrayOf(false, false, false, false, false, false,
            false, false)


        binding.tvSpecialities.setOnClickListener {

             lateinit var alertDialog: AlertDialog

//            val checkboxes = arrayOfNulls<CheckBox>(8)
//
//            val builder = AlertDialog.Builder(requireContext())
//            builder.setTitle("Select Options")
//
//            for (i in 0 until checkboxes.size) {
//                val checkbox = CheckBox(requireContext())
//                checkbox.id = i
//                checkboxes[i] = checkbox
//                builder.setPositiveButton(
//                    "OK",
//                    DialogInterface.OnClickListener { dialog, id ->
//                        // Disable the rest of the checkboxes if 3 of them are checked
//                        if (checkboxes.count { it?.isChecked ?: false } == 3) {
//                            for (checkbox in checkboxes) {
//                                if (checkbox?.id ?: -1 > 2) {
//                                    checkbox?.isEnabled = false
//                                }
//                            }
//                        }
//                    }
//                )
//            }
//
//            builder.setMultiChoiceItems(
//                arrayOf("Option 1", "Option 2", "Option 3","option 4", "option 5"),
//                null,
//                DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
//                    // Update the state of the checkbox
//                    checkboxes[which]?.isChecked = isChecked
//                }
//            )
//
//            builder.show()

            val dialog = AlertDialog.Builder(requireContext())
            dialog.setTitle("Specialities")

            dialog.setMultiChoiceItems(specialitiesList, isCheckedList) { _, position, isChecked ->
                if (isChecked && checkedCount >= 3) {
                    // If the user tries to check more checkboxes than the limit, uncheck the checkbox
                    alertDialog.listView.setItemChecked(position, false)
                    Toast.makeText(requireContext(), "more than 3", Toast.LENGTH_SHORT).show()


                } else {
                    isCheckedList[position] = isChecked
                    if (isChecked) {
                        checkedCount++
                    } else {
                        checkedCount--
                    }
                }
            }



//            dialog.setMultiChoiceItems(specialitiesList, isCheckedList) {
//                    _, which, isChecked ->
//
//                when(isChecked) {
//
//                    true -> {
//                        checkedCount++
//                        selectedSpecialitiesList.add(specialitiesList[which])
//                        isCheckedList[which] = true
//
//                        if (checkedCount <=3 ){
//
//                            dialog.setPositiveButton("Done"){
//                                    dialog, which ->
//
//                                checkedCount = 0
//
//                                isCheckedList = booleanArrayOf(false, false, false, false, false, false,
//                                    false, false)
//
////                Log.i("SignupFrag", "Done - ${selectedSpecialitiesList[0]} " +
////                        "${selectedSpecialitiesList[1]} ${selectedSpecialitiesList[2]}")
//
//                                // make sure that only 3 chosen values are checked and others are unchecked
//                                for(i in specialitiesList.withIndex()) {
//
//                                    if (i.value == selectedSpecialitiesList[0] ||
//                                        i.value == selectedSpecialitiesList[1] ||
//                                        i.value == selectedSpecialitiesList[2]
//                                    ) {
//                                        checkedCount++
//                                        isCheckedList[i.index] = true
//                                    }
//                                }
//
//
//                            }
//
//                        }
//
//                          if(checkedCount > 3) {
//
//                            Log.i("SignUpFrag", "onViewCreated: 3 box checked")
//                            Snackbar.make(binding.tvSpecialities,
//                                "Max 3 specialities only",Snackbar.LENGTH_SHORT).show()
//                        }
//                    }
//
//                    false -> {
//                        checkedCount --
//
//                            isCheckedList[which] = false
//                            selectedSpecialitiesList.remove(specialitiesList[which])
//                    }
//                }
//
//            }

            dialog.setNegativeButton("Cancel") {
                dialog,which ->

                checkedCount = 0
                 isCheckedList = booleanArrayOf(false, false, false, false, false, false,
                    false, false)
                selectedSpecialitiesList = arrayListOf()
            }

            alertDialog = dialog.create()
            alertDialog.show()

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
                nurse.specialities = arrayListOf(selectedSpecialitiesList[0],
                    selectedSpecialitiesList[1], selectedSpecialitiesList[2])
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