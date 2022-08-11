package com.yeji.bookassignment.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yeji.bookassignment.App
import com.yeji.bookassignment.data.BookData
import com.yeji.bookassignment.databinding.FragmentSearchMainBinding
import com.yeji.bookassignment.viewmodel.MainViewModel
import com.yeji.bookassignment.viewmodel.MainViewModelFactory
import kotlinx.coroutines.*

class SearchMainFragment : Fragment(), BookResultAdapter.OnItemClickListener {
    companion object {
        private val TAG = SearchMainFragment::class.java.simpleName
    }
    private var _binding: FragmentSearchMainBinding? = null
    private val binding: FragmentSearchMainBinding get() = requireNotNull(_binding)

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var adapter: BookResultAdapter
    private lateinit var layoutManager: LinearLayoutManager

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

        // set adapter
        setAdapter()
        initScrollListener()


        viewModel.bookList.observe(viewLifecycleOwner, Observer { booklist ->
            booklist?.let {
                adapter.setBookDataList(booklist.toMutableList())
            }
        })

    }

    fun setAdapter() {
        layoutManager = LinearLayoutManager(context)
        binding.rvResultMain.layoutManager = layoutManager
        binding.rvResultMain.addItemDecoration(DividerItemDecoration(binding.rvResultMain.context, layoutManager.orientation))

//        adapter = BookResultAdapter(viewModel.bookList, viewModel.toggleLike(0))
        adapter = BookResultAdapter(this)
        binding.rvResultMain.adapter = adapter

        viewModel.page.value = 1
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

                if (viewModel.isLoading.value == false) {

                    // 스크롤이 최하단에 도달하고, 리스트의 마지막이라면
                    if (!binding.rvResultMain.canScrollVertically(1) &&
                            lastVisibleItemPosition == itemTotalPosition) {

                        if (viewModel.isEnd.value == false) {
                            viewModel.page.value!!.plus(1)
                            loadMore()
                            Log.d("yezzz scroll", viewModel.page.value.toString())                        // 다음에 가져올 페이지가 있다면

                            viewModel.isLoading.value = true
                        }
                    }
                }
            }
        })
    }

    // TODO: 더 가져오기
    fun loadMore() {
        // set visible progress bar item
        adapter.bookDataList.add(BookData(title=""))
        adapter.notifyItemInserted(adapter.bookDataList.lastIndex)

        CoroutineScope(Dispatchers.Main).launch {
            // get more items
            viewModel.getSearchBookList(page = viewModel.page.value?:1)


            delay(1000)

            // delete progress bar item
            adapter.deleteLoading()
            adapter.notifyItemInserted(adapter.bookDataList.lastIndex)

            viewModel.isLoading.value = false
            Log.d("yezzz loadmore", viewModel.page.value.toString())
        }
    }

    override fun onItemClick(view: View, pos: Int, viewType: String) {
        // like toggle
        if (viewType == "imageButton") {
            val before = viewModel.bookList.value!![pos].like
            var after: Boolean? = null
            after = before != true
            viewModel.bookList.value!![pos].like = after
        }
        // open detail
        else if (viewType == "itemView") {
            // TODO: fragment 쌓기
            activity!!.supportFragmentManager.beginTransaction()
        }
    }
}