package com.amaurov.fakeinsta.utils.state

import android.content.Context
import com.amaurov.fakeinsta.dao.models.Post
import com.amaurov.fakeinsta.utils.Auth
import com.amaurov.fakeinsta.utils.GenericCallback

class AuthStateContext {
    private var state: AuthState = if (Auth.currentUser != null) {
        RegisteredUserState()
    } else {
        AnonymousUserState()
    }

    fun changeState(state: AuthState) = apply { this.state = state }

    fun likePost(post: Post, context: Context, callback: GenericCallback<Boolean>) {
        state.likePost(post, context, callback)
    }

    fun commentPost(post: Post, context: Context) {
        state.commentPost(post, context)
    }
}