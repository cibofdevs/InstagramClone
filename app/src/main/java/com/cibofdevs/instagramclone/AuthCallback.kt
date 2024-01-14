package com.cibofdevs.instagramclone

interface AuthCallback {
    fun onLogin(username: String, password: String)
}