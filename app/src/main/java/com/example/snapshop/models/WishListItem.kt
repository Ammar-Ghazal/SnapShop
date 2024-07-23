package com.example.snapshop.models

import android.graphics.Bitmap


data class WishListItem(
     val id: String,
     val title: String?,
     val image: Bitmap,
     val description: String?,
     val buyLinks: MutableList<String>
)