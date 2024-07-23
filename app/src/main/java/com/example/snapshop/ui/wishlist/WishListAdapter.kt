package com.example.snapshop.ui.wishlist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.snapshop.R
import com.example.snapshop.models.WishListItem

class WishListAdapter(private var wishlistItems: List<WishListItem>, private val onDeleteClick: (WishListItem) -> Unit) : RecyclerView.Adapter<WishListAdapter.WishListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_wishlist, parent, false)
        return WishListViewHolder(view)
    }

    override fun onBindViewHolder(holder: WishListViewHolder, position: Int) {
        val item = wishlistItems[position]
        holder.itemImage.setImageBitmap(item.image) // Assuming image is of type Bitmap
        holder.itemName.text = item.title
        holder.btnDelete.setOnClickListener {
            onDeleteClick(item)
        }

        Log.i("com.example.snapshop.ui.wishlist", item.title + " ")
    }

    override fun getItemCount(): Int = wishlistItems.size

    fun updateItems(newItems: List<WishListItem>) {
        Log.i("com.example.snapshop.ui.wishlist", newItems.toString())

        wishlistItems = newItems
        notifyDataSetChanged()
    }

    class WishListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage: ImageView = view.findViewById(R.id.ivItemImage)
        val itemName: TextView = view.findViewById(R.id.tvItemName)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }
}

