package com.amaurov.fakeinsta.dao.repositories.implementations

import com.amaurov.fakeinsta.dao.models.UserData
import com.amaurov.fakeinsta.dao.repositories.interfaces.UserDataRepository
import com.amaurov.fakeinsta.dao.responses.FirebaseResponse
import com.amaurov.fakeinsta.utils.DBEntities
import com.amaurov.fakeinsta.utils.GenericCallback
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserDataRepositoryImpl (
    private val dbRef: DatabaseReference = Firebase.database.reference,
    private val userDataRef: DatabaseReference = dbRef.child(DBEntities.USERDATA_REF)
): UserDataRepository {
    override suspend fun createUserData(userData: UserData, callback: GenericCallback<UserData>) {
        val response = FirebaseResponse<UserData>()

        userDataRef.child(userData.id!!).setValue(userData)
            .addOnCompleteListener {
                response.data = listOf(userData)
                callback.onCallback(response)
            }
    }

    override suspend fun getUserDataByKey(key: String, callback: GenericCallback<UserData>) {
        val response = FirebaseResponse<UserData>()

        userDataRef.child(key).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    var user = it.result.getValue(UserData::class.java)!!
                    user.id = key
                    response.data = listOf(user)
                    callback.onCallback(response)
                }
            }
    }
}