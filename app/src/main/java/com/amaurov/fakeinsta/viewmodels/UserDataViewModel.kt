package com.amaurov.fakeinsta.viewmodels

import com.amaurov.fakeinsta.dao.models.UserData
import com.amaurov.fakeinsta.dao.repositories.implementations.UserDataRepositoryImpl
import com.amaurov.fakeinsta.dao.utils.GenericCallback
import kotlinx.coroutines.runBlocking

class UserDataViewModel(
    private val repository: UserDataRepositoryImpl = UserDataRepositoryImpl()
) {
    fun createUser(userData: UserData, callback: GenericCallback<UserData>) = runBlocking {
        repository.createUserData(userData, callback)
    }
}