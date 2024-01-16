package com.cibofdevs.instagramclone.api

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface InstagramApi {
    @GET("post/all")
    fun getAllPosts(): Call<List<Post>>

    @FormUrlEncoded
    @POST("login")
    fun login(@Field("username") username: String,
              @Field("password") password: String
    ): Call<UserLoginResponse>

    @POST("user")
    fun signup(@Body user: UserSignupRequest): Call<UserSignupResponse>

    @Multipart
    @POST("post/image")
    fun uploadImage(@Part image: MultipartBody.Part,
                    @Header("Authorization") auth: String
    ): Call<ImageUploadResponse>
}