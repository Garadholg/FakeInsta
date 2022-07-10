package com.amaurov.fakeinsta.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amaurov.fakeinsta.adapters.PostsRecyclerViewAdapter
import com.amaurov.fakeinsta.dao.responses.Response
import com.amaurov.fakeinsta.dao.responses.interfaces.FirebaseCallback
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
        getResponse()
        //bindRepeater()
    }

    private fun bindRepeater() {
        rvPosts = binding.rvPosts
        rvPosts.layoutManager = LinearLayoutManager(this.context)
        //rvAdapter = PostsRecyclerViewAdapter(postList)
        rvPosts.adapter = rvAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    // FIREBASE + MVVM SHENANIGANS
    // REF URL: https://medium.com/firebase-tips-tricks/how-to-read-data-from-firebase-realtime-database-using-get-269ef3e179c5
    // TODO("Delete when not necessary")

    private fun print(response: Response) {
        response.posts?.let { posts ->
            posts.forEach { post ->
                post.caption?.let {
                    Log.i("TAG", it)
                }
            }
        }

        response.exception?.let { e ->
            e.message?.let {
                Log.d("EXCEPTION", it)
            }
        }
    }

    private fun getResponse() {
        viewModel.responseLiveData.observe(viewLifecycleOwner) {
            print(it)
        }
    }
}