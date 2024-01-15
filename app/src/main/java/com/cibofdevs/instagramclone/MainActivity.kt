package com.cibofdevs.instagramclone

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
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

        binding.signupButton.setOnClickListener {
            val dialog = SignupDialog(this)
            dialog.show(supportFragmentManager, "SignupDialog")
        }

        binding.logoutButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes") {dialog, which -> vm.onLogout()}
                .setNegativeButton("Cancel") {dialog, which -> dialog.dismiss()}
                .show()
        }
    }

    fun setupObservables() {
        vm.posts.observe(this, Observer { posts ->
            adapter.updatePosts(posts)
        })

        vm.loggedIn.observe(this, Observer { loggedIn ->
            binding.loginLayout.visibility = if (loggedIn) View.GONE else View.VISIBLE
            binding.logoutLayout.visibility = if (loggedIn) View.VISIBLE else View.GONE
            binding.uploadUnavailableMessage.visibility = if (loggedIn) View.GONE else View.VISIBLE
            binding.uploadLayout.visibility = if (loggedIn) View.VISIBLE else View.GONE
        })
    }

    override fun onLogin(username: String, password: String) {
        vm.onLogin(username, password)
    }

    override fun onSignup(username: String, email: String, password: String) {
        vm.onSignup(username, email, password)
    }
}