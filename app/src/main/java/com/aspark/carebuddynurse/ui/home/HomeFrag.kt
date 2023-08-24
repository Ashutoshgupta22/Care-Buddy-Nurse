package com.aspark.carebuddynurse.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aspark.carebuddynurse.databinding.FragmentHomeBinding
import com.aspark.carebuddynurse.model.Nurse
import com.aspark.carebuddynurse.model.Nurse.Companion.currentNurse
import com.bumptech.glide.Glide
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

        if ( currentNurse.profilePic.isNotEmpty() ) {

            setProfilePic(currentNurse.profilePic.toUri())
        }

        binding.ivHomeProfilePic.setOnClickListener {

            val action = HomeFragDirections.actionHomeFragToAccountFrag()
            navController.navigate(action)
        }

        val preferences = requireContext().getSharedPreferences(requireContext().packageName,
            AppCompatActivity.MODE_PRIVATE)
        val isSignedIn = preferences.getBoolean("is_signed_in", false)

        if (! isSignedIn) {

            val action = HomeFragDirections.actionHomeFragToLoginFrag()
            navController.navigate(action)
        }
        else {
            checkPermissions()
            val nurseId = preferences.getInt("nurseId", -1)

            Log.i("HomeFrag", "onViewCreated: nurseId-$nurseId")

            if (nurseId != -1)
                viewModel.getNurseById(nurseId)

            setIsNurseSignedIn(true)

        }


        Log.d("HomeFrag", "onViewCreated: Home Frag called")

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
            editor.putInt("nurseId", currentNurse.id)

        editor.apply()
    }


    private fun setProfilePic(it: Uri) {

        Glide
            .with(requireContext())
            .load(it)
            .centerCrop()
            .into(binding.ivHomeProfilePic)
    }
}