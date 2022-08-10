package com.yeji.bookassignment.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.yeji.bookassignment.R
import com.yeji.bookassignment.databinding.FragmentSearchMainBinding
import com.yeji.bookassignment.viewmodel.MainViewModel

class SearchMainFragment : Fragment() {
    companion object {
        private val TAG = SearchMainFragment::class.java.simpleName
    }
    private var _binding: FragmentSearchMainBinding? = null
    private val binding: FragmentSearchMainBinding get() = requireNotNull(_binding)
    // TODO: viewModel
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchMainBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        viewModel.bookList.observe(requireActivity()) {
            // updateDataList
        }

        return binding.root
    }
}