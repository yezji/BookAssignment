package com.yeji.bookassignment.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.yeji.bookassignment.databinding.FragmentSearchDetailBinding
import com.yeji.bookassignment.viewmodel.MainViewModel

class SearchDetailFragment : Fragment() {
    companion object {
        private val TAG = SearchDetailFragment::class.java.simpleName
    }
    private var _binding: FragmentSearchDetailBinding? = null
    private val binding: FragmentSearchDetailBinding get() = requireNotNull(_binding)

    private val viewModel: MainViewModel by activityViewModels()

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

        viewModel.bookList[position].observe(viewLifecycleOwner, Observer { booklist ->
            booklist?.let {
                adapter.setBookDataList(booklist.toMutableList())
            }
        })

    }

}