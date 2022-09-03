package com.amaurov.fakeinsta.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.amaurov.fakeinsta.R
import com.amaurov.fakeinsta.dao.models.Post
import com.amaurov.fakeinsta.dao.responses.FirebaseResponse
import com.amaurov.fakeinsta.databinding.FragmentEditPostBinding
import com.amaurov.fakeinsta.utils.Auth
import com.amaurov.fakeinsta.utils.GenericCallback
import com.amaurov.fakeinsta.utils.adapters.StringImageAdapter
import com.amaurov.fakeinsta.utils.toBase64String
import com.amaurov.fakeinsta.viewmodels.PostViewModel
import java.time.LocalDateTime

class EditPostFragment : Fragment() {
    private lateinit var postVM: PostViewModel
    private lateinit var editPost: Post
    private val args: EditPostFragmentArgs by navArgs()

    private var _binding: FragmentEditPostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postVM = ViewModelProvider(this)[PostViewModel::class.java]
        getPostData()
        setupListeners()
    }

    private fun getPostData() {
        postVM.getPostById(args.postId, object: GenericCallback<Post> {
            override fun onCallback(response: FirebaseResponse<Post>) {
                editPost = response.data!![0]
                binding.ivEditPostPicture.setImageBitmap(StringImageAdapter(editPost.picture!!).showImage())
                binding.etEditPostCaption.setText(editPost.caption)
                createHashtags()
            }
        })
    }

    private fun setupListeners() {
        binding.ivEditPostPicture.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            galleryImagePickedResult.launch(galleryIntent)
        }

        binding.etEditPostHashtags.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (event.action != KeyEvent.ACTION_DOWN)
                return@OnKeyListener false

            when (keyCode) {
                KeyEvent.KEYCODE_DEL -> {
                    val text = binding.etEditPostHashtags.text
                    if (text.last() == '#') {
                        binding.etEditPostHashtags.setText(text.substring(0, text.length -2))
                        binding.etEditPostHashtags.setSelection(binding.etEditPostHashtags.length())
                        return@OnKeyListener true
                    }
                    else {
                        return@OnKeyListener false
                    }
                }
                KeyEvent.KEYCODE_SPACE -> {
                    binding.etEditPostHashtags.setText(binding.etEditPostHashtags.text.toString() + " #")
                    binding.etEditPostHashtags.setSelection(binding.etEditPostHashtags.length())
                    return@OnKeyListener true
                }
            }

            false
        })

        binding.btnUpdatePost.setOnClickListener {
            val post = Post.Builder()
                .userId(Auth.currentUser!!.id!!)
                .userName(Auth.currentUser!!.username!!)
                .picture(binding.ivEditPostPicture.drawable.toBase64String())
                .caption(binding.etEditPostCaption.text.toString())
                .hashtags(getHashtags())
                .timeOfPosting(LocalDateTime.now())
                .build()

            postVM.updatePost(post, object: GenericCallback<Boolean> {
                override fun onCallback(response: FirebaseResponse<Boolean>) {
                    //ToDO("Shouldnt I go back to post details or something? XD")
                    Log.i("POST", "Post created!")
                }
            })
        }

        binding.btnCancelEdit.setOnClickListener {
            view?.findNavController()?.popBackStack(R.id.postDetailsFragment, false)
        }
    }

    private fun createHashtags() {
        val sb = StringBuilder()
        editPost.hashtags.keys.forEach {
            sb.append("#")
                .append(it)
                .append(" ")
        }

        binding.etEditPostHashtags.setText(sb.toString().trimEnd())
    }

    private fun getHashtags(): HashMap<String, Int> {
        val hashtags = HashMap<String, Int>()

        binding.etEditPostHashtags.text.toString()
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
                binding.ivEditPostPicture.setImageURI(image)
            }
        }
    }
}