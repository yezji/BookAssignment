package com.yeji.bookassignment.ui

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
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yeji.bookassignment.data.BookData
import com.yeji.bookassignment.data.FragmentEnum
import com.yeji.bookassignment.databinding.FragmentSearchMainBinding
import com.yeji.bookassignment.viewmodel.MainViewModel
import kotlinx.coroutines.*

class SearchMainFragment : Fragment() {
    private val TAG = SearchMainFragment::class.java.simpleName

    private var _binding: FragmentSearchMainBinding? = null
    private val binding: FragmentSearchMainBinding get() = requireNotNull(_binding)

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var adapter: BookResultAdapter
//    private lateinit var adapter: BookResultPagingAdapter
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

    }

    fun initUI() {
        /**
         * comments
         * - fragment에서 livedata observe할 때 requireActivity() 사용하면 위험하다.
         *   fragment는 onCreateView나 onViewCreated의 생명 주기를 가지는데 activity의 생명주기를 따라간다면 IllegalStateException 발생할 수 있기 때문 (내부구현 참조)
             -> viewLifecycleOwner 사용하기!
         */
        viewModel.bookList.observe(viewLifecycleOwner) { bookList ->
//            bookList.apply {
//                Log.d("yezzz mainfragment", "call bookList submit")
//                adapter.submitList(bookList)
//            }

            //binding.rvResultMain.adapter = adapter.apply {
            Log.d("yezzz mainfragment", "call adapter submit ${bookList.size}")
            /**
             * comments
             * - submitList에서는 List 타입으로만 받는다. MutableList를 넘기려했기에 문제 생겼다.
                 그래서 immutable 스타일로 바꾸어 자연스럽게 List 타입으로 들어가도록 해야 한다.
            */
            adapter.submitList(bookList.toList())
            //}
        }

        binding.svSearchMain.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // 문자열 입력을 완료했을 때 문자열 반환
                //viewModel.setQuery()
                viewModel.keyword.value = query

                // scroll to top
//                binding.scrollviewParent.smoothScrollBy(0, 0)

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 문자열이 변할 때마다 즉각 문자열 반환
                return true
            }
        })


    }

    fun setAdapter() {
        layoutManager = LinearLayoutManager(context)
        binding.rvResultMain.layoutManager = layoutManager
        binding.rvResultMain.addItemDecoration(DividerItemDecoration(binding.rvResultMain.context, layoutManager.orientation))

        adapter = BookResultAdapter { bookData, position -> bookItemClick(bookData, position) }
        adapter.setHasStableIds(true)
        binding.rvResultMain.adapter = adapter

    }

    fun initScrollListener() {
        binding.rvResultMain.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                val itemTotalPosition = (binding.rvResultMain.adapter?.itemCount ?: 1) - 1

                if (viewModel.isLoading.value == false) {
                    // 스크롤이 최하단에 도달하고, 리스트의 마지막이라면
                    if (!binding.rvResultMain.canScrollVertically(1)
                        && lastVisibleItemPosition == itemTotalPosition) {
                        // 마지막 페이지가 아니라면
                        if (viewModel.isEnd.value == false && viewModel.isLoading.value == false) {

                            lifecycleScope.launch {
                                viewModel.isLoading.value = true
                                viewModel.page.value = viewModel.page.value?.plus(1)
                                Log.d("yezzz scroll listener", "page: ${viewModel.page.value}")

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
            adapter.addLoading()

            delay(1000L)

            // delete progress bar item
            adapter.deleteLoading()

            delay(200L)
            // load more items
            adapter.apply {
                viewModel.getMoreList()
                submitList(viewModel.bookList.value)
            }
        }
    }

    fun bookItemClick(bookData: BookData?, position: Int) {
        val dest = SearchDetailFragment::class.java.simpleName
        setFragmentResult(dest, bundleOf("itemPosition" to position))

        parentFragmentManager.beginTransaction()
            .add(binding.root.id, searchDetailFragment, FragmentEnum.SearchDetail.resString)
            .addToBackStack(null)
            .commit()
        Log.d("yezzzz", "bookItemclick: ${bookData?.title?:"null"} $position")
    }

}