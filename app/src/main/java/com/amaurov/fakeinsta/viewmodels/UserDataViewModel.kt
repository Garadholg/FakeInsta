package com.amaurov.fakeinsta.viewmodels

import com.amaurov.fakeinsta.dao.models.UserData
import com.amaurov.fakeinsta.dao.repositories.RepositoryFactory
import com.amaurov.fakeinsta.utils.GenericCallback
import kotlinx.coroutines.runBlocking

class UserDataViewModel {
    private var repository = RepositoryFactory.getRepository<UserData>()!!

    fun createUser(userData: UserData, callback: GenericCallback<UserData>) = runBlocking {
        repository.create(userData, callback)
    }

    fun getUserById(id: String, callback: GenericCallback<UserData>) = runBlocking {
        repository.getById(id, callback)
    }

    fun updateUser(userData: UserData, callback: GenericCallback<Boolean>) = runBlocking {
        repository.update(userData, callback)
    }
}