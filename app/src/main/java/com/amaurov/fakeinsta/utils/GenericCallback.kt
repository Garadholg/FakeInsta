package com.amaurov.fakeinsta.utils

import com.amaurov.fakeinsta.dao.responses.FirebaseResponse

interface GenericCallback<T> {
    fun onCallback(response: FirebaseResponse<T>)
}