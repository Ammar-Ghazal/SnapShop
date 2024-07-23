package com.example.snapshop.ui.wishlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.snapshop.databinding.FragmentWishlistBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.snapshop.R

class WishListFragment : Fragment() {

    private var _binding: FragmentWishlistBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var adapter: WishListAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup RecyclerView
        val wishListViewModel = ViewModelProvider(requireActivity()).get(WishListViewModel::class.java)
        val wishlist = wishListViewModel.wishlistItems.value
        Log.i("com.example.snapshop.ui.wishlist", wishlist.toString())
        adapter = WishListAdapter(emptyList()) { item ->
            wishListViewModel.removeFromWishlist(item)
        }


        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewWishlist)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        Log.i("com.example.snapshop.ui.wishlist", adapter.toString())
        // Observe the wishlist items from the ViewModel
        wishListViewModel.wishlistItems.observe(viewLifecycleOwner, Observer { items ->
            Log.i("com.example.snapshop.ui.wishlist", items.toString())
            adapter.updateItems(items)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
