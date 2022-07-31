package com.amaurov.fakeinsta.dao.repositories.interfaces

import com.amaurov.fakeinsta.dao.models.Post
import com.amaurov.fakeinsta.dao.responses.FirebaseResponse
import com.amaurov.fakeinsta.dao.utils.GenericCallback

interface PostsRepository {
    suspend fun getPosts(): FirebaseResponse<Post>
    suspend fun updatePostLikes(callback: GenericCallback<Boolean>)
}