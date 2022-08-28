package com.amaurov.fakeinsta.dao.repositories

import com.amaurov.fakeinsta.dao.models.Post
import com.amaurov.fakeinsta.dao.models.UserData
import com.amaurov.fakeinsta.dao.repositories.implementations.PostsRepositoryImpl
import com.amaurov.fakeinsta.dao.repositories.implementations.UserDataRepositoryImpl

class RepositoryFactory {
    companion object {
        inline fun <reified E> getRepository(): Repository<E>? {
            return when (E::class) {
                Post::class -> PostsRepositoryImpl() as Repository<E>
                UserData::class -> UserDataRepositoryImpl() as Repository<E>
                else -> null
            }
        }
    }
}