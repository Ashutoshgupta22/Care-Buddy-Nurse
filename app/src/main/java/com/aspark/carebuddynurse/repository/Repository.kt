package com.aspark.carebuddynurse.repository

import android.util.Log
import com.aspark.carebuddynurse.api.Api
import com.aspark.carebuddynurse.api.LoginRequest
import com.aspark.carebuddynurse.model.Nurse
import com.aspark.carebuddynurse.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository {

    private val api = RetrofitService
        .retrofit
        .create(Api::class.java)


    fun login(email: String, password: String, firebaseToken: String) {

        val loginRequest = LoginRequest(email,password)
        Log.d("Repository", "login: $loginRequest")

        //TODO catch java.net.ConnectException, when couldn't connect with backend and show toast
        api.loginNurse(loginRequest)
            .enqueue(object : Callback<Nurse> {

                override fun onResponse(call: Call<Nurse>, response: Response<Nurse>) {

                    if (response.isSuccessful ) {

                        Log.i("Repository", "Welcome Back!")
                        Nurse.currentNurse = response.body()!!

                        setNurseFirebaseToken(firebaseToken, email)

                        mCallActivity.value = true

                    }
                    else if(response.code() == 403){
                        Log.e("Repository", "onResponse: Response " +
                                "unsuccessful invalid email ")

                        mLoginErrorMessage.value = "Invalid email or password"
                    }
                    else if (response.code() == 401){

                        Log.e("Repository", "onResponse: Response " +
                                "email not registered ")

                        mLoginErrorMessage.value = "Email not registered"

                    }
                }

                override fun onFailure(call: Call<Nurse>, t: Throwable) {
                    showNetworkError.value = true
                    Log.e("Repository", "onFailure: Nurse login Failed", t.cause )
                }
            })
    }

    private fun setNurseFirebaseToken(firebaseToken: String, email: String) {

        Nurse.currentNurse.firebaseToken = firebaseToken

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

}