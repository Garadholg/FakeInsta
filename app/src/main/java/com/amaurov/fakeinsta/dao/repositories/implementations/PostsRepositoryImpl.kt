package com.amaurov.fakeinsta.dao.repositories.implementations

import com.amaurov.fakeinsta.dao.models.Post
import com.amaurov.fakeinsta.dao.models.UserData
import com.amaurov.fakeinsta.dao.repositories.Repository
import com.amaurov.fakeinsta.dao.responses.FirebaseResponse
import com.amaurov.fakeinsta.utils.DBEntities.POSTS_REF
import com.amaurov.fakeinsta.utils.GenericCallback
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class PostsRepositoryImpl (
    private val dbRef: DatabaseReference = Firebase.database.reference,
    private val postRef: DatabaseReference = dbRef.child(POSTS_REF)
): Repository<Post> {
    override suspend fun create(newEntity: Post, callback: GenericCallback<Post>) {
        try {
            postRef.push().setValue(newEntity)
                .addOnCompleteListener {
                    // Maybe I don't even need a callback
                    callback.onCallback(FirebaseResponse())
                }
        } catch (exception: Exception) {

        }
    }

    override suspend fun getAll(): FirebaseResponse<Post> {
        val response = FirebaseResponse<Post>()
        try {
            val posts = mutableListOf<Post>()

            postRef.get().await().children.forEach {
                val post = it.getValue(Post::class.java)!!
                post.id = it.key
                posts.add(post)
            }

            response.data = posts.reversed()
        } catch (exception: Exception) {
            response.exception = exception
        }
        return response
    }

    override suspend fun getByQuery(query: String): FirebaseResponse<Post> {
        val response = FirebaseResponse<Post>()
        try {
            val posts = mutableListOf<Post>()

            postRef.get().await().children.forEach {
                val post = it.getValue(Post::class.java)!!
                post.id = it.key

                if (post.userName == query || post.hashtags.containsKey(query))
                    posts.add(post)
            }

            response.data = posts.reversed()
        } catch (exception: Exception) {
            response.exception = exception
        }
        return response
    }

    override suspend fun getById(id: String, callback: GenericCallback<Post>) {
        val response = FirebaseResponse<Post>()
        postRef.child(id).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    var post = it.result.getValue(Post::class.java)!!
                    post.id = id
                    response.data = listOf(post)
                    callback.onCallback(response)
                }
        }
    }

    override suspend fun update(newValue: Post, callback: GenericCallback<Boolean>) {
        val response = FirebaseResponse<Boolean>()
        postRef.child(newValue.id!!).setValue(newValue)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    response.data = listOf(true)
                    callback.onCallback(response)
                }
            }
    }

}