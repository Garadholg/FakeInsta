package com.amaurov.fakeinsta.utils.state

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import com.amaurov.fakeinsta.dao.models.Post
import com.amaurov.fakeinsta.utils.GenericCallback

class AnonymousUserState: AuthState {
    override fun likePost(post: Post, context: Context,  callback: GenericCallback<Boolean>?) {
        val dialog = AlertDialog.Builder(context)
        dialog.apply {
            setPositiveButton("Ok"
            ) { _, _ ->
                Log.i("STATE_TAG", "Cannot like cause not logged in")
            }
        }
        dialog.setTitle("Not logged in")
        dialog.setMessage("You must login first in order to like a post")
        dialog.show()
    }

    override fun commentPost(post: Post, context: Context) {
        val dialog = AlertDialog.Builder(context)
        dialog.apply {
            setPositiveButton("Ok"
            ) { _, _ ->
                Log.i("STATE_TAG", "Cannot like cause not logged in")
            }
        }
        dialog.setTitle("Not logged in")
        dialog.setMessage("You must login first in order to like comment on a post")
        dialog.show()
    }
}