package com.aspark.carebuddynurse.ui.account

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aspark.carebuddynurse.R
import com.aspark.carebuddynurse.databinding.FragmentAccountBinding
import com.aspark.carebuddynurse.model.Nurse.Companion.currentNurse
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFrag: Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private val viewModel: AccountViewModel by viewModels()
    private lateinit var pickPhoto: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onAttach(context: Context) {
        super.onAttach(context)

        //in fragments, activity result is registered before fragment is created.

        pickPhoto = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()) {

            if (it != null) {

                Log.i("AccountFrag", "onViewCreated: Image selected uri: $it")

                setProfilePic(it)
                currentNurse.profilePicUrl = it.toString()

                val contentResolver = requireContext().contentResolver
                val cacheDir = requireContext().cacheDir
                viewModel.uploadProfilePic(it, currentNurse.id, contentResolver, cacheDir)
            }
            else Log.d("AccountFrag", "onViewCreated: No image selected")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

//        activity?.actionBar?.setDisplayShowTitleEnabled(true)
//        activity?.actionBar?.title = "Account"

        if (currentNurse.email.isNotEmpty()){
            binding.tvAccountName.text = "${currentNurse.firstName} ${currentNurse.lastName}"
            binding.tvAccountEmail.text = currentNurse.email
        }

        if ( currentNurse.profilePicUrl.isNotEmpty() ) {
            setProfilePic(currentNurse.profilePicUrl.toUri())
        }

        binding.cvProfilePic.setOnClickListener {

            pickPhoto.launch(PickVisualMediaRequest(
                ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btnSignOut.setOnClickListener {

            setSignedOut()
            val action = AccountFragDirections.actionAccountFragToLoginFrag()
            navController.navigate(action)
           // navController.popBackStack(R.id.loginFrag, false)
          //  navController.popBackStack(R.id.loginFrag, false)
           // val action = AccountFragDirections.actionAccountFragToLoginFrag()
            //navController.navigate(action)
            //  navController.popBackStack()
        }
    }

    private fun setProfilePic(it: Uri) {

        Glide
            .with(requireContext())
            .load(it)
            .centerCrop()
            .into(binding.ivProfilePic)
    }

    private fun setSignedOut() {

        val preferences = requireContext().getSharedPreferences(requireContext().packageName,
            AppCompatActivity.MODE_PRIVATE)
        val editor = preferences.edit()

        Log.i("AccountFrag", "setSignedOut - Signed Out")

        editor.putBoolean("isSignedIn", false)
        editor.putInt("nurseId", -1)
        editor.apply()
    }
}