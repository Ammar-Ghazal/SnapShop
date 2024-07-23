package com.example.snapshop.models

import android.graphics.Bitmap
import androidx.camera.core.ImageCapture

class WishListItem(
    val id: String,
    val title: String?,
    val image: Bitmap,
    val description: String?,
    val buyLinks: MutableList<String>
)