package com.yeji.bookassignment.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yeji.bookassignment.R
import com.yeji.bookassignment.databinding.FragmentSearchDetailBinding

class SearchDetailFragment : Fragment() {
    companion object {
        private val TAG = SearchDetailFragment::class.java.simpleName
    }
    private var _binding: FragmentSearchDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_detail, container, false)
    }
}