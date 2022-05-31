package com.amaurov.fakeinsta.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amaurov.fakeinsta.R
import com.amaurov.fakeinsta.adapters.PostsRecyclerViewAdapter
import com.amaurov.fakeinsta.databinding.FragmentHomeBinding
import com.amaurov.fakeinsta.viewmodels.PostViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment() {
    private lateinit var postList: List<PostViewModel>
    private lateinit var rvPosts: RecyclerView
    private lateinit var rvAdapter: PostsRecyclerViewAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initList()
        bindRepeater()
    }

    // Currently hardcoded list for test purposes
    private fun initList() {
        postList = listOf(
            PostViewModel(1, "Amaurov", "First post", false),
            PostViewModel(1, "CatLover123", "I <3 Tiddies the most", false),
            PostViewModel(1, "Bob", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec vel pretium dolor, id hendrerit massa. Proin vitae leo libero. Etiam.", false)
        )
    }

    private fun bindRepeater() {
        rvPosts = binding.rvPosts
        rvPosts.layoutManager = LinearLayoutManager(this.context)
        rvAdapter = PostsRecyclerViewAdapter(postList)
        rvPosts.adapter = rvAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}