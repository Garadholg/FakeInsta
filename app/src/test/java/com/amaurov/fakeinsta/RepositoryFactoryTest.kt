package com.amaurov.fakeinsta

import com.amaurov.fakeinsta.dao.models.Post
import com.amaurov.fakeinsta.dao.models.UserData
import com.amaurov.fakeinsta.dao.repositories.Repository
import com.amaurov.fakeinsta.dao.repositories.RepositoryFactory
import com.google.firebase.database.FirebaseDatabase
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class RepositoryFactoryTest {

    @Test
    fun `repository factory returns a repository interface for the given model`() {
        mockkStatic(FirebaseDatabase::class)
        every { FirebaseDatabase.getInstance() } returns mockk(relaxed = true)
        every { FirebaseDatabase.getInstance().reference } returns mockk(relaxed = true)
        every { FirebaseDatabase.getInstance().reference.child(any()) } returns mockk(relaxed = true)

        var result = RepositoryFactory.getRepository<Post>()

        verify {
            RepositoryFactory.getRepository<Post>()
        }

        assert(result is Repository<Post>)
    }

    @Test
    fun `repository factory throws exception when given invalid class`() {
        mockkStatic(FirebaseDatabase::class)
        every { FirebaseDatabase.getInstance() } returns mockk(relaxed = true)
        every { FirebaseDatabase.getInstance().reference } returns mockk(relaxed = true)
        every { FirebaseDatabase.getInstance().reference.child(any()) } returns mockk(relaxed = true)

        var exception = assertThrows(Exception::class.java) {
            RepositoryFactory.getRepository<String>()
        }

        assertEquals(exception.message, "Cannot create a repository for the given class")
    }
}