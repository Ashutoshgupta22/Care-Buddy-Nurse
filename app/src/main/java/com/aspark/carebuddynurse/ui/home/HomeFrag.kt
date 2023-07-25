package com.aspark.carebuddynurse.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.aspark.carebuddynurse.databinding.FragmentHomeBinding
import com.aspark.carebuddynurse.model.Nurse
import com.aspark.carebuddynurse.ui.MainActivity
import com.aspark.carebuddynurse.ui.auth.LoginFragDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFrag: Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel : HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        val preferences = requireContext().getSharedPreferences(requireContext().packageName,
            AppCompatActivity.MODE_PRIVATE)
        val isSignedIn = preferences.getBoolean("is_signed_in", false)

        if (! isSignedIn) {

            val action = HomeFragDirections.actionHomeFragToLoginFrag()
            navController.navigate(action)
        }
        else {
            checkPermissions()
            setIsNurseSignedIn(true)
        }

        Log.d("HomeFrag", "onViewCreated: Home Frag called")

        binding.btnSignOut.setOnClickListener {

            setIsNurseSignedIn(false)
            val action = HomeFragDirections.actionHomeFragToLoginFrag()
            navController.navigate(action)
          //  navController.popBackStack()
        }
    }

    private fun checkPermissions() {

        //check if notification permission granted
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED)
            Log.i("HomeActivity", "onCreate: Checked notification permission granted")

        else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {

            //show a educational ui to inform the user of benefits of accepting this permission
            Toast.makeText(requireContext(), "Accept notification permission to not " +
                    "miss any update about your bookings",
                Toast.LENGTH_LONG).show()
        }

        else {
            Log.e("HomeActivity", "onCreate: Checked notification " +
                    "permission was denied " )
            showNotificationPermissionDialog()
        }

    }

    private fun showNotificationPermissionDialog() {

        val notificationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->

            if (isGranted){

                Log.d("UserHomeActivity", "showNotificationPermissionDialog: " +
                        "notification permission granted ")
            }
            else{
                Log.e("UserHomeActivity", "showNotificationPermissionDialog: " +
                        "notification permission denied")
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // no need to ask notification permission for android version below 13
            notificationPermissionRequest.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

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