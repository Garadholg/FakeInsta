package com.amaurov.fakeinsta.dao.repositories.implementations

import com.amaurov.fakeinsta.dao.models.Post
import com.amaurov.fakeinsta.dao.repositories.interfaces.PostsRepository
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
): PostsRepository {
    override suspend fun createPost(post: Post, callback: GenericCallback<Void>) {
        try {
            postRef.push().setValue(post)
                .addOnCompleteListener {
                    // Maybe I don't even need a callback
                    callback.onCallback(FirebaseResponse())
                }
        } catch (exception: Exception) {
            val msg = exception.message
        }
    }

    override suspend fun getPosts(): FirebaseResponse<Post> {
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

    override suspend fun updatePostLikes(postId: String, userId: String, isLiked:Boolean, callback: GenericCallback<Boolean>) {
        val likeNode = postRef.child(postId).child("likes").child(userId)
        val response = FirebaseResponse<Boolean>()

        if (isLiked) {
            likeNode.removeValue()
                .addOnCompleteListener {
                    response.data = listOf(false)
                    callback.onCallback(response)
                }
        }
        else {
            likeNode.setValue(1)
                .addOnCompleteListener {
                    response.data = listOf(true)
                    callback.onCallback(response)
                }
        }
    }
}