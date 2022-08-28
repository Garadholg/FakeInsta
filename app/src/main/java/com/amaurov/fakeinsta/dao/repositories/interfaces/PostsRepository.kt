package com.amaurov.fakeinsta.dao.repositories.interfaces

import com.amaurov.fakeinsta.dao.models.Post
import com.amaurov.fakeinsta.dao.repositories.Repository
import com.amaurov.fakeinsta.dao.responses.FirebaseResponse
import com.amaurov.fakeinsta.utils.GenericCallback

//interface PostsRepository : Repository {
//    suspend fun createPost(post: Post, callback: GenericCallback<Void>)
//    suspend fun getPosts(): FirebaseResponse<Post>
//    suspend fun updatePostLikes(postId: String, userId: String, isLiked: Boolean, callback: GenericCallback<Boolean>)
//}