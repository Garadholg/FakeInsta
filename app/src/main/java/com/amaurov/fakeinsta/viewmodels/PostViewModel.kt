package com.amaurov.fakeinsta.viewmodels

data class PostViewModel(
 val id: Long,
 val username: String,
 val caption: String,
 val hashtags: List<String>,
 var isLiked: Boolean
)