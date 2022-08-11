package com.yeji.bookassignment.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yeji.bookassignment.data.BookData
import com.yeji.bookassignment.data.FragmentEnum
import com.yeji.bookassignment.repo.ApiRepository
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

    private val _bookList = MutableLiveData<MutableList<BookData>>()
    val bookList: MutableLiveData<MutableList<BookData>> get() = _bookList

    fun getAllList() {
        getSearchBookList(query = "쉽게 배우는 자바" , page = 1)
//        getSearchBookList(query = "가" , page = 1)
    }

    fun getSearchBookList(query: String = keyword.value?:"가",
                          sort: String = "accuracy",
                          page: Int = 1,
                          size: Int = 10,
                          target: String = "title") {

        isProgressVisible.value = true

        // request search api
        val job = CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getSearchBookList(query, sort, page, size, target)
            withContext(Dispatchers.Main) {

                if (response.documents.isNotEmpty()) {
                    // TODO: meta로 페이징 처리
                    if (bookList.value == null) {
                        bookList.value = (response.documents as MutableList<BookData>)
                    }
                    else {
                        bookList.value!!.addAll(response.documents)
                    }

                    isEnd.value = response.meta.is_end
                    pageableCount.value = response.meta.pageable_count
                    totalCount.value = response.meta.total_count

                    loadError.value = ""
                    isProgressVisible.value = false
                }
                else if (response.errorType != "") {
                    // TODO: 실패 시 메세지 출력
                    onError("${response.errorType}, message: ${response.message}")
                }


            }

        }
    }

    fun onError(message: String) {
        loadError.value = message
        isProgressVisible.value = false
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
