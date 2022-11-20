package com.amaurov.fakeinsta

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.amaurov.fakeinsta.utils.adapters.StringImageAdapter
import io.mockk.*
import junit.framework.Assert.assertEquals
import org.junit.Test

class StringImageAdapterTest {

    @Test
    fun `testing the conversion from base64 to Bitmap`() {
        val sut = spyk(StringImageAdapter("abcde"))
        val bitmap = mockk<Bitmap>()
        mockkStatic(Base64::class)
        mockkStatic(BitmapFactory::class)
        mockkStatic(Bitmap::class)

        every { Base64.decode(any<String>(), Base64.DEFAULT) } returns ByteArray(0)
        every { Bitmap.createBitmap(any(), any(), any()) } returns bitmap
        every { BitmapFactory.decodeByteArray(any(), 0, any()) } returns Bitmap.createBitmap(0, 0, Bitmap.Config.ALPHA_8)

        val result = sut.showImage()

        verify { sut.showImage() }

        assertEquals(result::class, Bitmap::class)
    }
}