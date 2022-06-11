package com.amaurov.fakeinsta.fragments

import android.content.ContentValues
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.amaurov.fakeinsta.R
import com.amaurov.fakeinsta.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.UnsupportedApiCallException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
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
        binding.btnLogin.setOnClickListener {
            loginUser()
        }

        binding.btnGoogleLogin.setOnClickListener() {
            view?.findNavController()?.navigate(R.id.action_login_to_googleSignIn)
        }
    }

    private fun loginUser() {
        auth.signInWithEmailAndPassword(binding.etUsername.text.toString(), binding.etPassword.text.toString())
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    binding.testLogin.text = "Login successful"
                } else {
                    binding.testLogin.text = "Login failed"
                }
            }
    }

    private fun registerUser() {
        auth.createUserWithEmailAndPassword("amaurov@mail.com", "123456")
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    println("createUserWithEmail:success")
                } else {
                    println("createUserWithEmail:failure")
                }
            }
    }
}