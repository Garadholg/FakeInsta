package com.amaurov.fakeinsta.fragments

import android.content.ContentValues
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.amaurov.fakeinsta.R
import com.amaurov.fakeinsta.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private var showOneTapUI = true
    private lateinit var oneTapClient: SignInClient
    private lateinit var signUpRequest: BeginSignInRequest
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        setupListeners()
    }

    private fun setupListeners() {
        binding.tvRegistration.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_login_to_registration)
        }

        binding.btnLogin.setOnClickListener {
            startEmailSignIn()
        }

        binding.btnGoogleLogin.setOnClickListener() {
            startGoogleSignIn()
        }
    }

    private fun startEmailSignIn() {
        auth.signInWithEmailAndPassword(binding.tfUsername.editText?.text.toString(), binding.tfPassword.editText?.text.toString())
            .addOnCompleteListener(requireActivity()) { result ->
                if (result.isSuccessful) {
                    view?.findNavController()?.popBackStack()
                } else {
                    TODO("Display error message")
                }
            }
    }

    private fun startGoogleSignIn() {
        oneTapClient = Identity.getSignInClient(requireActivity())
        signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId("7160607213-7ikb5o5crabfpgr7t6lm42vqsukhi1bn.apps.googleusercontent.com")
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .build()

        oneTapClient.beginSignIn(signUpRequest)
            .addOnSuccessListener(requireActivity()) { result ->
                try {
                    googleSignInResultHandler.launch(IntentSenderRequest.Builder(result.pendingIntent.intentSender).build())
                } catch (e: IntentSender.SendIntentException) {
                    Log.e(ContentValues.TAG, "Cannot start OneTap UI, no Google acc on device")
                }
            }
            .addOnFailureListener(requireActivity()) { e ->
                e.localizedMessage?.let { Log.d(ContentValues.TAG, it) }
            }
    }

    private val googleSignInResultHandler = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()) { result ->
        when (result.resultCode) {
            AppCompatActivity.RESULT_OK -> {
                try {
                    val googleCredential = oneTapClient.getSignInCredentialFromIntent(result.data)
                    val idToken = googleCredential.googleIdToken
                    when {
                        idToken != null -> {
                            // Got an ID token from Google. Use it to authenticate
                            // with your backend.
                            Log.d(ContentValues.TAG, "Got ID token.")
                        }
                        else -> {
                            // Shouldn't happen.
                            Log.d(ContentValues.TAG, "No ID token!")
                        }
                    }
                } catch (e: ApiException) {
                    when (e.statusCode) {
                        CommonStatusCodes.CANCELED -> {
                            showOneTapUI = false
                        }
                        CommonStatusCodes.NETWORK_ERROR -> {
                            val toast = Toast.makeText(context, "No Internet Connection!", Toast.LENGTH_LONG)
                            toast.show()
                        }
                        else -> {
                            Log.d(
                                ContentValues.TAG, "Couldn't get credential from result." +
                                        " (${e.localizedMessage})"
                            )
                        }
                    }
                }
            }
        }
    }
}