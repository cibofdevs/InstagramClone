package com.cibofdevs.instagramclone

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cibofdevs.instagramclone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

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
    }

    fun setupUI() {
        binding.postsRV.adapter = adapter
        binding.postsRV.layoutManager = LinearLayoutManager(this)
    }
}