package com.amaurov.fakeinsta.dao.models

import com.google.firebase.database.Exclude

data class Post(
    @get:Exclude
    var id: String? = null,
    val userId: String? = null,
    val userName: String? = null,
    val caption: String? = null,
    val hashtags: HashMap<String, Int> = hashMapOf(),
    val likes: HashMap<String, Int> = hashMapOf()
)
