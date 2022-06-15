package com.amaurov.fakeinsta.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Layout
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.compose.ui.unit.dp
import androidx.core.view.marginLeft
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.amaurov.fakeinsta.R
import com.amaurov.fakeinsta.databinding.PostItemBinding
import com.amaurov.fakeinsta.fragments.HomeFragmentDirections
import com.amaurov.fakeinsta.viewmodels.PostViewModel

class PostsRecyclerViewAdapter(private val postList: List<PostViewModel>) : RecyclerView.Adapter<PostsRecyclerViewAdapter.ViewHolder>() {
    private var _binding: PostItemBinding? = null
    private val binding get() = _binding!!

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

    inner class ViewHolder(private val context : Context, private val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: PostViewModel) {
            binding.tvUsername.text = post.username
            binding.tvCaption.text = createCaptionText(post.username, post.caption)

            createHashtags(post.hashtags)
            binding.ivLiked.setOnClickListener {
                onLikePressed(post)
            }
        }

        private fun createCaptionText(username: String, caption: String) : SpannableStringBuilder {
            val ssb = SpannableStringBuilder()
            val sUsername = SpannableString(username)
            sUsername.setSpan(StyleSpan(Typeface.BOLD), 0, sUsername.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            ssb.append(sUsername)
            ssb.append(" $caption")

            return ssb
        }

        private fun createHashtags(hashtags: List<String>) {
            for (hashtag in hashtags) {
                var hashtagTextView = TextView(context)
                hashtagTextView.text = hashtag
                hashtagTextView.setTextColor(Color.BLUE)

                if (hashtags.indexOf(hashtag) != 0) {
                    var layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams.setMargins(5, 0, 0, 0)
                    hashtagTextView.layoutParams = layoutParams
                }

                hashtagTextView.setOnClickListener {
                    onHashtagClickedEvent(it as TextView)
                }

                binding.llHashtagContainer.addView(hashtagTextView);
            }
        }

        private fun onHashtagClickedEvent(tv : TextView) {
            val action = HomeFragmentDirections.actionHomeToHashtagPostResults(tv.text.toString())
            tv.findNavController().navigate(action)
        }

        private fun onLikePressed(post: PostViewModel) {
            post.isLiked = !post.isLiked

            if (post.isLiked)
                binding.ivLiked.setImageResource(R.drawable.heart)
            else binding.ivLiked.setImageResource(R.drawable.heart_outline)
        }
    }
}