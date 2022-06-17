package com.amaurov.fakeinsta.dao.responses.implementations

import com.amaurov.fakeinsta.dao.models.Post
import com.amaurov.fakeinsta.dao.responses.Response
import com.amaurov.fakeinsta.dao.responses.interfaces.FirebaseCallback
import com.amaurov.fakeinsta.dao.utils.DBEntities.POSTS_REF
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PostsRepositoryImpl(
    private val dbRef: DatabaseReference = Firebase.database.reference,
    private val postRef: DatabaseReference = dbRef.child(POSTS_REF)
) {
    fun getPosts(callback: FirebaseCallback) {
        postRef.get()
            .addOnCompleteListener { task ->
                val response = Response()
                if (task.isSuccessful) {
                    val result = task.result
                    result?.let {
                        response.posts = result.children.map { snapshot ->
                            snapshot.getValue(Post::class.java)!!
                        }
                    }
                } else {
                    response.exception = task.exception
                }
                callback.onResponse(response)
            }
    }
}