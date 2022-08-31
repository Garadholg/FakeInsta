package com.amaurov.fakeinsta.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.amaurov.fakeinsta.dao.models.Post
import com.amaurov.fakeinsta.dao.repositories.RepositoryFactory
import com.amaurov.fakeinsta.utils.GenericCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class PostViewModel : ViewModel() {
    private val repository = RepositoryFactory.getRepository<Post>()!!

    //Observer
    val responseLiveData = liveData(Dispatchers.IO) {
        repository.getAll()
        emit(repository.getAll())
    }

    fun updateLikes(
        postId: String,
        userId: String,
        isLiked: Boolean,
        callback: GenericCallback<Boolean>
    ) = runBlocking {
        //ToDo("Fix this thing")
        repository.update(Post.Builder().build(), callback)
    }

    fun createPost(post: Post, callback: GenericCallback<Post>) = runBlocking {
        repository.create(post, callback)
    }
}