package com.amaurov.fakeinsta.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.amaurov.fakeinsta.MainActivity
import com.amaurov.fakeinsta.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomNavigation(view)
    }

    private fun setupBottomNavigation(view : View) {
            val navController: NavController =
                findNavController(activity as MainActivity, R.id.bottomNavFragmentPlaceholder)
            val bottomNavigationView: BottomNavigationView =
                view.findViewById(R.id.bottomNavView)
            NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }
}