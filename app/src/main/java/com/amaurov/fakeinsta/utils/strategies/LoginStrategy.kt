package com.amaurov.fakeinsta.utils.strategies

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.amaurov.fakeinsta.MainActivity
import com.amaurov.fakeinsta.R
import com.amaurov.fakeinsta.dao.models.UserData
import com.amaurov.fakeinsta.dao.repositories.Repository
import com.amaurov.fakeinsta.dao.repositories.RepositoryFactory
import com.amaurov.fakeinsta.dao.responses.FirebaseResponse
import com.amaurov.fakeinsta.utils.Auth
import com.amaurov.fakeinsta.utils.GenericCallback
import com.amaurov.fakeinsta.utils.state.AuthStateContext
import com.amaurov.fakeinsta.utils.toBase64String
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking

interface LoginStrategy {
    val auth: FirebaseAuth
        get() = Firebase.auth
    val authState: AuthStateContext
        get() = AuthStateContext()
    var activity: FragmentActivity
    val repository: Repository<UserData>
        get() = RepositoryFactory.getRepository()!!

    fun login(username: String? = null, password: String? = null): Task<*>

    fun createUser() = runBlocking {
        val user = UserData(
            auth.currentUser?.uid,
            auth.currentUser?.email?.split("@")?.get(0),
            auth.currentUser?.email,
            ContextCompat.getDrawable(activity, R.drawable.cat_profile)!!.toBase64String(),
            "Free"
        )

        repository.create(user, object: GenericCallback<UserData> {
            override fun onCallback(response: FirebaseResponse<UserData>) {
                Auth.currentUser = response.data?.get(0)
            }
        })
    }

    fun getUserData(id: String) = runBlocking {
        repository.getById(id, object: GenericCallback<UserData> {
            override fun onCallback(response: FirebaseResponse<UserData>) {
                Auth.currentUser = response.data?.get(0)
            }
        })
    }
}