package com.aspark.carebuddynurse.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.aspark.carebuddynurse.R
import com.aspark.carebuddynurse.databinding.ActivityHomeBinding
import com.aspark.carebuddynurse.model.Nurse
import com.aspark.carebuddynurse.ui.login.LoginActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel : HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkPermissions()

        setIsNurseSignedIn(true)

        binding.btnSignOut.setOnClickListener {

            setIsNurseSignedIn(false)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun checkPermissions() {

        //check if notification permission granted
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED)
            Log.i("HomeActivity", "onCreate: Checked notification permission granted")

        else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {

            //show a educational ui to inform the user of benefits of accepting this permission
            Toast.makeText(this, "Accept notification permission to not " +
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

        val preferences = getSharedPreferences(packageName, MODE_PRIVATE)
        val editor = preferences.edit()

        Log.i("nurseHomeActivity", "setNurseSignedIn: isSignedIn $b")

        editor.putBoolean("is_signed_in", b)

        if (b)
            editor.putString("nurseEmail", Nurse.currentNurse.email)

        editor.apply()
    }
}

