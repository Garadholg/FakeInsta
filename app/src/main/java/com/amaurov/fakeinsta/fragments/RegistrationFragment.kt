package com.amaurov.fakeinsta.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.amaurov.fakeinsta.R
import com.amaurov.fakeinsta.databinding.FragmentRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegistrationFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        setupListeners()
    }

    private fun setupListeners() {
        binding.tvLogin.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_registration_to_login)
        }

        binding.btnRegister.setOnClickListener {
            val username = binding.tfUsername.editText?.text.toString()
            val password = binding.tfPassword.editText?.text.toString()
            val confirmPassword = binding.tfConfirmPassword.editText?.text.toString()

            if(validateUserInput(username, password, confirmPassword)) {
                startEmailSignUp(username, password)
            }
        }
    }

    private fun validateUserInput(username: String, password: String, confirmPassword: String): Boolean {
        // TODO("Better error handling")
        return !((username.isBlank() || password.isBlank() || (password != confirmPassword)))
    }

    private fun startEmailSignUp(username: String, password: String) {
        auth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener(requireActivity()) { result ->
                if (result.isSuccessful) {
                    // TODO("Navigate to caller page")
                    println("createUserWithEmail:success")
                } else {
                    println("createUserWithEmail:failure")
                    // TODO("Some error handling")
                }
            }
    }
}