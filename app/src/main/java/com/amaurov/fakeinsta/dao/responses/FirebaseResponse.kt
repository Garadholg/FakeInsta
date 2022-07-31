package com.amaurov.fakeinsta.dao.responses

import com.amaurov.fakeinsta.dao.models.Post
import java.lang.Exception

data class FirebaseResponse<T>(
    var data: List<T>? = null,
    var exception: Exception? = null
)
