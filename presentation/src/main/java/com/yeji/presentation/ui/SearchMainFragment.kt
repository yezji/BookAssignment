package com.yeji.presentation.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yeji.domain.model.BookData
import com.yeji.presentation.R
import com.yeji.presentation.databinding.FragmentSearchMainBinding
import com.yeji.presentation.viewmodel.MainViewModel
import com.yeji.presentation.viewmodel.SearchMainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class SearchMainFragment : Fragment() {
    private val TAG = SearchMainFragment::class.java.simpleName

    private var _binding: FragmentSearchMainBinding? = null
    private val binding: FragmentSearchMainBinding get() = requireNotNull(_binding)

    private val viewModel: SearchMainViewModel by viewModels<SearchMainViewModel>()
    private lateinit var adapter: BookResultAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private lateinit var searchDetailFragment: SearchDetailFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchMainBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // prepare fragment instance
        searchDetailFragment = SearchDetailFragment()
//        viewModel.fragmentType.value = FragmentEnum.SearchMain

        // set adapter
        setAdapter()
        initScrollListener()

        initUI()
/*
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {

                        // request search api
                        ApiRepository.getSearchBookListFlow(
                            query="??????",
                            sort="accuracy",
                            page=1,
                            size=10,
                            target="title")
                            // runtimeexception check Default, view?????? ???????????????
                            .flowOn(Dispatchers.Unconfined).collect { flow ->
                                when (flow) {
                                    is Result.Success -> {
                                        // success case
                                    }
                                    is Result.Error -> {
                                        // failure case
                                    }
                                }
                            }
                        }
                }
            }
        }*/

    }

    fun initUI() {
        /**
         * comments
         * - fragment?????? livedata observe??? ??? requireActivity() ???????????? ????????????.
         *   fragment??? onCreateView??? onViewCreated??? ?????? ????????? ???????????? activity??? ??????????????? ??????????????? IllegalStateException ????????? ??? ?????? ?????? (???????????? ??????)
             -> viewLifecycleOwner ????????????!
         */
        lifecycleScope.launch {
            // repeatOnLifecycle??? ?????? start, stop ?????? ???????????? ????????? ???????????? ????????????.
            /*viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.bookList.collect { bookList -> adapter.submitList(bookList) }
                }
                launch {
                    viewModel.keyword
                        // ????????? ?????? ??? 2??? ?????? api ???????????? (?????? ?????? ?????? ??????)
                        .debounce(2000)
                        .filter { query -> query.isNotBlank() }
                        .onEach { viewModel.getAllList() }
                        .launchIn(this)
                }
            }*/

            // uiStateFlow ????????? ????????? ???????????? repeatOnLifecycle ?????? flowWithLifecycle??? ??????
            viewModel.uiState
                .flowWithLifecycle(lifecycle = lifecycle, Lifecycle.State.STARTED)
                .collect { uiState ->
                    uiState.bookList?.let { submitBookList(it) }
                    // uiState.keyword?.let { setKeyword(it) }
                }
        }

        /*viewModel.bookList.observe(viewLifecycleOwner) { bookList ->
//            bookList.apply { adapter.submitList(bookList) }

            //binding.rvResultMain.adapter = adapter.apply {
            *//**
             * comments
             * - submitList????????? List ??????????????? ?????????. MutableList??? ?????????????????? ?????? ?????????.
                 ????????? immutable ???????????? ????????? ??????????????? List ???????????? ??????????????? ?????? ??????.
            *//*
            adapter.submitList(bookList)
            //}
        }*/


        binding.toolbarSearchMain.svSearchMain.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    // ????????? ????????? ???????????? ??? ????????? ??????
                    query?.let { viewModel.setKeyword(query, true) }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    // ???????????? ?????? ????????? ?????? ????????? ??????
                    newText?.let {
                        if (it.isNotEmpty()) {
                            viewModel.setKeyword(it, false)
                            Log.d("yezzz mainActivity", "text change: $it")
                            return true
                        }
                    }
                    return false
                }
            })
    }



    private fun submitBookList(bookList: List<BookData?>) {
        // submitList to adapter
        adapter.submitList(bookList)
        Log.d("yezzz mainfragment", "call adapter submit ${bookList.size}")
    }



    private fun setAdapter() {
        layoutManager = LinearLayoutManager(context)
        binding.rvResultMain.layoutManager = layoutManager
        binding.rvResultMain.addItemDecoration(DividerItemDecoration(binding.rvResultMain.context, layoutManager.orientation))

        adapter = BookResultAdapter { bookData, position -> bookItemClick(bookData, position) }
        adapter.setHasStableIds(true)
        binding.rvResultMain.adapter = adapter

    }


    private fun initScrollListener() {
        binding.rvResultMain.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                val itemTotalPosition = (binding.rvResultMain.adapter?.itemCount ?: 1) - 1

                if (!viewModel.uiState.value.isLoading) {
                    // ???????????? ???????????? ????????????, ???????????? ??????????????????
                    if (!binding.rvResultMain.canScrollVertically(1)
                        && lastVisibleItemPosition == itemTotalPosition) {
                        // ????????? ???????????? ????????????
                        if (!viewModel.uiState.value.isEnd && !viewModel.uiState.value.isLoading) {

                            lifecycleScope.launch {
                                viewModel.setIsLoading(true)
                                viewModel.incrementPage()
                                Log.d("yezzz scroll listener", "page: ${viewModel.uiState.value.page}")

                                loadMore()
                            }

                        }
                    }
                }
            }
        })
    }

    suspend fun loadMore() {
        withContext(Dispatchers.Default) {
            // add progress bar item to last row
            submitBookList(adapter.addLoading())

            delay(1000L)

            // delete progress bar item
            submitBookList(adapter.deleteLoading())

            delay(200L)
            // load more items
            adapter.apply {
                viewModel.getMoreList()
                submitList(viewModel.uiState.value.bookList)
            }
        }
    }

    fun bookItemClick(bookData: BookData?, position: Int) {
        val action: NavDirections = SearchMainFragmentDirections.actionFragmentSearchMainToFragmentSearchDetail(itemPosition = position)
        findNavController().navigate(action)

//        val dest = SearchDetailFragment::class.java.simpleName
//        setFragmentResult(dest, bundleOf("itemPosition" to position))
//
//        parentFragmentManager.beginTransaction()
//            .add(binding.root.id, searchDetailFragment, null)
//            .addToBackStack(null)
//            .commit()
        Log.d("yezzzz", "bookItemclick: ${bookData?.title?:"null"} $position")
    }

}