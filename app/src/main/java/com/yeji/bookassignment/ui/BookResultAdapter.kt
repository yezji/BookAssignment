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
            else -> BookResultViewHolder(itemView, itemClick)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val bookData = getItem(position)
        if (holder is BookResultViewHolder) {
            (holder as BookResultViewHolder).bind(bookData, position)
        }
        else if (holder is LoadingViewHolder) {
            (holder as LoadingViewHolder)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) == null) TYPE_LOADING
        else position // item id 재사용 때문에 사용
//        else TYPE_ITEM
    }

    fun addLoading() : Int {
        val list = currentList.toMutableList()
        list.add(null)
        /**
         * comments
         * TODO: submitList view쪽에서 하기
         * - submitList()는 view쪽에서 해주는 것이 좋다.
         */
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


    /**
     * comments
     * - inner class보단 class로 빼기
     *   - itemClick listener도 클래스 인자로 넘겨주는 방식으로 사용
     *   - init block으로 넣어주어도 좋다.
     * - kotlin에서 nullable type 사용 시 .let을 사용하는 것이 권장되는 방법
     *   - let, run, apply, also, with 차이 제대로 알고 사용하기
     */
    class BookResultViewHolder(val binding: ViewMainBookItemBinding, val itemClick: (BookData, Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        private var bookData: BookData? = null
        init {
            binding.root.setOnClickListener { it ->
                bookData?.let{ data ->
                    itemClick(data, absoluteAdapterPosition)
                }
            }
        }
        fun bind(bookData: BookData?, position: Int) {
            this.bookData = bookData
            binding.executePendingBindings() // 데이터가 수정되면 즉각 바인딩

            Glide.with(itemView.context)
                .load(bookData?.thumbnail)
                .placeholder(R.drawable.ic_image_24)
                .fallback(R.drawable.ic_image_24)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(binding.ivMainBook)

            binding.tvMainBookTitle.text = bookData?.title ?: "null"
            binding.tvMainBookPublishedDate.text = bookData?.datetime?.substring(0, 10) ?: "null"
            binding.tvMainBookDescription.text = bookData?.contents ?: "null"
            binding.tvMainBookPrice.text = bookData?.price?.let { LocalizeCurrency.getCurrency(it) }


            if (bookData?.like == true) binding.ibMainLike.setImageResource(R.drawable.ic_favorite_filled_24)
            else binding.ibMainLike.setImageResource(R.drawable.ic_favorite_empty_24)
            binding.ibMainLike.setOnClickListener {
                bookData?.let { data ->
                    itemClick(data, position) // 함수에서 position 따로 안넘겨주고 absoluteAdapterPosition으로 사용해도 된다.
                }
            }

        }
    }

    class LoadingViewHolder(binding: ViewMainBookItemLoadingBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind() {
//            // do nothing - displayed progressbar
//        }
    }
}
