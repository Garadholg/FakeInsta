package com.amaurov.fakeinsta.viewmodels

data class PostViewModel(
 var id: Long,
 var username: String,
 var caption: String,
 var hashtags: List<String>,
 var isLiked: Boolean
)