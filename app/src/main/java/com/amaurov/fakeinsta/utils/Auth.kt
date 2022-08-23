package com.amaurov.fakeinsta.utils

import com.amaurov.fakeinsta.dao.models.UserData

// Singleton
object Auth {
    var currentUser: UserData? = null
}