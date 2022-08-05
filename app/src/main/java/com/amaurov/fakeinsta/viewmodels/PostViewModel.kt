package com.amaurov.fakeinsta.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.amaurov.fakeinsta.dao.repositories.implementations.PostsRepositoryImpl
import com.amaurov.fakeinsta.utils.GenericCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class PostViewModel(
 private val repository: PostsRepositoryImpl = PostsRepositoryImpl()
): ViewModel() {
 val responseLiveData = liveData(Dispatchers.IO) {
  emit(repository.getPosts())
 }

 fun updateLikes(postId: String, userId: String, isLiked: Boolean, callback: GenericCallback<Boolean>) = runBlocking {
  repository.updatePostLikes(postId, userId, isLiked, callback)
 }
}