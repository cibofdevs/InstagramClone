package com.cibofdevs.instagramclone

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cibofdevs.instagramclone.api.InstagramApiService
import com.cibofdevs.instagramclone.api.Post
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {

    val posts = MutableLiveData<List<Post>>()

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
}