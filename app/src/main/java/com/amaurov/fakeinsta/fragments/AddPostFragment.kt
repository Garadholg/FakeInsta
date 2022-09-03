package com.amaurov.fakeinsta.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.amaurov.fakeinsta.R
import com.amaurov.fakeinsta.dao.models.Post
import com.amaurov.fakeinsta.dao.responses.FirebaseResponse
import com.amaurov.fakeinsta.databinding.FragmentAddPostBinding
import com.amaurov.fakeinsta.utils.Auth
import com.amaurov.fakeinsta.utils.GenericCallback
import com.amaurov.fakeinsta.utils.toBase64String
import com.amaurov.fakeinsta.viewmodels.PostViewModel
import java.time.LocalDateTime

class AddPostFragment : Fragment() {
    private val postVM = PostViewModel()
    
    private var _binding: FragmentAddPostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        binding.ivNewPostPicture.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            galleryImagePickedResult.launch(galleryIntent)
        }

        binding.tvSelectPicture.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            galleryImagePickedResult.launch(galleryIntent)
        }

        binding.etNewPostHashtags.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.etNewPostHashtags.text.toString().isEmpty()) {
                binding.etNewPostHashtags.setText("#")
            }
        }

        binding.etNewPostHashtags.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (event.action != KeyEvent.ACTION_DOWN)
                return@OnKeyListener false

            when (keyCode) {
                KeyEvent.KEYCODE_DEL -> {
                    val text = binding.etNewPostHashtags.text
                    if (text.last() == '#') {
                        binding.etNewPostHashtags.setText(text.substring(0, text.length -2))
                        binding.etNewPostHashtags.setSelection(binding.etNewPostHashtags.length())
                        return@OnKeyListener true
                    }
                    else {
                        return@OnKeyListener false
                    }
                }
                KeyEvent.KEYCODE_SPACE -> {
                    binding.etNewPostHashtags.setText(binding.etNewPostHashtags.text.toString() + " #")
                    binding.etNewPostHashtags.setSelection(binding.etNewPostHashtags.length())
                    return@OnKeyListener true
                }
            }

            false
        })
        
        binding.btnCreateNewPost.setOnClickListener {
            val post = Post.Builder()
                .userId(Auth.currentUser!!.id!!)
                .userName(Auth.currentUser!!.username!!)
                .picture(binding.ivNewPostPicture.drawable.toBase64String())
                .caption(binding.etNewPostCaption.text.toString())
                .hashtags(getHashtags())
                .timeOfPosting(LocalDateTime.now())
                .build()
            
            postVM.createPost(post, object: GenericCallback<Post> {
                override fun onCallback(response: FirebaseResponse<Post>) {
                    //ToDO("Shouldnt I go back to home or something? XD")
                    Log.i("POST", "Post created!")
                }
            })
        }
    }

    private fun getHashtags(): HashMap<String, Int> {
        val hashtags = HashMap<String, Int>()

        binding.etNewPostHashtags.text.toString()
            .split(" ")
            .forEach {
                val tag = it.removePrefix("#").lowercase()
                hashtags[tag] = 1
            }

        return hashtags
    }

    private var galleryImagePickedResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        when (result.resultCode) {
            AppCompatActivity.RESULT_OK -> {
                val image: Uri? = result.data?.data
                binding.ivNewPostPicture.setImageURI(image)
                binding.btnCreateNewPost.setBackgroundColor(resources.getColor(R.color.bright_blue, context?.theme))
                binding.btnCreateNewPost.isEnabled = true
                binding.tvSelectPicture.visibility = View.GONE
                binding.ivNewPostPicture.visibility = View.VISIBLE
            }
        }
    }
}