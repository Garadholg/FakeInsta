package com.amaurov.fakeinsta.dao.repositories.interfaces

import com.amaurov.fakeinsta.dao.models.UserData
import com.amaurov.fakeinsta.dao.responses.FirebaseResponse
import com.amaurov.fakeinsta.dao.utils.GenericCallback

interface UserDataRepository {
    suspend fun createUserData(userData: UserData, callback: GenericCallback<UserData>)
    suspend fun getUserDataByKey()
}