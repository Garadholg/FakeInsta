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
    private lateinit var postList: List<PostViewModel>
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
        super.onViewCreated(view, savedInstanceState)
        // VIEWMODEL SHENANIGANS
        viewModel = ViewModelProvider(this).get(PostViewModel::class.java)
        getResponse()

        initList()

        // TODO("Uncomment after MVVM testing")
        //bindRepeater()
    }

    // Currently hardcoded list for test purposes
    // TODO("Delete when actual Firebase viewmodel is done")
    private fun initList() {
//        postList = listOf(
//            PostViewModel(1, "Amaurov", "First post", listOf("#first", "#post"), false),
//            PostViewModel(1, "CatLover123", "I <3 cats the most", listOf("#cats", "#love", "#pet", "#bestpet"), false),
//            PostViewModel(1, "Bob", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec vel pretium dolor, id hendrerit massa. Proin vitae leo libero. Etiam.", listOf("#template"), false)
//        )
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

    // FIREBASE + MVVM SHENANIGANS
    // REF URL: https://medium.com/firebase-tips-tricks/how-to-read-data-from-firebase-realtime-database-using-get-269ef3e179c5
    // TODO("Delete when not necessary")
    private lateinit var viewModel: PostViewModel

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
        viewModel.getResponse(object: FirebaseCallback {
            override fun onResponse(response: Response) {
                print(response)
            }
        })
    }
}