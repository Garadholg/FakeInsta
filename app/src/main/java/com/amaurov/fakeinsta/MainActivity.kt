package com.amaurov.fakeinsta

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.amaurov.fakeinsta.dao.models.UserData
import com.amaurov.fakeinsta.dao.responses.FirebaseResponse
import com.amaurov.fakeinsta.utils.Auth
import com.amaurov.fakeinsta.utils.GenericCallback
import com.amaurov.fakeinsta.viewmodels.UserDataViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    var userDataVM = UserDataViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        checkLoggedUser()
        super.onResume()
    }

    private fun checkLoggedUser() {
        var auth = Firebase.auth

        if (auth.currentUser != null && Auth.currentUser == null) {
            userDataVM.getUserById(auth.currentUser!!.uid, object: GenericCallback<UserData> {
                override fun onCallback(response: FirebaseResponse<UserData>) {
                    Auth.currentUser = response.data?.get(0)
                }
            })
        }
    }
}