package com.amaurov.fakeinsta.viewmodels

import androidx.lifecycle.ViewModel
import com.amaurov.fakeinsta.dao.responses.implementations.PostsRepositoryImpl
import com.amaurov.fakeinsta.dao.responses.interfaces.FirebaseCallback

class PostViewModel(
 private val repository: PostsRepositoryImpl = PostsRepositoryImpl()
): ViewModel() {
 fun getResponse(callback: FirebaseCallback) {
  repository.getPosts(callback)
 }
}