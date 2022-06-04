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
import androidx.recyclerview.widget.RecyclerView
import com.amaurov.fakeinsta.databinding.PostItemBinding
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
        holder.bind(holder, postList[position])
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    inner class ViewHolder(private val context : Context, private val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(holder: ViewHolder, post: PostViewModel) {
            binding.username.text = post.username
            binding.caption.text = createCaptionText(post.username, post.caption)

            createHashtags(post.hashtags)
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
                var hashtagText = TextView(context)
                hashtagText.text = hashtag
                hashtagText.setTextColor(Color.BLUE)

                if (hashtags.indexOf(hashtag) != 0) {
                    var layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    layoutParams.setMargins(5, 0, 0, 0)
                    hashtagText.layoutParams = layoutParams
                }

                binding.hashtagContainer.addView(hashtagText);
            }
        }
    }
}