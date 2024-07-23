package com.example.snapshop.ui.recommendations

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.snapshop.databinding.FragmentRecommendationsBinding
import com.example.snapshop.ui.wishlist.WishListViewModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class RecommendationsFragment : Fragment() {

    private var _binding: FragmentRecommendationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val apiKey = "AIzaSyC_zYTrGIQMYnW3zaUYXr8h5O7YJreaQtw"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val wishListViewModel = ViewModelProvider(requireActivity()).get(WishListViewModel::class.java)
        val wishlist = wishListViewModel.wishlistItems.value.orEmpty().toMutableList()
        if (wishlist.size > 0) {
            Log.i("com.example.snapshop", wishlist.toString())
            var index = 0
            if (wishlist.size > 1) {
                index = Random.nextInt(0, wishlist.size - 1)
            }
            recommendation("Give me a numbered list of products that are similar to " + wishlist[index].title)
        } else {
            val viewModel: RecommendationsViewModel by viewModels()
            viewModel.setRecommendations("Nothing yet")
        }

        val recommendationsViewModel =
            ViewModelProvider(this).get(RecommendationsViewModel::class.java)

        _binding = FragmentRecommendationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textRecommendations
        recommendationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun recommendation(prompt: String) {
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = apiKey
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val inputContent = content {
                    text(prompt)
                }
                val response = generativeModel.generateContent(inputContent)

                withContext(Dispatchers.Main) {
                    Log.i("com.example.snapshop", response.text.toString())
                    val viewModel: RecommendationsViewModel by viewModels()
                    viewModel.setRecommendations(response.text.toString())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}