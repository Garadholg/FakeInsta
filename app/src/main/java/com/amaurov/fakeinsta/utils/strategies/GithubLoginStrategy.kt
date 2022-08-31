package com.amaurov.fakeinsta.utils.strategies

import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import com.amaurov.fakeinsta.R
import com.amaurov.fakeinsta.dao.models.UserData
import com.amaurov.fakeinsta.dao.repositories.RepositoryFactory
import com.amaurov.fakeinsta.dao.responses.FirebaseResponse
import com.amaurov.fakeinsta.utils.Auth
import com.amaurov.fakeinsta.utils.GenericCallback
import com.amaurov.fakeinsta.utils.toBase64String
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.OAuthProvider
import kotlinx.coroutines.runBlocking

class GithubLoginStrategy(override var activity: FragmentActivity): LoginStrategy {
    override fun login(username: String?, password: String?): Task<AuthResult> {
        val provider = OAuthProvider.newBuilder("github.com")
        var pendingResultTask = auth.pendingAuthResult

        if (pendingResultTask != null) {
            pendingResultTask
                .addOnSuccessListener(activity) {
                    getUserData(auth.currentUser?.uid!!)
                }
                .addOnFailureListener {
                    // TODO("Handle errors")
                }
        } else {
            pendingResultTask = auth.startActivityForSignInWithProvider(activity, provider.build())
                .addOnSuccessListener {
                    createUser()
                }
                .addOnFailureListener {
                    // TODO("Handle errors)
                }
        }

        return pendingResultTask
    }
}