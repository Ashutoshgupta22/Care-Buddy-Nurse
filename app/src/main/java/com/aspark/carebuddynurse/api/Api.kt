package com.aspark.carebuddynurse.api

import com.aspark.carebuddynurse.model.Nurse
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @POST("/api/nurse/login")
    fun login(@Body loginRequest: LoginRequest) : Call<Nurse>

    @POST("/api/nurse/signup")
    fun signUp(@Body nurse: Nurse): Call<Nurse>

    @POST("/api/nurse/set-firebase-token/{email}")
    fun setNurseFirebaseToken(@Path(value = "email") email: String,
                              @Body firebaseToken: String) : Call<Boolean>

    @GET("/api/nurse/{id}")
    fun getNurseById(@Path("id") id: Int): Call<Nurse>

    @Multipart
    @POST("/api/nurse/upload-profile-pic")
    fun uploadProfilePic(@Part image: MultipartBody.Part,
                         @Query("nurseId") nurseId: Int): Call<String>
}