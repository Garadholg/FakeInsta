package com.amaurov.fakeinsta.dao.responses.implementations

import com.amaurov.fakeinsta.dao.models.Post
import com.amaurov.fakeinsta.dao.responses.Response
import com.amaurov.fakeinsta.dao.utils.DBEntities.POSTS_REF
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class PostsRepositoryImpl(
    private val dbRef: DatabaseReference = Firebase.database.reference,
    private val postRef: DatabaseReference = dbRef.child(POSTS_REF)
) {
    suspend fun getPosts(): Response {
        val response = Response()
        try {
            response.posts = postRef.get().await().children.map { snapShot ->
                snapShot.getValue(Post::class.java)!!
            }
        } catch (exception: Exception) {
            response.exception = exception
        }
        return response
    }
}