package com.amaurov.fakeinsta.viewmodels

import com.amaurov.fakeinsta.dao.models.UserData
import com.amaurov.fakeinsta.dao.repositories.implementations.UserDataRepositoryImpl
import com.amaurov.fakeinsta.utils.GenericCallback
import kotlinx.coroutines.runBlocking

class UserDataViewModel(
    private val repository: UserDataRepositoryImpl = UserDataRepositoryImpl()
) {
    fun createUser(userData: UserData, callback: GenericCallback<UserData>) = runBlocking {
        repository.createUserData(userData, callback)
    }

    fun getUserById(id: String, callback: GenericCallback<UserData>) = runBlocking {
        repository.getUserDataByKey(id, callback)
    }
}