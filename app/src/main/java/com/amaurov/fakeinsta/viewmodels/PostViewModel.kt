package com.amaurov.fakeinsta.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.amaurov.fakeinsta.dao.models.Post
import com.amaurov.fakeinsta.dao.models.UserData
import com.amaurov.fakeinsta.dao.repositories.RepositoryFactory
import com.amaurov.fakeinsta.utils.GenericCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class PostViewModel : ViewModel() {
    private val repository = RepositoryFactory.getRepository<Post>()!!

    //Observer
    val getAllPosts = liveData(Dispatchers.IO) {
        emit(repository.getAll())
    }

    fun getByKeyword(keyword: String) = liveData(Dispatchers.IO) {
        emit(repository.getByQuery(keyword))
    }

    fun getPostById(id: String, callback: GenericCallback<Post>) = runBlocking {
        repository.getById(id, callback)
    }

    fun createPost(post: Post, callback: GenericCallback<Post>) = runBlocking {
        repository.create(post, callback)
    }
}