package com.amaurov.fakeinsta.adapters

import android.graphics.Typeface
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amaurov.fakeinsta.databinding.PostItemBinding
import com.amaurov.fakeinsta.viewmodels.PostViewModel

class PostsRecyclerViewAdapter(private val postList: List<PostViewModel>) : RecyclerView.Adapter<PostsRecyclerViewAdapter.ViewHolder>() {
    private var _binding: PostItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        _binding = PostItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(holder, postList[position])
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    inner class ViewHolder(private val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(holder: ViewHolder, post: PostViewModel) {
            binding.username.text = post.username
            binding.caption.text = createCaptionText(post.username, post.caption)

            createHashtags()
        }

        private fun createCaptionText(username: String, caption: String) : SpannableStringBuilder {
            val ssb = SpannableStringBuilder()
            val sUsername = SpannableString(username)
            sUsername.setSpan(StyleSpan(Typeface.BOLD), 0, sUsername.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            ssb.append(sUsername)
            ssb.append(" $caption")

            return ssb
        }

        private fun createHashtags() {

        }
    }
}