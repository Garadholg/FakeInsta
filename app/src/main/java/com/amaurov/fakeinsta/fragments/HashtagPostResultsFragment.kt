package com.amaurov.fakeinsta.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.amaurov.fakeinsta.R
import com.amaurov.fakeinsta.databinding.FragmentHashtagPostResultsBinding
import com.amaurov.fakeinsta.databinding.FragmentHomeBinding

class HashtagPostResultsFragment : Fragment() {
    val args: HashtagPostResultsFragmentArgs by navArgs()
    private var _binding: FragmentHashtagPostResultsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHashtagPostResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.hashtag.text = args.hashtag
    }
}