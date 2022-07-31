package com.amaurov.fakeinsta.dao.repositories.interfaces

import com.amaurov.fakeinsta.dao.models.Post
import com.amaurov.fakeinsta.dao.responses.FirebaseResponse

interface PostsRepository {
    suspend fun getPosts(): FirebaseResponse<Post>
    suspend fun updatePostLikes()
}