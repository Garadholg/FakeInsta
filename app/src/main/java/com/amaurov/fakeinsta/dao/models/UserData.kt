package com.amaurov.fakeinsta.dao.models

data class UserData(
    val username: String,
    val email: String,
    var subscriptionType: String
)
