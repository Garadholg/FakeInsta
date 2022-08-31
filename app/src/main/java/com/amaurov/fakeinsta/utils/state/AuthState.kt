package com.amaurov.fakeinsta.utils.state

import android.content.Context
import com.amaurov.fakeinsta.dao.models.Post
import com.amaurov.fakeinsta.utils.GenericCallback

interface AuthState {
    fun likePost(post: Post, context: Context, callback: GenericCallback<Boolean>? = null)
    fun commentPost(post: Post, context: Context)
}