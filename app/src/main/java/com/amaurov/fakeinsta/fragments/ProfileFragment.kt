package com.amaurov.fakeinsta.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amaurov.fakeinsta.R
import com.amaurov.fakeinsta.dao.models.UserData
import com.amaurov.fakeinsta.dao.responses.FirebaseResponse
import com.amaurov.fakeinsta.databinding.FragmentProfileBinding
import com.amaurov.fakeinsta.utils.*
import com.amaurov.fakeinsta.viewmodels.UserDataViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private val userDataVM = UserDataViewModel()

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        if (auth.currentUser != null && Auth.currentUser != null) {
            binding.llNoUserLogged.visibility = View.GONE
            binding.svViewProfile.visibility = View.VISIBLE
            bindProfileView()
        } else {
            binding.svViewProfile.visibility = View.GONE
            binding.svEditProfile.visibility = View.GONE
            binding.llNoUserLogged.visibility = View.VISIBLE
        }

        setupListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupListeners() {
        binding.btnProfileLogin.setOnClickListener {
            requireParentFragment().requireParentFragment().findNavController().navigate(R.id.action_main_to_signIn)
        }

        binding.btnProfileLogout.setOnClickListener {
            Auth.currentUser = null
            Firebase.auth.signOut()
            binding.svViewProfile.visibility = View.GONE
            binding.llNoUserLogged.visibility = View.VISIBLE
        }

        binding.btnProfileEdit.setOnClickListener {
            bindEditFields()
            binding.svViewProfile.visibility = View.GONE
            binding.svEditProfile.visibility = View.VISIBLE
        }

        binding.btnSaveProfileChanges.setOnClickListener {
            requireContext().hideKeyboard(it)
            
            val userData = getUpdatedUserData()
            userDataVM.updateUser(userData, object: GenericCallback<UserData> {
                override fun onCallback(response: FirebaseResponse<UserData>) {
                    Auth.currentUser = response.data!![0]
                    bindProfileView()
                    binding.svEditProfile.visibility = View.GONE
                    binding.svViewProfile.visibility = View.VISIBLE
                }

            })
        }

        binding.btnCancelEdit.setOnClickListener {
            requireContext().hideKeyboard(it)
            binding.svEditProfile.visibility = View.GONE
            binding.svViewProfile.visibility = View.VISIBLE
        }
        
        binding.tfUsername.editText?.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                requireContext().hideKeyboard(v)
            }
        }

        binding.civProfilePictureEdit.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            galleryImagePickedResult.launch(galleryIntent)
        }
    }

    private fun bindProfileView() {
        binding.civProfilePicture.setImageBitmap(Auth.currentUser!!.profilePicture!!.toBitmapImage())
        binding.tvUsername.text = Auth.currentUser!!.username
        binding.tvEmail.text = Auth.currentUser!!.email
        binding.tvSubscription.text = Auth.currentUser!!.subscriptionType
    }

    private fun bindEditFields() {
        binding.civProfilePictureEdit.setImageBitmap(Auth.currentUser!!.profilePicture!!.toBitmapImage())
        binding.tfUsername.editText?.setText(Auth.currentUser!!.username)
        binding.btgSubscription.check(
            when (Auth.currentUser!!.subscriptionType) {
                "Free" -> R.id.btnSubscriptionFree
                "Gold" -> R.id.btnSubscriptionGold
                "Platinum" -> R.id.btnSubscriptionPlat
                else -> R.id.btnSubscriptionFree
            }
        )
    }

    private fun getUpdatedUserData(): UserData {
        return UserData(
            Auth.currentUser!!.id,
            binding.tfUsername.editText?.text.toString(),
            Auth.currentUser!!.email,
            binding.civProfilePictureEdit.drawable.toBase64String(),
            getCheckedSubscription()
        )
    }

    private fun getCheckedSubscription(): String {
        return when (binding.btgSubscription.checkedButtonId) {
            R.id.btnSubscriptionFree -> binding.btnSubscriptionFree.text.toString()
            R.id.btnSubscriptionGold -> binding.btnSubscriptionGold.text.toString()
            R.id.btnSubscriptionPlat -> binding.btnSubscriptionPlat.text.toString()
            else -> ""
        }
    }

    private var galleryImagePickedResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                AppCompatActivity.RESULT_OK -> {
                    val image: Uri? = result.data?.data
                    binding.civProfilePictureEdit.setImageURI(image)
                }
            }
        }
}