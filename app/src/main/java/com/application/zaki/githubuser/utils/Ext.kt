package com.application.zaki.githubuser.utils

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.appcompat.widget.SearchView

fun ImageView.loadImageUrl(url: String?) {
    Glide.with(context)
        .load(url)
        .into(this)
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}


fun SearchView.getQueryTextChangeStateFlow(): StateFlow<String> {
    val searchQuery = MutableStateFlow("")
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(newText: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            searchQuery.value = newText ?: ""
            return true
        }
    })
    return searchQuery
}