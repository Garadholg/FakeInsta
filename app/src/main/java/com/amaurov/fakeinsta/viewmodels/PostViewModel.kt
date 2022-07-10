package com.amaurov.fakeinsta.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.amaurov.fakeinsta.dao.responses.implementations.PostsRepositoryImpl
import kotlinx.coroutines.Dispatchers

class PostViewModel(
 private val repository: PostsRepositoryImpl = PostsRepositoryImpl()
): ViewModel() {
 val responseLiveData = liveData(Dispatchers.IO) {
  emit(repository.getPosts())
 }
}