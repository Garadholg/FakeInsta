package com.amaurov.fakeinsta.dao.repositories

import com.amaurov.fakeinsta.dao.responses.FirebaseResponse
import com.amaurov.fakeinsta.utils.GenericCallback

interface Repository<T> {
    suspend fun create(newEntity: T, callback: GenericCallback<T>)
    suspend fun getAll() : FirebaseResponse<T>
    suspend fun getByQuery(query: String): FirebaseResponse<T>
    suspend fun getById(id: String, callback: GenericCallback<T>)
    suspend fun update(newValue: T, callback: GenericCallback<Boolean>)
}