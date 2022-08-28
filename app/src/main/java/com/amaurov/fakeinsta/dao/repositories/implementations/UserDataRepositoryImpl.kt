package com.amaurov.fakeinsta.dao.repositories.implementations

import com.amaurov.fakeinsta.dao.models.UserData
import com.amaurov.fakeinsta.dao.repositories.Repository
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
): Repository<UserData> {
    override suspend fun create(newEntity: UserData, callback: GenericCallback<UserData>) {
        val response = FirebaseResponse<UserData>()

        userDataRef.child(newEntity.id!!).setValue(newEntity)
            .addOnCompleteListener {
                response.data = listOf(newEntity)
                callback.onCallback(response)
            }
    }

    override suspend fun getAll(): FirebaseResponse<UserData> {
        //TODO("Not yet implemented")
        return FirebaseResponse()
    }

    override suspend fun getById(id: String, callback: GenericCallback<UserData>) {
        val response = FirebaseResponse<UserData>()

        userDataRef.child(id).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    var user = it.result.getValue(UserData::class.java)!!
                    user.id = id
                    response.data = listOf(user)
                    callback.onCallback(response)
                }
            }
    }

    override suspend fun update(newValue: UserData, callback: GenericCallback<Boolean>) {
        val response = FirebaseResponse<Boolean>()

        userDataRef.child(newValue.id!!).setValue(newValue)
            .addOnCompleteListener {
                response.data = listOf(true)
                callback.onCallback(response)
            }
    }
}