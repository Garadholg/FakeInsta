package com.amaurov.fakeinsta.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.amaurov.fakeinsta.R
import com.amaurov.fakeinsta.dao.models.UserData
import com.amaurov.fakeinsta.dao.responses.FirebaseResponse
import com.amaurov.fakeinsta.databinding.FragmentLoginBinding
import com.amaurov.fakeinsta.utils.Auth
import com.amaurov.fakeinsta.utils.GenericCallback
import com.amaurov.fakeinsta.utils.hideKeyboard
import com.amaurov.fakeinsta.utils.strategies.EmailLoginStrategy
import com.amaurov.fakeinsta.utils.strategies.GithubLoginStrategy
import com.amaurov.fakeinsta.utils.strategies.GoogleLoginStrategy
import com.amaurov.fakeinsta.utils.strategies.LoginContext
import com.amaurov.fakeinsta.viewmodels.UserDataViewModel
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private val userDataVM = UserDataViewModel()
    private lateinit var loginContext: LoginContext

    private var showOneTapUI = true
    private lateinit var oneTapClient: SignInClient

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
        loginContext = LoginContext(requireActivity())
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
            loginContext.changeStrategy(EmailLoginStrategy(requireActivity()))
            loginContext.login(binding.tfEmail.editText?.text.toString(), binding.tfPassword.editText?.text.toString())
                .addOnCompleteListener(requireActivity()) { result ->
                    if (result.isSuccessful) {
                        view?.findNavController()?.popBackStack()
                    } else {
                        // TODO("Display error message")
                    }
                }
        }

        binding.btnGoogleSignIn.setOnClickListener {
            loginContext.changeStrategy(GoogleLoginStrategy(requireActivity(), googleSignInResultHandler))
            loginContext.login(null, null)
        }

        binding.btnGithubSignIn.setOnClickListener {
            loginContext.changeStrategy(GithubLoginStrategy(requireActivity()))
            loginContext.login(null, null)
                .addOnCompleteListener(requireActivity()) { result  ->
                    if (result.isSuccessful) {
                        view?.findNavController()?.popBackStack()
                    }
                }
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

    private fun createUser() {
        val user = UserData(
            auth.currentUser?.uid,
            auth.currentUser?.email?.split("@")?.get(0),
            auth.currentUser?.email,
            "",
            "Free"
        )

        userDataVM.createUser(user, object: GenericCallback<UserData> {
            override fun onCallback(response: FirebaseResponse<UserData>) {
                Auth.currentUser = response.data?.get(0)
                view?.findNavController()?.popBackStack()
            }
        })
    }

}