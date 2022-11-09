package com.amaurov.fakeinsta.dao.models

import com.amaurov.fakeinsta.utils.annotations.FirebaseEntity
import com.google.firebase.database.Exclude

@FirebaseEntity
data class UserData(
    @get:Exclude
    var id: String? = null,
    var username: String? = null,
    var email: String? = null,
    var profilePicture: String? = null,
    var subscriptionType: String? = null
)
