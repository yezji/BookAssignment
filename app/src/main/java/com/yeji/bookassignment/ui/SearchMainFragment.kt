package com.yeji.bookassignment.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yeji.bookassignment.databinding.FragmentSearchMainBinding
import com.yeji.bookassignment.viewmodel.MainViewModel
import kotlinx.coroutines.*

class SearchMainFragment : Fragment(), BookResultAdapter.OnItemClickListener {
    companion object {
        private val TAG = SearchMainFragment::class.java.simpleName
    }
    private var _binding: FragmentSearchMainBinding? = null
    private val binding: FragmentSearchMainBinding get() = requireNotNull(_binding)
    // TODO: viewModel
//    private val viewModel: MainViewModel by activityViewModels()
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var adapter: BookResultAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchMainBinding.inflate(inflater, container, false)

//        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set adapter
        setAdapter()
        initScrollListener()


        viewModel.bookList.observe(requireActivity()) { booklist ->
            adapter.setBookDataList(booklist.toMutableList())
//            Log.d("yezzz", booklist.size.toString())
        }
    }

    fun setAdapter() {
        layoutManager = LinearLayoutManager(context)
        binding.rvResultMain.layoutManager = layoutManager
//        adapter = BookResultAdapter(viewModel.bookList, viewModel.toggleLike(0))
        adapter = BookResultAdapter(this)
        binding.rvResultMain.adapter = adapter

        viewModel.getAllList()

//        adapter.setOnClickListener(object : BookResultAdapter.OnItemClickListener {
//            override fun onItemClick(view: View, pos: Int?) {
//                // TODO: click 이벤트 처리
//            }
//        })
    }

    fun initScrollListener() {
        binding.rvResultMain.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                val itemTotalPosition = binding.rvResultMain.adapter!!.itemCount - 1

                if (viewModel.isProgressVisible.value == false) {
                    // 스크롤이 최하단에 도달하고, 리스트의 마지막이라면
                    if (!binding.rvResultMain.canScrollVertically(1) &&
                            lastVisibleItemPosition == itemTotalPosition) {
                        if (viewModel.isEnd.value == true && viewModel.isProgressVisible.value == true) {
                            viewModel.page.value!!.plus(1)
                            loadMore()
                        }
                    }
                }
            }
        })
    }

    // TODO: 더 가져오기
    fun loadMore() {
        // progress bar visible

        // get more items
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Default) {
                // progress bar visible
                adapter.setBookDataList(mutableListOf())
                adapter.notifyItemInserted(adapter.bookDataList.size - 1)

                val keyword = viewModel.keyword.value
                viewModel.keyword.postValue("")
                viewModel.keyword.postValue(keyword)
            }
        }
    }

    override fun onItemClick(view: View, pos: Int, viewType: String) {
        // toggle
        if (viewType == "imageButton") {
            val before = viewModel.bookList.value!![pos].like
            var after: Boolean? = null
            after = before != true
            viewModel.bookList.value!![pos].like = after
        }
        // open detail
        else if (viewType == "itemView") {
            // TODO: fragment 쌓기
//            parentFragment
        }
    }
}