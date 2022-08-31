package com.amaurov.fakeinsta.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Base64
import java.io.ByteArrayOutputStream

//fun String.toBitmapImage(): Bitmap {
//    val imageBytes = Base64.decode(this, Base64.DEFAULT)
//    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
//}

fun Drawable.toBase64String(): String {
    val pictureBitmap = (this as BitmapDrawable).bitmap
    val baos = ByteArrayOutputStream().apply {
        pictureBitmap.compress(Bitmap.CompressFormat.PNG, 100, this)
    }
    return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
}