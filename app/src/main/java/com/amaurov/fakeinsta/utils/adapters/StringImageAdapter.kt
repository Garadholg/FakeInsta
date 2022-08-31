package com.amaurov.fakeinsta.utils.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

class StringImageAdapter(private val adapted: String): ImageString {
    override fun showImage(): Bitmap {
        val imageBytes = Base64.decode(adapted, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}