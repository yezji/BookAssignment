package com.yeji.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.yeji.domain.model.BookData
import com.yeji.presentation.R
import com.yeji.presentation.databinding.FragmentSearchDetailBinding
import com.yeji.presentation.util.LocalizeCurrency
import com.yeji.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchDetailFragment : Fragment() {
    private val TAG = SearchDetailFragment::class.java.simpleName

    private var _binding: FragmentSearchDetailBinding? = null
    private val binding: FragmentSearchDetailBinding get() = requireNotNull(_binding)

    private val viewModel: MainViewModel by activityViewModels()

    private val navArgs by navArgs<SearchDetailFragmentArgs>()
    private var position: Int = 0
    private var bookData: BookData = BookData(
        title = null,
        contents = null,
        url = null,
        isbn = null,
        datetime = null,
        authors = null,
        publisher = null,
        translators = null,
        price = null,
        sale_price = null,
        thumbnail = null,
        status = null,
        like = false
    )

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

        /*// receive bundle data
        setFragmentResultListener(TAG) { requestKey: String, bundle: Bundle ->
            position = bundle.getInt("itemPosition")
            viewModel.uiState.value.bookList.let {
                bookData = it.get(position)!!
            }
            initUI(position)
        }*/

        // receive bundle data from navArgs
        position = navArgs.itemPosition
        viewModel.uiState.value.bookList.let {
            bookData = it[position]!!
        }
        initUI(position)

    }

    private fun initUI(position: Int) {
        Glide.with(binding.ivDetailBook.context)
            .load(bookData.thumbnail)
            .placeholder(R.drawable.ic_image_24)
            .fallback(R.drawable.ic_image_24)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(binding.ivDetailBook)

        binding.tvDetailBookTitle.text = bookData.title
        binding.tvDetailPublishedDate.text = bookData.datetime?.substring(0, 10) ?: "0000-00-00"
        binding.tvDetailBookPublisher.text = bookData.publisher
        binding.tvDetailBookDescription.text = bookData.contents
        binding.tvDetailBookPrice.text = LocalizeCurrency.getCurrency(bookData.price as Double)


        binding.toolbarSearchDetail.ibNormalBack.setOnClickListener {
//            parentFragmentManager.popBackStack()
            findNavController().navigateUp()
        }
        binding.toolbarSearchDetail.ibNormalLike.setOnClickListener {
            // toggle like status
            var status = viewModel.uiState.value.bookList[position]?.like
            status = !status!!
            bookData.like = status

            setLikeResource(status)

            viewModel.uiState.value.bookList.let {
                val mutableList = it.toMutableList()
                mutableList[position] = bookData
                viewModel.setBookList(mutableList.toList())
            }

            Log.d("yezzz", "pos: $position, like: ${viewModel.uiState.value.bookList?.get(position)?.like}")
        }
    }

    private fun setLikeResource(liked: Boolean) {
        var resId: Int = R.drawable.ic_favorite_empty_24
        if (liked) resId = R.drawable.ic_favorite_filled_24
        binding.toolbarSearchDetail.ibNormalLike.setImageResource(resId)
    }
}