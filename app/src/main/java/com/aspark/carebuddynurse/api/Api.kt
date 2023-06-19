package com.aspark.carebuddynurse.api

import com.aspark.carebuddynurse.model.Nurse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface Api {

    @POST("/api/nurse/login")
    fun loginNurse(@Body loginRequest: LoginRequest) : Call<Nurse>

    @POST("/api/nurse/set-firebase-token/{email}")
    fun setNurseFirebaseToken(@Path(value = "email") email: String,
                              @Body firebaseToken: String) : Call<Boolean>
}