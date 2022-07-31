package com.amaurov.fakeinsta.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.amaurov.fakeinsta.dao.repositories.implementations.PostsRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PostViewModel(
 private val repository: PostsRepositoryImpl = PostsRepositoryImpl()
): ViewModel() {
 val responseLiveData = liveData(Dispatchers.IO) {
  emit(repository.getPosts())
 }

 fun updateLikes() = runBlocking {
  repository.updatePostLikes()
 }
}