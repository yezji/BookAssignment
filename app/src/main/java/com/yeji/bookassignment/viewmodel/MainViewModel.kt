package com.yeji.bookassignment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yeji.bookassignment.data.BookData
import com.yeji.bookassignment.data.FragmentEnum
import com.yeji.bookassignment.network.ApiFactory
import com.yeji.bookassignment.repo.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModel(private val repository: ApiRepository): ViewModel() {
    val isProgressVisible = MutableLiveData<Boolean>()
    val keyword = MutableLiveData<String>()
    val fragmentType = MutableLiveData<FragmentEnum>()

    private val _bookList = MutableLiveData<List<BookData>>()
    val bookList: MutableLiveData<List<BookData>> get() = _bookList


    fun getSearchBookList(query: String,
                          sort: String?,
                          page: Int?,
                          size: Int?,
                          target: String?) {
        isProgressVisible.postValue(true)
        // TODO: request search api
        GlobalScope.launch(Dispatchers.IO) {
            val response = repository.getSearchBookList(query, sort, page, size, target)
            GlobalScope.launch(Dispatchers.IO) {
                val meta = response.meta
                val docs = response.document
                bookList.postValue(docs)
            }
            isProgressVisible.postValue(false)
        }

    }
}


// Parameter가 있는 ViewModel
class MainViewModelFactory(private val repository: ApiRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) return MainViewModel(repository) as T
        throw IllegalAccessException("Unknown Viewmodel Class")
    }
}
