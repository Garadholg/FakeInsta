package com.amaurov.fakeinsta.dao.responses

import com.amaurov.fakeinsta.dao.models.Post
import java.lang.Exception

data class Response(
    var posts: List<Post>? = null,
    var exception: Exception? = null
)
