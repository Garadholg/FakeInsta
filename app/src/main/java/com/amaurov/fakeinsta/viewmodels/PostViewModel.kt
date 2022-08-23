package com.amaurov.fakeinsta.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.amaurov.fakeinsta.dao.models.Post
import com.amaurov.fakeinsta.dao.repositories.implementations.PostsRepositoryImpl
import com.amaurov.fakeinsta.dao.repositories.interfaces.PostsRepository
import com.amaurov.fakeinsta.utils.GenericCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class PostViewModel(
    private val repository: PostsRepository = PostsRepositoryImpl()
) : ViewModel() {

    //Observer
    val responseLiveData = liveData(Dispatchers.IO) {
        emit(repository.getPosts())
    }

    fun updateLikes(
        postId: String,
        userId: String,
        isLiked: Boolean,
        callback: GenericCallback<Boolean>
    ) = runBlocking {
        repository.updatePostLikes(postId, userId, isLiked, callback)
    }

    fun createPost(post: Post, callback: GenericCallback<Void>) = runBlocking {
        repository.createPost(post, callback)
    }
}