package com.aspark.carebuddynurse.repository

import android.util.Log
import com.aspark.carebuddynurse.api.Api
import com.aspark.carebuddynurse.api.LoginRequest
import com.aspark.carebuddynurse.model.Nurse
import com.aspark.carebuddynurse.model.Nurse.Companion.currentNurse
import com.aspark.carebuddynurse.retrofit.HttpStatusCode
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor( private val api: Api) {


    fun login(nurseEmail: String, password: String,
              firebaseToken: String, callback: (HttpStatusCode) -> Unit) {

        val loginRequest = LoginRequest(nurseEmail,password)
        Log.d("Repository", "login: $loginRequest")

        //TODO catch java.net.ConnectException, when couldn't connect with backend and show toast
        api.login( loginRequest)
            .enqueue(object : Callback<Nurse> {

                override fun onResponse(call: Call<Nurse>, response: Response<Nurse>) {

                    if (response.isSuccessful ) {

                        Log.i("Repository", "Welcome Back!")
                        currentNurse = response.body()!!
//                        val(id, firstName, lastName, email) = response.body()!!
//                        currentNurse.id = id
//                        currentNurse.firstName = firstName
//                        currentNurse.lastName = lastName
//                        currentNurse.email = email

                        Log.i("Repository", "onResponse: nurseid-${currentNurse.id}")

                        setNurseFirebaseToken(firebaseToken, nurseEmail)

                        callback(HttpStatusCode.OK)

                    }
                    else if (response.code() == HttpStatusCode.UNAUTHORIZED.code) {

                        Log.e("Repository", "onResponse: Response " +
                                "email not registered ")

                        callback(HttpStatusCode.UNAUTHORIZED)
                    }
                    else if(response.code() == HttpStatusCode.FORBIDDEN.code) {
                        Log.e("Repository", "onResponse: Response " +
                                "unsuccessful invalid email ")

                        callback(HttpStatusCode.FORBIDDEN)
                    }
                }

                override fun onFailure(call: Call<Nurse>, t: Throwable) {
                    callback(HttpStatusCode.FAILED)
                    Log.e("Repository", "onFailure: Boolean login Failed", t )
                }
            })
    }

    private fun setNurseFirebaseToken(firebaseToken: String, email: String) {

        currentNurse.firebaseToken = firebaseToken

        api
            .setNurseFirebaseToken(email, firebaseToken)
            .enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {

                    if (response.isSuccessful && response.body() == true) {
                        Log.d("Repository", "onResponse: setToken success")
                    }
                    else Log.e("Repository", "onResponse: " +
                            "set firebaseToken response unsuccessful" )

                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {

                    Log.e("Repository", "onFailure: " +
                            "set firebase token FAILED" )
                }
            })
    }

    fun signUp(nurse: Nurse, callback: (HttpStatusCode) -> Unit) {

        api
            .signUp(nurse)
            .enqueue(object : Callback<Nurse> {

                override fun onResponse(call: Call<Nurse>, response: Response<Nurse>) {

                    if (response.isSuccessful && response.body() != null) {

                        callback(HttpStatusCode.OK)
                    }
                    else {
                        Log.e("Repository", "onResponse: Signup Response unsuccessful")
                        callback(HttpStatusCode.FAILED)
                    }
                }

                override fun onFailure(call: Call<Nurse>, t: Throwable) {

                    Log.e("Repository", "onFailure: Signup Failed", t)
                    callback(HttpStatusCode.FAILED)
                }
            })
    }

    fun uploadProfilePic(body: MultipartBody.Part, id: Int) {

        Log.i("Repository", "uploadProfilePic:nurseId-$id")

        api
            .uploadProfilePic(body, id)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>,
                                        response: Response<String>) {


                    if (response.isSuccessful && response.body() != null)
                        Log.i("Repository", "onResponse: Profile pic link- "
                        + response.body().toString())

                    else Log.e("Repository", "onResponse: Profile pic" +
                            " upload UNSUCCESSFUL- ${response.code()}" )
                }

                override fun onFailure(call: Call<String>, t: Throwable) {

                    Log.e("Repository", "onFailure: Profile pic upload FAILED ", t )

                }
            })
    }

    fun getNurseById(nurseId: Int, callback: (HttpStatusCode) -> Unit) {

        api
            .getNurseById(nurseId)
            .enqueue(object : Callback<Nurse> {
                override fun onResponse(call: Call<Nurse>, response: Response<Nurse>) {

                    if (response.isSuccessful && response.body()!= null)
                        currentNurse = response.body()!!

                    else {
                        callback(HttpStatusCode.FAILED)
                        Log.e("Repository", "onResponse: " +
                                "getNurseById UNSUCCESSFUL ${response.code()}" )
                    }


                }

                override fun onFailure(call: Call<Nurse>, t: Throwable) {

                    callback(HttpStatusCode.FAILED)
                    Log.e("Repository", "onFailure: " +
                            "getNurseById FAILED", t )

                }

            })
    }
}