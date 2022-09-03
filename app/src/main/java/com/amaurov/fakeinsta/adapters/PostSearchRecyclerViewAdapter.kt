package com.amaurov.fakeinsta.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.amaurov.fakeinsta.dao.models.Post
import com.amaurov.fakeinsta.databinding.PostThumbnailBinding
import com.amaurov.fakeinsta.fragments.HomeFragmentDirections
import com.amaurov.fakeinsta.fragments.SearchFragmentDirections
import com.amaurov.fakeinsta.utils.adapters.StringImageAdapter

class PostSearchRecyclerViewAdapter: RecyclerView.Adapter<PostSearchRecyclerViewAdapter.ViewHolder>() {
    private var postList: List<Post> = emptyList()

    private var _binding: PostThumbnailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        _binding = PostThumbnailBinding.inflate(layoutInflater, parent, false)
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

    inner class ViewHolder(
        private val context: Context,
        private val binding: PostThumbnailBinding)
        : RecyclerView.ViewHolder(binding.root) {

            fun bind(post: Post) {
                binding.ivPostPictureThumbnail.setImageBitmap(StringImageAdapter(post.picture!!).showImage())

                binding.ivPostPictureThumbnail.setOnClickListener {
                    val action = SearchFragmentDirections.actionSearchToPostDetails(post.id!!)
                    it.findNavController().navigate(action)
                }
            }
    }
}