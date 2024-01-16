package com.cibofdevs.instagramclone

interface PostCallback {
    fun onDeletePost(postId: Int)
    fun onComment(text: String, postId: Int)
}