package com.example.snapshop.ui.recommendations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecommendationsViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "Recommendations"
    }
    val text: LiveData<String> = _text

    fun setRecommendations(text: String) {
        _text.value = text
    }
}