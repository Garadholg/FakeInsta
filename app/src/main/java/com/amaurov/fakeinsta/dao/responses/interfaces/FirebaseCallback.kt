package com.amaurov.fakeinsta.dao.responses.interfaces

import com.amaurov.fakeinsta.dao.responses.Response

interface FirebaseCallback {
    fun onResponse(response: Response)
}