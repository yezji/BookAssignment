package com.yeji.bookassignment.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yeji.bookassignment.data.BookData
import com.yeji.bookassignment.data.FragmentEnum
import com.yeji.bookassignment.repository.ApiRepository
import kotlinx.coroutines.*

class MainViewModel(private val repository: ApiRepository): ViewModel() {
    val isProgressVisible = MutableLiveData<Boolean>()
    val keyword = MutableLiveData<String>()
    val fragmentType = MutableLiveData<FragmentEnum>()
    val loadError = MutableLiveData<String?>()
    // paging
    val page = MutableLiveData<Int>(1)
    val isEnd = MutableLiveData<Boolean>(false)
    val pageableCount = MutableLiveData<Int>(0)
    val totalCount = MutableLiveData<Int>(0)
    val isLoading = MutableLiveData<Boolean>(false)

    private val _bookList = MutableLiveData<MutableList<BookData?>>()
    val bookList: MutableLiveData<MutableList<BookData?>> get() = _bookList

    init {
        isProgressVisible.value = false
        keyword.value = "가"
        fragmentType.value = FragmentEnum.SearchMain
        loadError.value = ""

        page.value = 1
        isEnd.value = false
        pageableCount.value = 0
        totalCount.value = 0
        isLoading.value = false

        _bookList.value = mutableListOf()
    }

    fun getAllList() {
        clearSearchBookList()
        page.value = 1
        val job = viewModelScope.launch(Dispatchers.IO) { getSearchBookList() }
}

    fun getMoreList() {
        val job = viewModelScope.launch(Dispatchers.IO) { getSearchBookList() }
    }

    suspend fun getSearchBookList(query: String = keyword.value?:"가",
                                  sort: String = "accuracy",
                                  page: Int = this.page.value!!,
                                  size: Int = 10, // TODO: replace fixed value
                                  target: String = "title")
    {
        withContext(Dispatchers.Main) {
            if (keyword.value.equals("")) {
                keyword.value = "가"
            }
            isProgressVisible.value = true
            Log.d("yezzz viewmodel", "query: $query, page: $page")

        }

        // request search api
        val response = repository.getSearchBookList(query, sort, page, size, target)
        withContext(Dispatchers.Main) {
            // success
            if (response.documents.isNotEmpty()) {
                if (bookList.value == null) {
                    bookList.value = (response.documents as MutableList<BookData?>)
                }
                else {
                    bookList.value!!.addAll(response.documents)
                }
//                    Log.d("yezzz viewmodel", "booklist: ${bookList.value!!}")
                    Log.d("yezzz viewmodel", "meta: ${response.meta}")

                isEnd.value = response.meta.is_end
                pageableCount.value = response.meta.pageable_count
                totalCount.value = response.meta.total_count

                loadError.value = ""
                isProgressVisible.value = false
            }
            // failure
            else if (response.errorType != "") {
                // TODO: 실패 시 메세지 출력
                onError("${response.errorType}, message: ${response.message}")
            }
        }


//        Log.d("yezzz viewmodel", "booklist: ${bookList.value!!}")
    }

    fun onError(message: String) {
        loadError.value = message
        isProgressVisible.value = false
    }

    fun clearSearchBookList() {
        bookList.value = mutableListOf()
    }

}


// Parameter가 있는 ViewModel
class MainViewModelFactory(private val repository: ApiRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) return MainViewModel(repository) as T
        throw IllegalAccessException("Unknown Viewmodel Class")
    }
}
