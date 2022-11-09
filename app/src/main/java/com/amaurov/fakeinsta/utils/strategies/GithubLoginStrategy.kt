package com.amaurov.fakeinsta.utils.strategies

import androidx.fragment.app.FragmentActivity
import com.amaurov.fakeinsta.utils.annotations.Strategy
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.OAuthProvider

@Strategy
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