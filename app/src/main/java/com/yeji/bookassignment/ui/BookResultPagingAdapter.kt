package com.yeji.bookassignment.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.yeji.bookassignment.R
import com.yeji.bookassignment.data.BookData
import com.yeji.bookassignment.databinding.ViewMainBookItemBinding
import com.yeji.bookassignment.util.LocalizeCurrency

class BookResultPagingAdapter(
    val itemClick: (BookData, Int) -> Unit
) : PagingDataAdapter<BookData, BookResultPagingAdapter.BookViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<BookData>() {
            override fun areItemsTheSame(oldItem: BookData, newItem: BookData): Boolean {
                // 각 item들의 고유한 값을 비교
                return oldItem.title == newItem.title && oldItem.like == newItem.like
            }

            override fun areContentsTheSame(oldItem: BookData, newItem: BookData): Boolean {
                // item의 세부내용도 같은지 비교하여 다르다면 item을 갱신(notify)
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val itemView = ViewMainBookItemBinding.inflate(inflater, parent, false)

        return BookViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position)
    }

    inner class BookViewHolder(val binding: ViewMainBookItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bookData: BookData?, position: Int) {

            binding.root.setOnClickListener {
                if (bookData != null) {
                    itemClick(bookData, position)
                }
            }

            Glide.with(itemView.context)
                .load(bookData?.thumbnail)
                .placeholder(R.drawable.ic_image_24)
                .fallback(R.drawable.ic_image_24)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(binding.ivMainBook)

            binding.tvMainBookTitle.text = bookData?.title ?: "null"
            binding.tvMainBookPublishedDate.text = bookData?.datetime?.substring(0, 10) ?: "null"
            binding.tvMainBookDescription.text = bookData?.contents ?: "null"
            binding.tvMainBookPrice.text = bookData?.price?.let { LocalizeCurrency.getCurrency(it.toDouble()) }


            if (bookData?.like == true) binding.ibMainLike.setImageResource(R.drawable.ic_favorite_filled_24)
            else binding.ibMainLike.setImageResource(R.drawable.ic_favorite_empty_24)

            binding.executePendingBindings() // 데이터가 수정되면 즉각 바인딩
        }
    }
}