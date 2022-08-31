package com.amaurov.fakeinsta.utils.state

import android.content.Context
import android.util.Log
import com.amaurov.fakeinsta.dao.models.Post
import com.amaurov.fakeinsta.dao.repositories.RepositoryFactory
import com.amaurov.fakeinsta.utils.Auth
import com.amaurov.fakeinsta.utils.GenericCallback
import kotlinx.coroutines.runBlocking

class RegisteredUserState: AuthState {
    private val repository = RepositoryFactory.getRepository<Post>()!!

    override fun likePost(post: Post, context: Context, callback: GenericCallback<Boolean>?) = runBlocking {
        if (post.likes.contains(Auth.currentUser!!.id)) {
            post.likes.remove(Auth.currentUser!!.id!!)
        } else {
            post.likes[Auth.currentUser!!.id!!] = 1
        }

        repository.update(post, callback!!)
    }

    override fun commentPost(post: Post, context: Context) {
        Log.i("STATE_TAG", "comment set")
    }
}