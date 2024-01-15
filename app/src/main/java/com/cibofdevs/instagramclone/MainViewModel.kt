package com.cibofdevs.instagramclone

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cibofdevs.instagramclone.api.InstagramApiService
import com.cibofdevs.instagramclone.api.Post
import com.cibofdevs.instagramclone.api.UserLoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {

    val posts = MutableLiveData<List<Post>>()
    val loggedIn = MutableLiveData<Boolean>()

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
                        val list = response.body()
                        posts.value = list ?: listOf()
                    }
                }

                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    val i = 0
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
                    val i = 0
                }

            })
    }
}