package com.example.snapshop.ui.wishlist

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.snapshop.models.WishListItem
import com.example.snapshop.models.WishListItemGson
import com.example.snapshop.util.SharedPreferencesManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WishListViewModel : ViewModel() {

    private val _wishlistItems = MutableLiveData<List<WishListItem>>()
    val wishlistItems: LiveData<List<WishListItem>> get() = _wishlistItems

    init {
        // Initialize with some dummy data
        _wishlistItems.value = emptyList()
        loadWishListItems()
    }

    fun addToWishList(item: WishListItem) {
        val currentList = _wishlistItems.value.orEmpty()
        _wishlistItems.value = currentList + item

        Log.i("com.example.snapshop", _wishlistItems.value.toString())
        saveWishListItems(currentList+item)
    }

    fun removeFromWishlist(item: WishListItem) {
        val currentList = _wishlistItems.value.orEmpty().toMutableList()
        currentList.remove(item)
        saveWishListItems(currentList)
        _wishlistItems.value = currentList
    }

    fun saveWishListItems(items: List<WishListItem>) {
        viewModelScope.launch {
            val jsonString = toJsonString(items)
            Log.i("com.example.snapshop.models", jsonString)
            SharedPreferencesManager.saveString("wishlist_items", jsonString)
        }
    }


    fun loadWishListItems() {
        val jsonString = SharedPreferencesManager.getString("wishlist_items", "[]")
        val items = fromJsonString(jsonString)
        val newItems : List<WishListItem> = items.map { item ->
            WishListItem(
                item.id,
                item.title,
                WishListItemGson.stringToBitmap(item.image),
                item.description,
                item.buyLinks
            )
        }
        Log.i("com.example.snapshop.models", newItems.size.toString())
        Log.i("com.example.snapshop.models", newItems.toString())
        _wishlistItems.value = newItems
    }

    // Convert List<WishListItem> to JSON string
    private fun toJsonString(items: List<WishListItem>): String {
        val newItems : List<WishListItemGson> = items.map { item ->
            WishListItemGson(
                item.id,
                item.title,
                WishListItemGson.bitmapToString(item.image),
                item.description,
                item.buyLinks
            )
        }
        return Gson().toJson(newItems)
    }

    // Convert JSON string to List<WishListItem>
    private fun fromJsonString(jsonString: String?): List<WishListItemGson> {
        val listType = object : TypeToken<List<WishListItemGson>>() {}.type
        return Gson().fromJson(jsonString, listType)
    }
}