package com.cibofdevs.instagramclone

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cibofdevs.instagramclone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), AuthCallback {

    private val vm: MainViewModel by viewModels()
    private val adapter = PostListAdapter(arrayListOf())
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        setupUI()
        setupObservables()
    }

    fun setupUI() {
        binding.postsRV.adapter = adapter
        binding.postsRV.layoutManager = LinearLayoutManager(this)

        binding.loginButton.setOnClickListener {
            val dialog = LoginDialog(this)
            dialog.show(supportFragmentManager, "LoginDialog")
        }
    }

    fun setupObservables() {
        vm.posts.observe(this, Observer { posts ->
            adapter.updatePosts(posts)
        })
    }

    override fun onLogin(username: String, password: String) {
        vm.onLogin(username, password)
    }
}