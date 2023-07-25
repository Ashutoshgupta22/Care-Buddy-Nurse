package com.aspark.carebuddynurse.repository

import android.util.Log
import com.aspark.carebuddynurse.api.Api
import com.aspark.carebuddynurse.api.LoginRequest
import com.aspark.carebuddynurse.model.Nurse
import com.aspark.carebuddynurse.model.Nurse.Companion.currentNurse
import com.aspark.carebuddynurse.retrofit.HttpStatusCode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor( private val api: Api) {

//    private val api = RetrofitService
//        .retrofit
//        .create(Api::class.java)


    fun login(email: String, password: String,
              firebaseToken: String, callback: (HttpStatusCode) -> Unit) {

        val loginRequest = LoginRequest(email,password)
        Log.d("Repository", "login: $loginRequest")

//         val api = RetrofitService
//            .retrofit
//            .create(Api::class.java)

        //TODO catch java.net.ConnectException, when couldn't connect with backend and show toast
        api.login( loginRequest)
            .enqueue(object : Callback<Nurse> {

                override fun onResponse(call: Call<Nurse>, response: Response<Nurse>) {

                    if (response.isSuccessful ) {

                        Log.i("Repository", "Welcome Back!")
                        currentNurse = response.body()!!

                        setNurseFirebaseToken(firebaseToken, email)

                       // mCallActivity.value = true
                        callback(HttpStatusCode.OK)

                    }
                    else if (response.code() == HttpStatusCode.UNAUTHORIZED.code) {

                        Log.e("Repository", "onResponse: Response " +
                                "email not registered ")

                        //mLoginErrorMessage.value = "Email not registered"
                        callback(HttpStatusCode.UNAUTHORIZED)
                    }
                    else if(response.code() == HttpStatusCode.FORBIDDEN.code) {
                        Log.e("Repository", "onResponse: Response " +
                                "unsuccessful invalid email ")

                        // mLoginErrorMessage.value = "Invalid email or password"
                        callback(HttpStatusCode.FORBIDDEN)
                    }
                }

                override fun onFailure(call: Call<Nurse>, t: Throwable) {
                   // showNetworkError.value = true
                    callback(HttpStatusCode.FAILED)
                    Log.e("Repository", "onFailure: Boolean login Failed", t )
                }
            })
    }

    private fun setNurseFirebaseToken(firebaseToken: String, email: String) {

        currentNurse.firebaseToken = firebaseToken
//         val api = RetrofitService
//            .retrofit
//            .create(Api::class.java)

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

}