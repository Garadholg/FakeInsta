package com.amaurov.fakeinsta.fragments

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.amaurov.fakeinsta.R
import com.amaurov.fakeinsta.dao.models.Post
import com.amaurov.fakeinsta.dao.responses.FirebaseResponse
import com.amaurov.fakeinsta.databinding.FragmentPostDetailsBinding
import com.amaurov.fakeinsta.utils.Auth
import com.amaurov.fakeinsta.utils.GenericCallback
import com.amaurov.fakeinsta.utils.adapters.StringImageAdapter
import com.amaurov.fakeinsta.viewmodels.PostViewModel
import java.io.File
import java.io.FileOutputStream

class PostDetailsFragment : Fragment() {
    private lateinit var viewModel: PostViewModel
    private lateinit var selectedPost: Post

    private val args: PostDetailsFragmentArgs by navArgs()

    private var _binding: FragmentPostDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[PostViewModel::class.java]
        getPostData()
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnDownloadPicture.setOnClickListener {
            val bitmapImg = (binding.itemPostDetails.ivPicture.drawable as BitmapDrawable).bitmap
            val dirFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path +"/FakeInsta/")

            if (!dirFile.exists())
                dirFile.mkdirs()

            try {
                val fos = FileOutputStream(File(dirFile, selectedPost.id + ".png"))
                bitmapImg.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.flush()
                fos.close()
            } catch (e: Exception) {
                val msg = e.message
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getPostData() {
        viewModel.getPostById(args.postId, object: GenericCallback<Post> {
            override fun onCallback(response: FirebaseResponse<Post>) {
                selectedPost = response.data!![0]
                binding.itemPostDetails.tvUsername.text = selectedPost.userName
                binding.itemPostDetails.ivPicture.setImageBitmap(StringImageAdapter(selectedPost.picture!!).showImage())
                binding.itemPostDetails.tvCaption.text = createCaptionText(selectedPost.userName, selectedPost.caption)
                createHashtags(selectedPost.hashtags)
                setEditButtonVisibility()
            }
        })
    }

    private fun createCaptionText(username: String?, caption: String?) : SpannableStringBuilder {
        val ssb = SpannableStringBuilder()
        val sUsername = SpannableString(username)
        sUsername.setSpan(StyleSpan(Typeface.BOLD), 0, sUsername.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ssb.append(sUsername)
        ssb.append(" $caption")

        return ssb
    }

    private fun createHashtags(hashtags: HashMap<String, Int>) {
        for (hashtag in hashtags) {
            val hashtagTextView = TextView(context)
            """#${hashtag.key}""".also { hashtagTextView.text = it }
            hashtagTextView.setTextColor(Color.BLUE)

            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(0, 0, 5, 0)
            hashtagTextView.layoutParams = layoutParams

            binding.itemPostDetails.llHashtagContainer.removeAllViews()
            binding.itemPostDetails.llHashtagContainer.addView(hashtagTextView)
        }
    }

    private fun setEditButtonVisibility() {
        if (Auth.currentUser != null && Auth.currentUser?.id == selectedPost.userId) {
            binding.btnEditPost.visibility = View.VISIBLE
        } else {
            binding.btnEditPost.visibility = View.GONE
        }
    }
}