package com.aspark.carebuddynurse.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.aspark.carebuddynurse.R
import com.aspark.carebuddynurse.databinding.ActivityHomeBinding
import com.aspark.carebuddynurse.model.Nurse

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel : HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.signInNurse()

        binding.btnSignOut.setOnClickListener {

            viewModel.signOutNurse()

            finish()
        }

        viewModel.isNurseSignedIn.observe(this) {

            it?.let {
                setIsNurseSignedIn(it)
            }
        }

    }

    private fun setIsNurseSignedIn(b: Boolean) {

        val preferences = getSharedPreferences(packageName, MODE_PRIVATE)
        val editor = preferences.edit()

        Log.i("nurseHomeActivity", "setNurseSignedIn: isSignedIn $b")

        editor.putBoolean("isNurseSignedIn", b)

        if (b)
            editor.putString("nurseEmail", Nurse.currentNurse.email)

        editor.apply()
    }
}

