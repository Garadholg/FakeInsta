package com.amaurov.fakeinsta.dao.repositories.implementations

import com.amaurov.fakeinsta.dao.models.Post
import com.amaurov.fakeinsta.dao.repositories.interfaces.PostsRepository
import com.amaurov.fakeinsta.dao.responses.FirebaseResponse
import com.amaurov.fakeinsta.dao.utils.DBEntities.POSTS_REF
import com.amaurov.fakeinsta.dao.utils.GenericCallback
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class PostsRepositoryImpl (
    private val dbRef: DatabaseReference = Firebase.database.reference,
    private val postRef: DatabaseReference = dbRef.child(POSTS_REF)
): PostsRepository {
    override suspend fun getPosts(): FirebaseResponse<Post> {
        val response = FirebaseResponse<Post>()
        try {
            val posts = mutableListOf<Post>()

            postRef.get().await().children.forEach {
                val post = it.getValue(Post::class.java)!!
                post.id = it.key
                posts.add(post)
            }

            response.data = posts
        } catch (exception: Exception) {
            response.exception = exception
        }
        return response
    }

    override suspend fun updatePostLikes(callback: GenericCallback<Boolean>) {

        //TODO("Insert real values after testing")
        val testHM = hashMapOf<String, Int>()
        testHM["asdkjlasd"] = 1

        postRef.child("2").child("likes").setValue(testHM)
            .addOnCompleteListener {
                val response = FirebaseResponse<Boolean>()
                response.data = listOf(true)
                callback.onCallback(response)
            }
    }
}