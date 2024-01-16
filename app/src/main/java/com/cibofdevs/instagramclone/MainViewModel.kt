package com.cibofdevs.instagramclone

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cibofdevs.instagramclone.api.ImageUploadResponse
import com.cibofdevs.instagramclone.api.InstagramApiService
import com.cibofdevs.instagramclone.api.Post
import com.cibofdevs.instagramclone.api.UserLoginResponse
import com.cibofdevs.instagramclone.api.UserSignupRequest
import com.cibofdevs.instagramclone.api.UserSignupResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainViewModel: ViewModel() {

    val posts = MutableLiveData<List<Post>>()
    val loggedIn = MutableLiveData<Boolean>()
    val message = MutableLiveData<String>()

    private var accessToken: String = ""
    private var currentUsername: String? = null
    private var currentUserId: Int? = null

    init {
        getAllPosts()
    }

    fun getAllPosts() {
        InstagramApiService.api
            .getAllPosts()
            .enqueue(object : Callback<List<Post>> {
                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                    if (response.isSuccessful) {
                        val list = response.body()?.sortedByDescending { post ->
                            val split = post.timestamp.split(".")
                            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                            val date = LocalDateTime.parse(split[0], formatter)
                            date
                        }
                        posts.value = list ?: listOf()
                    } else {
                        message.value = response.message()
                    }
                }

                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    handleError(t)
                }

            })
    }

    fun onLogin(username: String, password: String) {
        InstagramApiService.api
            .login(username, password)
            .enqueue(object : Callback<UserLoginResponse> {
                override fun onResponse(
                    call: Call<UserLoginResponse>,
                    response: Response<UserLoginResponse>
                ) {
                    if (response.isSuccessful) {
                        accessToken = "${response.body()?.tokenType} ${response.body()?.accessToken}"
                        currentUsername = response.body()?.username
                        currentUserId = response.body()?.userId
                        loggedIn.value = true
                    }
                }

                override fun onFailure(call: Call<UserLoginResponse>, t: Throwable) {
                    handleError(t)
                }

            })
    }

    fun onSignup(username: String, email: String, password: String) {
        val signupRequest = UserSignupRequest(username, email, password)
        InstagramApiService.api
            .signup(signupRequest)
            .enqueue(object : Callback<UserSignupResponse> {
                override fun onResponse(
                    call: Call<UserSignupResponse>,
                    response: Response<UserSignupResponse>
                ) {
                    if (response.isSuccessful) {
                        message.value = "User $username created. Logging in..."
                        onLogin(username, password)
                    } else {
                        message.value = response.message()
                    }
                }

                override fun onFailure(call: Call<UserSignupResponse>, t: Throwable) {
                    handleError(t)
                }

            })
    }

    fun onPostUpload(inputStream: InputStream, caption: String) {
        val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), inputStream.readBytes())
        val part = MultipartBody.Part.createFormData("image", "name.jpg", requestBody)

        InstagramApiService.api
            .uploadImage(part, accessToken)
            .enqueue(object : Callback<ImageUploadResponse> {
                override fun onResponse(
                    call: Call<ImageUploadResponse>,
                    response: Response<ImageUploadResponse>
                ) {
                    val imageUrl = response.body()?.filename
                    if (response.isSuccessful && !imageUrl.isNullOrEmpty()) {
                        // call upload method
                        val i = 0
                    } else {
                        message.value = "Something went wrong"
                    }
                }

                override fun onFailure(call: Call<ImageUploadResponse>, t: Throwable) {
                    handleError(t)
                }

            })
    }

    private fun handleError(t: Throwable) {
        message.value = t.localizedMessage
        t.printStackTrace()
    }

    fun onLogout() {
        message.value = "Logged out"
        accessToken = ""
        currentUsername = null
        currentUserId = null
        loggedIn.value = false
    }
}