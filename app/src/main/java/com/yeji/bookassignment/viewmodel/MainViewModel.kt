package com.yeji.bookassignment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yeji.bookassignment.data.BookData
import com.yeji.bookassignment.data.FragmentEnum
import com.yeji.bookassignment.repo.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val repository: ApiRepository): ViewModel() {
    val isProgressVisible = MutableLiveData<Boolean>()
    val keyword = MutableLiveData<String>()
    val fragmentType = MutableLiveData<FragmentEnum>()

    // paging
    val isEnd = MutableLiveData<Boolean>()
    val pageableCount = MutableLiveData<Int>()
    val totalCount = MutableLiveData<Int>()
    val page = MutableLiveData<Int>()

    private val _bookList = MutableLiveData<MutableList<BookData>>()
    val bookList: MutableLiveData<MutableList<BookData>> get() = _bookList

    fun getAllList() {
        getSearchBookList("가","accuracy", 1, 50, "title")
    }

    fun getSearchBookList(query: String,
                          sort: String?,
                          page: Int?,
                          size: Int?,
                          target: String?) {
        isProgressVisible.postValue(true)

        // request search api
        GlobalScope.launch(Dispatchers.IO) {
            val response = repository.getSearchBookList(query, sort, page, size, target)
            withContext(Dispatchers.Default) {
                // TODO: meta로 페이징 처리
                val meta = response.meta
                val docs = response.document
                bookList.postValue(docs as MutableList<BookData>)
                isEnd.postValue(meta.is_end)
                pageableCount.postValue(meta.pageable_count)
                totalCount.postValue(meta.total_count)
            }
            // TODO: 실패 시 메세지 출력

            isProgressVisible.postValue(false)
        }
    }

    fun clearSearchBookList() {
        bookList.postValue(mutableListOf())
    }

    fun toggleLike(pos: Int) {
        val before = bookList.value!![pos].like
        bookList.value!![pos].like = !before
    }

}


// Parameter가 있는 ViewModel
class MainViewModelFactory(private val repository: ApiRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) return MainViewModel(repository) as T
        throw IllegalAccessException("Unknown Viewmodel Class")
    }
}
