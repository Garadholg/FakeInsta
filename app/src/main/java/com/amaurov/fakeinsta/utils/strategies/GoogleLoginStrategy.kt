package com.amaurov.fakeinsta.utils.strategies

import android.content.ContentValues
import android.content.IntentSender
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.tasks.Task

class GoogleLoginStrategy(
    override var activity: FragmentActivity,
    private val googleSignInResultHandler: ActivityResultLauncher<IntentSenderRequest>
): LoginStrategy {
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var signUpRequest: BeginSignInRequest
    private lateinit var signInResult: Task<BeginSignInResult>

    override fun login(username: String?, password: String?): Task<BeginSignInResult> {
        oneTapClient = Identity.getSignInClient(activity)
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId("290542329001-o38o8ra1tu6fqp0fvbjvfvdi6979fv59.apps.googleusercontent.com")
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .build()



        signInResult = oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(activity) { result ->
                googleSignInResultHandler.launch(IntentSenderRequest.Builder(result.pendingIntent.intentSender).build())
            }
            .addOnFailureListener(activity) { e ->
                e.localizedMessage?.let { Log.d(ContentValues.TAG, it) }
                startGoogleSignUp()
            }

        return signInResult
    }

    private fun startGoogleSignUp() {
        signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId("290542329001-o38o8ra1tu6fqp0fvbjvfvdi6979fv59.apps.googleusercontent.com")
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .build()

        signInResult = oneTapClient.beginSignIn(signUpRequest)
            .addOnSuccessListener(activity) { result ->
                try {
                    googleSignInResultHandler.launch(IntentSenderRequest.Builder(result.pendingIntent.intentSender).build())
                } catch (e: IntentSender.SendIntentException) {
                    Log.d(ContentValues.TAG, "Cannot start OneTap UI, no Google acc on device")
                }
            }
            .addOnFailureListener(activity) { e ->
                e.localizedMessage?.let { Log.d(ContentValues.TAG, it) }
            }
    }
}