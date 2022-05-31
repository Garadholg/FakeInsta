package com.amaurov.fakeinsta.viewmodels

data class PostViewModel(
 var id: Long,
 var username: String,
 var caption: String,
 var isLiked: Boolean
)