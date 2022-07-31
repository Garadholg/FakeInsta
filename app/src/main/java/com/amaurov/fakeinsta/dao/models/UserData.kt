package com.amaurov.fakeinsta.dao.models

import com.google.firebase.database.Exclude

data class UserData(
    @get:Exclude
    var id: String,
    val username: String,
    val email: String,
    var subscriptionType: String
)
