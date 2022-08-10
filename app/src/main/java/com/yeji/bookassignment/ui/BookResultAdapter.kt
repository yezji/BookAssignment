package com.yeji.bookassignment.ui

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

class BookResultAdapter(
//    var bookDataList: MutableLiveData<MutableList<BookData>>,
//    val onToggleLike: Unit
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<BookResultAdapter.BookResultViewHolder>() {
    val bookDataList = mutableListOf<BookData>()
//    var listener: OnItemClickListener? = null

    fun setBookDataList(updatedList: MutableList<BookData>) {
        this.bookDataList.clear()
        this.bookDataList.addAll(updatedList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookResultViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_main_book_item, parent, false)
        return BookResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookResultViewHolder, position: Int) {
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

    inner class BookResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ViewMainBookItemBinding.bind(itemView)

        fun bind(bookData: BookData, position: Int) {
            Glide.with(itemView.context)
                .load(bookData.thumbnail)
                .placeholder(itemView.context.getDrawable(R.drawable.ic_image_24))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(binding.ivMainBook)

            binding.tvMainBookTitle.text = bookData.title
            binding.tvMainBookPublishedDate.text = bookData.datetime
            binding.tvMainBookDescription.text = bookData.contents
            binding.tvMainBookPrice.text = bookData.price.toString()

            binding.ibMainLike.setOnClickListener {
                listener.onItemClick(view = it, pos = position, viewType = "imageButton")
            }
        }
    }
}
