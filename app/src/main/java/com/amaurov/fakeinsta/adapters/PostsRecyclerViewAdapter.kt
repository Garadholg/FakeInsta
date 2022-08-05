package com.amaurov.fakeinsta.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.amaurov.fakeinsta.R
import com.amaurov.fakeinsta.dao.models.Post
import com.amaurov.fakeinsta.databinding.PostItemBinding
import com.amaurov.fakeinsta.fragments.HomeFragment
import com.amaurov.fakeinsta.fragments.HomeFragmentDirections
import com.amaurov.fakeinsta.utils.Auth

class PostsRecyclerViewAdapter : RecyclerView.Adapter<PostsRecyclerViewAdapter.ViewHolder>() {
    private var _binding: PostItemBinding? = null
    private val binding get() = _binding!!

    private var postList: List<Post> = emptyList()
    //TODO("Testing like event shenanigans, delete later)
    var likeEvent: (post: Post, isLiked: Boolean) -> Unit? = { _: Post, _: Boolean -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        _binding = PostItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(parent.context, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(postList[position])
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    fun updatePosts(data: List<Post>) {
        postList = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val context : Context, private val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.tvUsername.text = post.userName
            binding.tvCaption.text = createCaptionText(post.userName, post.caption)

            createHashtags(post.hashtags)

            if (post.likes.contains(Auth.currentUser?.id))
                binding.ivLiked.setImageResource(R.drawable.heart)
            else binding.ivLiked.setImageResource(R.drawable.heart_outline)

            binding.ivLiked.setOnClickListener {
                onLikePressed(post)
            }
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

                hashtagTextView.setOnClickListener {
                    onHashtagClickedEvent(it as TextView)
                }

                binding.llHashtagContainer.removeAllViews()
                binding.llHashtagContainer.addView(hashtagTextView)
            }
        }

        private fun onHashtagClickedEvent(tv : TextView) {
            val action = HomeFragmentDirections.actionHomeToHashtagPostResults(tv.text.toString())
            tv.findNavController().navigate(action)
        }

        private fun onLikePressed(post: Post) {
            likeEvent(post, post.likes.contains(Auth.currentUser!!.id))

            if (post.likes.contains(Auth.currentUser!!.id)) {
                post.likes.remove(Auth.currentUser!!.id!!)
            } else {
                post.likes[Auth.currentUser!!.id!!] = 1
            }

            notifyDataSetChanged()
        }
    }
}