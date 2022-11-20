package com.amaurov.fakeinsta

import android.content.res.Resources
import android.util.Log
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.amaurov.fakeinsta.dao.models.UserData
import com.amaurov.fakeinsta.fragments.*
import com.amaurov.fakeinsta.utils.Auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.junit.Rule
import org.junit.Test

class UiTest {

    @Test
    fun addPostFragmentAccessPermittedWhenUserIsSignedIn() {
        val scenario = launchFragmentInContainer<HomeFragment>()
        scenario.moveToState(Lifecycle.State.RESUMED)
        onView(withId(R.id.rvPosts)).check(matches(isDisplayed()))
    }

    @Test
    fun addPostFragmentAccessDeniedWhenUserIsNotSignedIn() {
        //Making sure that the user is empty
        Auth.currentUser = null

        val scenario = launchFragmentInContainer<AddPostFragment>()
        scenario.moveToState(Lifecycle.State.RESUMED)

        onView(withId(R.id.llNoUserLogged)).check(matches(isDisplayed()))
    }

}