package com.aspark.carebuddynurse.ui.account

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspark.carebuddynurse.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(private val repo: Repository): ViewModel() {

    fun uploadProfilePic(uri: Uri, id: Int,
                         contentResolver: ContentResolver,
                         cacheDir: File) {

        viewModelScope.launch(Dispatchers.IO) {

            val uploadFile: MultipartBody.Part? = convertToFile(uri, id, contentResolver, cacheDir)

            uploadFile?.let {
                repo.uploadProfilePic(it, id)
            }
        }
    }

    private fun convertToFile(
        imageUri: Uri,
        id: Int,
        contentResolver: ContentResolver,
        cacheDir: File
    ): MultipartBody.Part? {

        val inputStream = contentResolver.openInputStream(imageUri)
        val tempFile = File(cacheDir, "profile_nurse_$id.jpg")
        tempFile.outputStream().use {
            inputStream?.copyTo(it)
        }
        inputStream?.close()

        val file = RequestBody.create(MediaType.parse("image/*"), tempFile)

        //the name of requestBody should be same as the parameter name received in backend
        val requestBody = MultipartBody.Part.createFormData("image", tempFile.name, file)

        Log.i("AccountViewModel", "convertToFile:type- ${file.contentType()}")
        Log.i("AccountViewModel", "isFile- ${tempFile.isFile}")

        return requestBody

    }
}