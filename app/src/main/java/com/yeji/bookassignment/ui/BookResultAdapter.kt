package com.yeji.bookassignment.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.yeji.bookassignment.R
import com.yeji.bookassignment.data.BookData
import com.yeji.bookassignment.databinding.ViewMainBookItemBinding
import com.yeji.bookassignment.databinding.ViewMainBookItemLoadingBinding
import com.yeji.bookassignment.util.LocalizeCurrency

class BookResultAdapter(
    val itemClick: (BookData, Int) -> Unit
) : ListAdapter<BookData, RecyclerView.ViewHolder>(diffUtil) {

    companion object {
        private const val TYPE_LOADING = -1
        private const val TYPE_ITEM = 0

        val diffUtil = object : DiffUtil.ItemCallback<BookData>() {
            override fun areItemsTheSame(oldItem: BookData, newItem: BookData): Boolean {
                // 각 item들의 고유한 값을 비교
                return oldItem.title == newItem.title && oldItem.like == newItem.like
            }

            override fun areContentsTheSame(oldItem: BookData, newItem: BookData): Boolean {
                // item의 세부내용도 같은지 비교하여 다르다면 item을 갱신(notify)
//                return oldItem == newItem
                return oldItem == newItem
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val itemView = ViewMainBookItemBinding.inflate(inflater, parent, false)
        val loadingView = ViewMainBookItemLoadingBinding.inflate(inflater, parent, false)

        return when(viewType) {
            TYPE_LOADING -> LoadingViewHolder(loadingView)
            else -> BookResultViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val bookData = getItem(position)
        if (holder is BookResultViewHolder) {
            (holder as BookResultViewHolder).bind(bookData, position)
       }
       else if (holder is LoadingViewHolder) {
            (holder as LoadingViewHolder).bind()
       }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) == null) TYPE_LOADING
        else position
//        else TYPE_ITEM
    }

    fun addLoading() : Int {
        val list = currentList.toMutableList()
        list.add(null)
        submitList(list)

        return list.lastIndex // loading position
    }

    fun deleteLoading() : Boolean {
        val list = currentList.toMutableList()
        if (list.isNotEmpty()) {
//            list.removeAt(position)
            list.removeLast()
            submitList(list)
        }

        return true // success deletion
    }


    inner class BookResultViewHolder(val binding: ViewMainBookItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bookData: BookData?, position: Int) {
            binding.executePendingBindings() // 데이터가 수정되면 즉각 바인딩

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
            binding.ibMainLike.setOnClickListener {
                if (bookData != null) {
                    itemClick(bookData, position)
                }
            }

//            Log.d("yezzz", "title: ${bookData.title}")
        }
    }

    inner class LoadingViewHolder(val binding: ViewMainBookItemLoadingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            // displayed progressbar
        }
    }
}
