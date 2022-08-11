package com.yeji.bookassignment.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.yeji.bookassignment.R
import com.yeji.bookassignment.data.BookData
import com.yeji.bookassignment.databinding.ViewMainBookItemBinding
import com.yeji.bookassignment.databinding.ViewMainBookItemLoadingBinding

class BookResultAdapter(
//    var bookDataList: MutableLiveData<MutableList<BookData>>,
//    val onToggleLike: Unit
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val bookDataList = mutableListOf<BookData>()
//    var listener: OnItemClickListener? = null

    lateinit var parentView: ViewGroup

    companion object {
        private const val TYPE_ITEM = 0
        private const val TYPE_LOADING = 1
    }

    fun setBookDataList(updatedList: MutableList<BookData>) {
        this.bookDataList.clear()
        this.bookDataList.addAll(updatedList)
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        parentView = parent

        val inflater = LayoutInflater.from(parent.context)

        val itemView = inflater.inflate(R.layout.view_main_book_item, parent, false)
        val loadingView = inflater.inflate(R.layout.view_main_book_item_loading, parent, false)

        return when (viewType) {
            TYPE_ITEM -> BookResultViewHolder(itemView)
            else -> LoadingViewHolder(loadingView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       if (holder is BookResultViewHolder) {
           showItemRows(holder as BookResultViewHolder, position)
       }
       else {
           showLoadingView(holder as LoadingViewHolder, position)
       }
    }

    fun showLoadingView(holder: LoadingViewHolder, position: Int) {
        // displayed progressbar
    }

    fun showItemRows(holder: BookResultViewHolder, position: Int) {
        val bookData = bookDataList[position]

        holder.bind(bookData, position)
        holder.itemView.setOnClickListener {
//            listener?.onItemClick(it, position)
            this.listener.onItemClick(view = it, pos = position, viewType = "itemView")
        }
    }


    override fun getItemCount(): Int {
        return bookDataList.size
    }

//    fun setOnClickListener(listener: OnItemClickListener) {
//        this.listener = listener
//    }

    interface OnItemClickListener {
        fun onItemClick(view: View, pos: Int, viewType: String)
    }

    override fun getItemViewType(position: Int): Int {
        return if (bookDataList.get(position).title == "") TYPE_LOADING
        else TYPE_ITEM
    }

    fun deleteLoading() {
        bookDataList.removeLast()
    }

    inner class BookResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ViewMainBookItemBinding.bind(itemView)

        fun bind(bookData: BookData, position: Int) {
            Glide.with(itemView.context)
                .load(bookData.thumbnail)
                .placeholder(R.drawable.ic_image_24)
                .fallback(R.drawable.ic_image_24)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(binding.ivMainBook)

            binding.tvMainBookTitle.text = bookData.title
            binding.tvMainBookPublishedDate.text = bookData.datetime
            binding.tvMainBookDescription.text = bookData.contents
            binding.tvMainBookPrice.text = bookData.price.toString()
            // TODO: 이미지 할당
//            binding.ibMainLike.

            binding.ibMainLike.setOnClickListener {
                listener.onItemClick(view = it, pos = position, viewType = "imageButton")
            }
        }
    }

    inner class LoadingViewHolder(loadingView: View) : RecyclerView.ViewHolder(loadingView) {
        private val binding = ViewMainBookItemLoadingBinding.bind(loadingView)

        fun bind(bookData: BookData, position: Int) {
        }
    }

}
