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
        private const val TYPE_ITEM = 0
        private const val TYPE_LOADING = 1

        val diffUtil = object : DiffUtil.ItemCallback<BookData>() {
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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val itemView = ViewMainBookItemBinding.inflate(inflater, parent, false)
        val loadingView = ViewMainBookItemLoadingBinding.inflate(inflater, parent, false)

        return BookResultViewHolder(itemView)
        // TODO: view type 분리해서 로딩 아이템 보이기
//        return when (viewType) {
//            TYPE_ITEM -> BookResultViewHolder(itemView)
//            else -> LoadingViewHolder(loadingView)
//        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val bookData = getItem(position)
//        Log.d("yezzz adapter", "bookdata: $bookData")
        if (holder is BookResultViewHolder) {
            (holder as BookResultViewHolder).bind(bookData, position)
       }
       else if (holder is LoadingViewHolder) {
            (holder as LoadingViewHolder).bind()
       }
    }

    override fun getItemViewType(position: Int): Int {
        return position
//        return if (getItem(position) == null) TYPE_LOADING
//        else TYPE_ITEM
    }

    fun addLoading() {
        val list = currentList.toMutableList()
        list.add(null)
        submitList(list)
    }

    fun deleteLoading() {
        val list = currentList.toMutableList()
        if (list.isNotEmpty()) {
            list.removeLast()
            submitList(list)
        }
    }


    inner class BookResultViewHolder(val binding: ViewMainBookItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bookData: BookData?, position: Int) {

            binding.root.setOnClickListener {
                itemClick(bookData!!, position)
            }

            Glide.with(itemView.context)
                .load(bookData!!.thumbnail)
                .placeholder(R.drawable.ic_image_24)
                .fallback(R.drawable.ic_image_24)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(binding.ivMainBook)

            binding.tvMainBookTitle.text = bookData.title
            binding.tvMainBookPublishedDate.text = bookData.datetime.substring(0, 10)
            binding.tvMainBookDescription.text = bookData.contents
            binding.tvMainBookPrice.text = LocalizeCurrency.getCurrency(bookData.price.toDouble())


            if (bookData.like) binding.ibMainLike.setImageResource(R.drawable.ic_favorite_filled_24)
            else binding.ibMainLike.setImageResource(R.drawable.ic_favorite_empty_24)

            binding.executePendingBindings() // 데이터가 수정되면 즉각 바인딩
//            Log.d("yezzz", "title: ${bookData.title}")
        }
    }

    inner class LoadingViewHolder(val binding: ViewMainBookItemLoadingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            // displayed progressbar
        }
    }
}
