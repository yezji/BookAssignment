package com.yeji.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.yeji.domain.model.BookData
import com.yeji.domain.repository.Result
import com.yeji.domain.usecase.book.GetBookListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SearchDetailViewModel @Inject constructor(
    private val getBookListUseCase: GetBookListUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<SearchDetailViewState>(SearchDetailViewState())
    val uiState = _uiState.asStateFlow()

    init {
        handleEvent(event = GetDetailedBookDataEvent.RequestBookDataList)
    }

    fun handleEvent(event: GetDetailedBookDataEvent) {
        when (event) {
//            GetDetailedBookDataEvent.RequestBookDataList -> getAllList()
//            GetDetailedBookDataEvent.HandleErrorComplete -> handleError()
        }
    }

    fun setBookData(bookData: BookData) {
        _uiState.update { it.copy(bookData = bookData) }
    }

    private fun setLike(like: Boolean) {
        _uiState.update { it.copy(like = like) }
    }

    fun toggleLike() : Boolean {
        setLike(!uiState.value.like)
        return uiState.value.like
    }

    fun setIsLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    private fun setLoadError(loadError: String?) {
        _uiState.update { it.copy(loadError = loadError) }
    }


    // TODO: MainViewState Error쪽에서 view 변경하기
    private fun onError(message: String) {
        setLoadError(message)
    }

    private fun clearBookData() {
        setBookData(
            BookData(
                title = null,
                contents = null,
                url = null,
                isbn = null,
                datetime = null,
                authors = null,
                publisher = null,
                translators = null,
                price = null,
                sale_price = null,
                thumbnail = null,
                status = null,
                like = false
            ))
    }

    /** comments
     * MainViewState 형태로 변환하기
     * - State 스타일 참고 https://developer.android.com/jetpack/compose/state?hl=ko#viewmodels-source-of-truth
     * - example
    data class MainViewState(
    var isEnd: Boolean = false,
    val isLoading:Boolean = false
    )
    private val _uiState = MutableStateFlow<MainViewState>()
    val uiState = _uiState.asStateFlow()

    fun test(){
    _uiState.update {
    it.copy (
    isEnd = true
    )
    }
    }
     */

    fun setBookData() {
        clearBookData()
    }

    private fun handleError() {
        setLoadError(null)
    }

}


sealed class GetDetailedBookDataEvent {
    // ViewState 적용
    object RequestBookDataList : GetDetailedBookDataEvent()
    object HandleErrorComplete : GetDetailedBookDataEvent()
}

data class SearchDetailViewState(
    val bookData: BookData = BookData(
        title = null,
        contents = null,
        url = null,
        isbn = null,
        datetime = null,
        authors = null,
        publisher = null,
        translators = null,
        price = null,
        sale_price = null,
        thumbnail = null,
        status = null,
        like = false
    ),

    val like: Boolean = false,

    val isLoading: Boolean = false,
    val loadError: String? = null
)