package com.example.snapshop.models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.ByteArrayOutputStream

data class WishListItemGson(
    @SerializedName("id") @Expose val id: String,
    @SerializedName("title") @Expose val title: String?,
    @SerializedName("image") @Expose val image: String,
    @SerializedName("description") @Expose val description: String?,
    @SerializedName("buyLinks") @Expose val buyLinks: MutableList<String>
) {
    companion object {
        // Convert Bitmap to String for Gson serialization
        fun bitmapToString(bitmap: Bitmap): String {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            return Base64.encodeToString(byteArray, Base64.DEFAULT)
        }

        // Convert String to Bitmap for Gson deserialization
        fun stringToBitmap(encodedString: String): Bitmap {
            val decodedBytes = Base64.decode(encodedString, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        }
    }
}
