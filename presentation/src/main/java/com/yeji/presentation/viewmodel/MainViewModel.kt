package com.yeji.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.yeji.domain.usecase.book.GetBookListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getBookListUseCase: GetBookListUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<MainViewState>(MainViewState())
    val uiState = _uiState.asStateFlow()

    init {
        handleEvent(event = MainEvent.RequestBookDataList)
    }

    fun handleEvent(event: MainEvent) {
        when (event) {
//            MainEvent.RequestBookDataList ->
//            MainEvent.HandleErrorComplete -> handleError()
        }
    }

    fun setIsLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    private fun setLoadError(loadError: String?) {
        _uiState.update { it.copy(loadError = loadError) }
    }

    private fun setIsProgressVisible(isProgressVisible: Boolean) {
        _uiState.update { it.copy(isProgressVisible = isProgressVisible) }
    }

    // TODO: MainViewState Error쪽에서 view 변경하기
    private fun onError(message: String) {
        setLoadError(message)
        setIsProgressVisible(false)
    }


    private fun handleError() {
        setLoadError(null)
    }

}


sealed class MainEvent {
    // ViewState 적용
    object RequestBookDataList : MainEvent()
    object HandleErrorComplete : MainEvent()
}

data class MainViewState(
    val isLoading: Boolean = false,
    val loadError: String? = null,
    val isProgressVisible: Boolean = false
)