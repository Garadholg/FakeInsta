package com.amaurov.fakeinsta.dao.repositories

import com.amaurov.fakeinsta.dao.models.Post
import com.amaurov.fakeinsta.dao.models.UserData
import com.amaurov.fakeinsta.dao.repositories.implementations.PostsRepositoryImpl
import com.amaurov.fakeinsta.dao.repositories.implementations.UserDataRepositoryImpl
import com.amaurov.fakeinsta.utils.annotations.FirebaseEntity

class RepositoryFactory {
    companion object {
        inline fun <reified E> getRepository(): Repository<E>? {
            if ((E::class.java).isAnnotationPresent(FirebaseEntity::class.java)) {
                return when (E::class) {
                    Post::class -> PostsRepositoryImpl() as Repository<E>
                    UserData::class -> UserDataRepositoryImpl() as Repository<E>
                    else -> null
                }
            } else {
                throw Exception("Cannot create a repository for the given class")
            }
        }
    }
}