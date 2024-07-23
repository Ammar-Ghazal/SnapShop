package com.example.snapshop.ui.wishlist

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.snapshop.R
import com.example.snapshop.models.WishListItem
import java.util.UUID

class WishListViewModel : ViewModel() {

    private val _wishlistItems = MutableLiveData<List<WishListItem>>()
    val wishlistItems: LiveData<List<WishListItem>> get() = _wishlistItems

    init {
        // Initialize with some dummy data
        _wishlistItems.value = emptyList()
    }

    fun addToWishList(item: WishListItem) {
        val currentList = _wishlistItems.value.orEmpty()
        _wishlistItems.value = currentList + item

        Log.i("com.example.snapshop", _wishlistItems.value.toString())
    }

    fun removeFromWishlist(item: WishListItem) {
        val currentList = _wishlistItems.value.orEmpty().toMutableList()
        currentList.remove(item)
        _wishlistItems.value = currentList
    }
}