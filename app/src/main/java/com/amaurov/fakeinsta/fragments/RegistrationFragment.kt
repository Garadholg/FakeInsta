package com.amaurov.fakeinsta.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.amaurov.fakeinsta.R
import com.amaurov.fakeinsta.dao.models.UserData
import com.amaurov.fakeinsta.dao.responses.FirebaseResponse
import com.amaurov.fakeinsta.utils.Auth
import com.amaurov.fakeinsta.utils.GenericCallback
import com.amaurov.fakeinsta.databinding.FragmentRegistrationBinding
import com.amaurov.fakeinsta.utils.hideKeyboard
import com.amaurov.fakeinsta.utils.toBase64String
import com.amaurov.fakeinsta.viewmodels.UserDataViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegistrationFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private val userDataVM = UserDataViewModel()

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO("Autofill for testing purposes, delete once done")
        binding.tfUsername.editText?.setText("amaurov")
        binding.tfEmail.editText?.setText("amaurov@mail.com")
        binding.tfPassword.editText?.setText("123456")
        binding.tfConfirmPassword.editText?.setText("123456")

        auth = Firebase.auth
        setupListeners()
    }

    private fun setupListeners() {
        binding.tvLogin.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_registration_to_login)
        }

        binding.btnRegister.setOnClickListener {
            requireContext().hideKeyboard(it)

            val username = binding.tfUsername.editText?.text.toString()
            val email = binding.tfEmail.editText?.text.toString()
            val password = binding.tfPassword.editText?.text.toString()
            val confirmPassword = binding.tfConfirmPassword.editText?.text.toString()

            if(validateUserInput(username, email, password, confirmPassword)) {
                startEmailSignUp(email, password)
            }
        }
    }

    private fun getCheckedSubscription(): String {
        return when (binding.btgSubscription.checkedButtonId) {
            R.id.btnSubscriptionFree -> binding.btnSubscriptionFree.text.toString()
            R.id.btnSubscriptionGold -> binding.btnSubscriptionGold.text.toString()
            R.id.btnSubscriptionPlat -> binding.btnSubscriptionPlat.text.toString()
            else -> ""
        }
    }

    private fun validateUserInput(username: String, email: String, password: String, confirmPassword: String): Boolean {
        // TODO("Better error handling")
        return !((username.isBlank() || email.isBlank() || password.isBlank() || (password != confirmPassword)))
    }

    private fun startEmailSignUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) {
                if (it.isSuccessful) saveUserData(email)
            }
            .addOnFailureListener(requireActivity()) {
                it.localizedMessage
            }
    }

    private fun saveUserData(email: String) {
        val userData = UserData(
            auth.currentUser!!.uid,
            binding.tfUsername.editText?.text.toString(),
            email,
            resources.getDrawable(R.drawable.cat_image, context?.theme).toBase64String(),
            getCheckedSubscription()
        )

        try {
            userDataVM.createUser(userData, object: GenericCallback<UserData> {
                override fun onCallback(response: FirebaseResponse<UserData>) {
                    Auth.currentUser = response.data?.get(0)
                    view?.findNavController()?.popBackStack(R.id.loginFragment, true)
                }
            })

        } catch (e: Exception) {
            e.localizedMessage
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}