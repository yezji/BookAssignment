package com.yeji.bookassignment.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.yeji.bookassignment.R
import com.yeji.bookassignment.data.BookData
import com.yeji.bookassignment.databinding.FragmentSearchDetailBinding
import com.yeji.bookassignment.util.LocalizeCurrency
import com.yeji.bookassignment.viewmodel.MainViewModel

class SearchDetailFragment : Fragment() {
    private val TAG = SearchDetailFragment::class.java.simpleName

    private var _binding: FragmentSearchDetailBinding? = null
    private val binding: FragmentSearchDetailBinding get() = requireNotNull(_binding)

    private val viewModel: MainViewModel by activityViewModels()
    private var position: Int = 0
    private var bookData: BookData = BookData()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener(TAG) { requestKey: String, bundle: Bundle ->
            position = bundle.getInt("itemPosition")
            bookData = viewModel.bookList.value?.get(position)!!
            initUI(position)
        }
    }

    private fun initUI(position: Int) {
        Glide.with(binding.ivDetailBook.context)
            .load(bookData.thumbnail)
            .placeholder(R.drawable.ic_image_24)
            .fallback(R.drawable.ic_image_24)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(binding.ivDetailBook)

        binding.tvDetailBookTitle.text = bookData.title
        binding.tvDetailPublishedDate.text = bookData.datetime.substring(0, 10)
        binding.tvDetailBookPublisher.text = bookData.publisher
        binding.tvDetailBookDescription.text = bookData.contents
        binding.tvDetailBookPrice.text = LocalizeCurrency.getCurrency(bookData.price.toDouble())
        setLikeResource()

        binding.ibNormalLike.setOnClickListener {
            var status = viewModel.bookList.value?.get(position)?.like
            status = !status!!
            bookData.like = status
            val list = viewModel.bookList.value
            if (list!!.isNotEmpty()) {
                list[position] = bookData
                viewModel._bookList.value = list
                setLikeResource()

                Log.d("yezzz", "pos: $position, like: ${viewModel.bookList.value!!.get(position)!!.like}")
            }
        }
        binding.ibNormalBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setLikeResource() {
        if (viewModel.bookList.value?.get(position)?.like == true) binding.ibNormalLike.setImageResource(R.drawable.ic_favorite_filled_24)
        else binding.ibNormalLike.setImageResource(R.drawable.ic_favorite_empty_24)
    }
}