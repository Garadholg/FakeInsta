package com.amaurov.fakeinsta.fragments

import android.content.ContentValues
import android.content.Context
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.amaurov.fakeinsta.R
import com.amaurov.fakeinsta.dao.models.UserData
import com.amaurov.fakeinsta.dao.responses.FirebaseResponse
import com.amaurov.fakeinsta.databinding.FragmentLoginBinding
import com.amaurov.fakeinsta.utils.Auth
import com.amaurov.fakeinsta.utils.GenericCallback
import com.amaurov.fakeinsta.utils.hideKeyboard
import com.amaurov.fakeinsta.viewmodels.UserDataViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private val userDataVM = UserDataViewModel()

    private var showOneTapUI = true
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var signUpRequest: BeginSignInRequest
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        setupListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupListeners() {
        binding.tvRegistration.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_login_to_registration)
        }

        binding.btnUsernameSignIn.setOnClickListener {
            requireContext().hideKeyboard(it)
            startEmailSignIn()
        }

        binding.btnGoogleSignIn.setOnClickListener {
            startGoogleSignIn()
        }

        binding.btnGithubSignIn.setOnClickListener {
            startGithubSingIn()
        }
    }

    private fun startEmailSignIn() {
        auth.signInWithEmailAndPassword(binding.tfEmail.editText?.text.toString(), binding.tfPassword.editText?.text.toString())
            .addOnCompleteListener(requireActivity()) { result ->
                if (result.isSuccessful) {
                    getUserData(auth.currentUser?.uid!!)
                } else {
                    // TODO("Display error message")
                }
            }
    }

    private fun startGoogleSignIn() {
        oneTapClient = Identity.getSignInClient(requireActivity())
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId("290542329001-o38o8ra1tu6fqp0fvbjvfvdi6979fv59.apps.googleusercontent.com")
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .build()

        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(requireActivity()) { result ->
                googleSignInResultHandler.launch(IntentSenderRequest.Builder(result.pendingIntent.intentSender).build())
            }
            .addOnFailureListener(requireActivity()) { e ->
                e.localizedMessage?.let { Log.d(ContentValues.TAG, it) }
                startGoogleSignUp()
            }
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

        oneTapClient.beginSignIn(signUpRequest)
            .addOnSuccessListener(requireActivity()) { result ->
                try {
                    googleSignInResultHandler.launch(IntentSenderRequest.Builder(result.pendingIntent.intentSender).build())
                } catch (e: IntentSender.SendIntentException) {
                    Log.d(ContentValues.TAG, "Cannot start OneTap UI, no Google acc on device")
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
                            val firebaseCredentials = GoogleAuthProvider.getCredential(idToken, null)
                            auth.signInWithCredential(firebaseCredentials)
                                .addOnCompleteListener(requireActivity()) { task ->
                                    if (task.isSuccessful) {
                                        Log.d(ContentValues.TAG, "Auth successful")
                                        createUser()
                                    }
                                    else {
                                        // TODO("Display an error message")
                                    }
                                }
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

    private fun startGithubSingIn() {
        val provider = OAuthProvider.newBuilder("github.com")
        val pendingResultTask = auth.pendingAuthResult

        if (pendingResultTask != null) {
            pendingResultTask
                .addOnSuccessListener {
                    getUserData(auth.currentUser?.uid!!)
                }
                .addOnFailureListener {
                    // TODO("Handle errors")
                }
        } else {
            auth.startActivityForSignInWithProvider(requireActivity(), provider.build())
                .addOnSuccessListener {
                    createUser()
                }
                .addOnFailureListener {
                    // TODO("Handle errors)
                }
        }
    }

    private fun createUser() {
        val user = UserData(
            auth.currentUser?.uid,
            auth.currentUser?.email?.split("@")?.get(0),
            auth.currentUser?.email,
            "Free"
        )

        userDataVM.createUser(user, object: GenericCallback<UserData> {
            override fun onCallback(response: FirebaseResponse<UserData>) {
                Auth.currentUser = response.data?.get(0)
                view?.findNavController()?.popBackStack()
            }
        })
    }

    private fun getUserData(id: String) {
        userDataVM.getUserById(id, object: GenericCallback<UserData> {
            override fun onCallback(response: FirebaseResponse<UserData>) {
                Auth.currentUser = response.data?.get(0)
                view?.findNavController()?.popBackStack()
            }

        })
    }

}