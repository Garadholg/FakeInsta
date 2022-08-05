package com.amaurov.fakeinsta.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.amaurov.fakeinsta.R
import com.amaurov.fakeinsta.utils.Auth
import com.amaurov.fakeinsta.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        binding.testUser.text = if (auth.currentUser != null && Auth.currentUser != null) Auth.currentUser!!.username else "Not logged in"
        binding.btnProfileLogin.setOnClickListener {
            requireParentFragment().requireParentFragment().findNavController().navigate(R.id.action_main_to_signIn)
        }
        binding.btnProfileLogout.setOnClickListener {
            Auth.currentUser = null
            Firebase.auth.signOut()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}