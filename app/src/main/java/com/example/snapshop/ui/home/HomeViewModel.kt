package com.example.snapshop.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Click the camera icon to start"
    }
    val text: LiveData<String> = _text

    fun setHomeText(product: String, urls: MutableList<String>) {
        var str = "<div>$product</div>"
        var count = 1
        for (i in urls) {
            str += "<div>$count .<a href='$i'>$i</a></div>"
            count += 1
        }
        _text.value = str
    }
}