package com.amaurov.fakeinsta.utils.strategies

import androidx.fragment.app.FragmentActivity
import com.amaurov.fakeinsta.utils.annotations.Strategy
import com.google.android.gms.tasks.Task

class LoginContext(activity: FragmentActivity) {
    private var strategy: LoginStrategy = EmailLoginStrategy(activity)

    fun changeStrategy(strategy: LoginStrategy) = apply {
        if ((strategy::class.java).isAnnotationPresent(Strategy::class.java)) {
            this.strategy = strategy
        } else {
            throw Exception("Only instances of classes annotated with @Strategy can be passed")
        }
    }

    fun login(username: String? = null, password: String? = null): Task<*> {
        return strategy.login(username, password)
    }
}