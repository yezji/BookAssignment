package com.yeji.presentation.ui

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yeji.domain.model.BookData

object RecyclerViewBindingAdapter {
    @BindingAdapter("items")
    @JvmStatic
    fun setItems(recyclerView: RecyclerView, items: MutableList<BookData>?) {
        val adapter = recyclerView.adapter as BookResultAdapter
        adapter.submitList(items)
    }
}