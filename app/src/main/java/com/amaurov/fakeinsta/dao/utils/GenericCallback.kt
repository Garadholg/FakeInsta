package com.amaurov.fakeinsta.dao.utils

import com.amaurov.fakeinsta.dao.responses.FirebaseResponse

interface GenericCallback<T> {
    fun onCallback(value: FirebaseResponse<T>)
}