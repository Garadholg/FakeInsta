package com.amaurov.fakeinsta.utils.strategies

import androidx.fragment.app.FragmentActivity
import com.amaurov.fakeinsta.utils.state.RegisteredUserState
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

class EmailLoginStrategy(override var activity: FragmentActivity): LoginStrategy {
    override fun login(username: String?, password: String?): Task<AuthResult> {
        if (username != null && password != null) {
            return auth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(activity) { result ->
                    if (result.isSuccessful) {
                        getUserData(auth.currentUser?.uid!!)
                        authState.changeState(RegisteredUserState())
                    } else {
                        // TODO("Display error message")
                    }
                }
        }

        return auth.signInWithEmailAndPassword("", "")
    }
}