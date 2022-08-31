package com.amaurov.fakeinsta.utils.strategies

import androidx.fragment.app.FragmentActivity
import com.google.android.gms.tasks.Task

class LoginContext(activity: FragmentActivity) {
    private var strategy: LoginStrategy = EmailLoginStrategy(activity)

    fun changeStrategy(strategy: LoginStrategy) = apply { this.strategy = strategy }

    fun login(username: String? = null, password: String? = null): Task<*> {
        return strategy.login(username, password)
    }
}