package com.amaurov.fakeinsta.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amaurov.fakeinsta.adapters.PostsRecyclerViewAdapter
import com.amaurov.fakeinsta.dao.responses.FirebaseResponse
import com.amaurov.fakeinsta.dao.utils.GenericCallback
import com.amaurov.fakeinsta.databinding.FragmentHomeBinding
import com.amaurov.fakeinsta.viewmodels.PostViewModel

class HomeFragment : Fragment() {
    private lateinit var viewModel: PostViewModel
    private lateinit var rvPosts: RecyclerView
    private lateinit var rvAdapter: PostsRecyclerViewAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(PostViewModel::class.java)
        getData()
        bindRepeater()
    }

    private fun bindRepeater() {
        rvPosts = binding.rvPosts
        rvPosts.layoutManager = LinearLayoutManager(this.context)
        rvAdapter = PostsRecyclerViewAdapter()
        rvAdapter.likeEvent = onPostLiked
        rvPosts.adapter = rvAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getData() {
        viewModel.responseLiveData.observe(viewLifecycleOwner) {
            it.data?.let { posts ->
                rvAdapter.updatePosts(posts)
            }
        }
    }

    private val onPostLiked : () -> Unit = {
        viewModel.updateLikes(object: GenericCallback<Boolean> {
            override fun onCallback(value: FirebaseResponse<Boolean>) {
                //ToDo("change post data in recyclerview")
            }

        })
    }
}