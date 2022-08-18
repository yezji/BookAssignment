package com.yeji.bookassignment.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.yeji.bookassignment.data.BookData
import com.yeji.bookassignment.data.FragmentEnum
import com.yeji.bookassignment.data.Response
import com.yeji.bookassignment.repository.ApiRepository
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback

//class MainViewModel(private val repository: ApiRepository): ViewModel() {
class MainViewModel: ViewModel() {

    /**
     * comments
     TODO: Backing Property 방식 적용하기
     * - Backing Property 방식 사용하기
        //    private val _keyword = MutableLiveData<String>()
        //    val keyword: LiveData<String>
        //    get() = _keyword
     TODO: Immutable 스타일로 사용하기
     * - Immutable 스타일로 사용하기
        // private val books = mutableListOf<BookData>()
     */
    val keyword = MutableLiveData<String>()

    val isProgressVisible = MutableLiveData<Boolean>()
    val loadError = MutableLiveData<String?>()

    // paging
    val page = MutableLiveData<Int>(1)
    val isEnd = MutableLiveData<Boolean>(true)
    val pageableCount = MutableLiveData<Int>(0)
    val totalCount = MutableLiveData<Int>(0)
    val isLoading = MutableLiveData<Boolean>(false)

     val _bookList = MutableLiveData<MutableList<BookData?>>()
    val bookList: LiveData<MutableList<BookData?>> get() = _bookList


    init {
        isProgressVisible.value = false
        keyword.value = "가"
        loadError.value = ""

        page.value = 1
        isEnd.value = true
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

    private suspend fun getSearchBookList(query: String = keyword.value ?: "가",
                                          sort: String = "accuracy",
                                          page: Int = this.page.value ?: 1,
                                          size: Int = 10, // TODO: replace fixed value
                                          target: String = "title")
    {
            withContext(Dispatchers.Main) {
                isLoading.value = true
//                isProgressVisible.value = true

                if (keyword.value.equals("")) {
                    keyword.value = "가"
                }
                Log.d("yezzz viewmodel", "isLoading: ${isLoading.value.toString()}")
                Log.d("yezzz viewmodel", "query: $query, page: $page")
            }

            // request search api
            val response = ApiRepository.getSearchBookList(query, sort, page, size, target)

            withContext(Dispatchers.Main) {
                // success case

                if (response.documents.isNotEmpty()) {
                    val documents = response.documents
                    val meta = response.meta
                    if (documents.isNotEmpty()) {
                        if (bookList.value == null) {
                            _bookList.value = (documents as MutableList<BookData?>)
                        } else {
                            val list = bookList.value
                            list!!.addAll(documents)
                            _bookList.value = list!!
                        }
                    }
                    Log.d("yezzz viewmodel", "meta: ${meta}")

                    isEnd.value = meta.is_end
                    pageableCount.value = meta.pageable_count
                    totalCount.value = meta.total_count

                    loadError.value = ""
                }
                // failure case
                else {
                    onError("err msg: ${response.errorType}")
                }


                isLoading.value = false
                Log.d("yezzz viewModel", "isLoading: ${isLoading.value}")
                Log.d("yezzz viewModel", "page: ${page}")

            }
    }

    private fun onError(message: String) {
        loadError.value = message
        isProgressVisible.value = false
        isLoading.value = false
    }

    private fun clearSearchBookList() {
        _bookList.value = mutableListOf()
    }

}


/**
 * comments
 * TODO: DI
 * - DI 주입하는 경우 repository 넘겨서 사용
 */
/*
// Parameter가 있는 ViewModel
class MainViewModelFactory(private val repository: ApiRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) return MainViewModel(repository) as T
        throw IllegalAccessException("Unknown Viewmodel Class")
    }
}
*/
