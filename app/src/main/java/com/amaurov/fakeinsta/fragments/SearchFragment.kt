package com.amaurov.fakeinsta.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amaurov.fakeinsta.adapters.PostSearchRecyclerViewAdapter
import com.amaurov.fakeinsta.databinding.FragmentSearchBinding
import com.amaurov.fakeinsta.viewmodels.PostViewModel

class SearchFragment : Fragment() {
    private lateinit var viewModel: PostViewModel
    private lateinit var rvSearchedPosts: RecyclerView
    private lateinit var rvAdapter: PostSearchRecyclerViewAdapter

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[PostViewModel::class.java]
        getData()
        bindRepeater()
        setupListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.etSearchKeyword.text.clear()
        _binding = null
    }

    private fun setupListeners() {
        binding.btnSearchPosts.setOnClickListener {
            val searchText = binding.etSearchKeyword.text.toString()
            if (searchText.isNullOrBlank())
                getData()
            else
                getPostsByKeyword(searchText)

        }
    }

    private fun bindRepeater() {
        rvSearchedPosts = binding.rvSearchedPosts
        rvSearchedPosts.layoutManager = GridLayoutManager(this.context, 3)
        rvAdapter = PostSearchRecyclerViewAdapter()
        rvSearchedPosts.adapter = rvAdapter
    }

    private fun getData() {
        viewModel.getAllPosts.observe(viewLifecycleOwner) {
            it.data?.let { posts ->
                rvAdapter.updatePosts(posts)
            }
        }
    }

    private fun getPostsByKeyword(keyword: String) {
        viewModel.getByKeyword(keyword).observe(viewLifecycleOwner) {
            it.data?.let { posts ->
                rvAdapter.updatePosts(posts)
            }
        }
    }
}