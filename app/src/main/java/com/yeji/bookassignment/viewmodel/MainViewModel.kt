package com.yeji.bookassignment.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.yeji.bookassignment.data.BookData
import com.yeji.bookassignment.data.Response
import com.yeji.bookassignment.repository.ApiRepository
import com.yeji.bookassignment.repository.Result
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.coroutines.coroutineContext

//class MainViewModel(private val repository: ApiRepository): ViewModel() {
class MainViewModel: ViewModel() {

    /**
     * comments
     * - LiveData Backing Property 방식 사용하기
        //    private val _keyword = MutableLiveData<String>()
        //    val keyword: LiveData<String>
        //    get() = _keyword
     * - Immutable 스타일로 사용하기
        // private val books = mutableListOf<BookData>()
     */
//    private val _keyword = MutableLiveData<String>()
//    val keyword : LiveData<String> get() = _keyword
//    fun setKeyword(query: String?) { _keyword.value = query ?: "가" }
//
//    private val _bookList = MutableLiveData<List<BookData?>>()
//    val bookList: LiveData<List<BookData?>> get() = _bookList
//    fun setBookList(bookList: List<BookData?>) { _bookList.value = bookList }
//
//    // paging
//    private val _page = MutableLiveData<Int>(1)
//    val page : LiveData<Int> get() = _page
//    fun setPage(pageNumber: Int) { _page.value = pageNumber }
//    fun incrementPage() = run { _page.value?.plus(1) }
//
//    private val _isEnd = MutableLiveData<Boolean>(true)
//    val isEnd : LiveData<Boolean> get() = _isEnd
//    fun setIsEnd(flag: Boolean?) { _isEnd.value = flag ?: true }
//
//    private val _pageableCount = MutableLiveData<Int>(0)
//    val pageableCount : LiveData<Int> get() = _pageableCount
//    fun setPageableCount(count: Int?) { _pageableCount.value = count ?: 0 }
//
//    private val _totalCount = MutableLiveData<Int>(0)
//    val totalCount : LiveData<Int> get() = _totalCount
//    fun setTotalCount(count: Int?) { _totalCount.value = count ?: 0 }
//
//    private val _isLoading = MutableLiveData<Boolean>(false)
//    val isLoading : LiveData<Boolean> get() = _isLoading
//    fun setIsLoading(flag: Boolean?) { _isLoading.value = flag ?: false }
//
//    private val _loadError = MutableLiveData<String?>()
//    val loadError : LiveData<String?> get() = _loadError
//    fun setLoadError(message: String?) { _loadError.value = message ?: "" }
//
//    private val _isProgressVisible = MutableLiveData<Boolean>()
//    val isProgressVisible : LiveData<Boolean> get() = _isProgressVisible
//    fun setIsProgressVisible(flag: Boolean?) { _isProgressVisible.value = flag ?: false }

    /** comments
     * - StateFlow
     * TODO: asStateFlow() 형태로 getter 변환하기
     * - get() 형태로 작성하지 않아도 된다. `.asStateFlow()`는 내부에서 ReadonlyStateFlow다. (immutable 스타일)
     *   // private val _isProgressVisible = MutableStateFlow<Boolean>(false)
         // val isProgressVisible  = _isProgressVisible.asStateFlow()
     */
    private val _keyword = MutableStateFlow<String>("가")
    val keyword : StateFlow<String> get() = _keyword
    fun setKeyword(query: String?) { _keyword.value = query ?: "가" }

    private val _bookList = MutableStateFlow<List<BookData?>>(mutableListOf())
    val bookList: StateFlow<List<BookData?>> get() = _bookList
    fun setBookList(bookList: List<BookData?>) { _bookList.value = bookList }

    // paging
    private val _page = MutableStateFlow<Int>(1)
    val page : StateFlow<Int> get() = _page
    fun setPage(pageNumber: Int) { _page.value = pageNumber }
    fun incrementPage() = run { _page.value.plus(1) }

    private val _isEnd = MutableStateFlow<Boolean>(true)
    val isEnd : StateFlow<Boolean> get() = _isEnd
    fun setIsEnd(flag: Boolean?) { _isEnd.value = flag ?: true }

    private val _pageableCount = MutableStateFlow<Int>(0)
    val pageableCount : StateFlow<Int> get() = _pageableCount
    fun setPageableCount(count: Int?) { _pageableCount.value = count ?: 0 }

    private val _totalCount = MutableStateFlow<Int>(0)
    val totalCount : StateFlow<Int> get() = _totalCount
    fun setTotalCount(count: Int?) { _totalCount.value = count ?: 0 }

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading : StateFlow<Boolean> get() = _isLoading
    fun setIsLoading(flag: Boolean?) { _isLoading.value = flag ?: false }

    private val _loadError = MutableStateFlow<String?>("")
    val loadError : StateFlow<String?> get() = _loadError
    fun setLoadError(message: String?) { _loadError.value = message ?: "" }

    private val _isProgressVisible = MutableStateFlow<Boolean>(false)
    val isProgressVisible  = _isProgressVisible.asStateFlow()

    fun setIsProgressVisible(flag: Boolean?) { _isProgressVisible.value = flag ?: false }


    // TODO: MainViewState 형태로 변환하기
    // State 스타일 참고 https://developer.android.com/jetpack/compose/state?hl=ko#viewmodels-source-of-truth

//
//    data class MainViewState(
//        var isEnd: Boolean = false,
//        val isLoading:Boolean = false
//    )
//    private val _uiState = MutableStateFlow<MainViewState>()
//    val uiState = _uiState.asStateFlow()
//
//    fun test(){
//        _uiState.update{
//            it.copy(
//                isEnd = true
//            )
//        }
//    }
//

    init {
        _keyword.value = "가"
        _bookList.value = mutableListOf()

        _page.value = 1
        _isEnd.value = true
        _pageableCount.value = 0
        _totalCount.value = 0
        _isLoading.value = false
        _loadError.value = ""
        _isProgressVisible.value = false
    }

    fun getAllList() {
        clearSearchBookList()
        setPage(1)
        /**
         * comments
         * - retrofit에서 기본적으로 IO thread 사용해서 통신하고 있기에 안넣어줘도 무방하다.
         *   - 옛날 자바코드일 때 흔적이다.
         * - viewModelScope의 기본은 Dispatchers.Main.immediate이다.
         *   - Main.immediate는 순서 보장 O, 그냥 Main은 순서보장 X
         *   - Dispatcher.Main은 단순히 Context Switching에서 필요할 경우에 사용
         *      - ex. Dispatchers.IO를 사용하는 CoroutineScope 내에서는 withContext를 이용해서 context switching이 가능한데, withContext(Dispatchers.Main)을 사용하나, withCOntext(Dispatchers.Main.immediate)을 사용하나 결과는 동일하기에 이럴 경우 Dispatchers.Main을 사용하면 된다.
         */
        viewModelScope.launch { getSearchBookList() }
}

    fun getMoreList() {
        viewModelScope.launch { getSearchBookList() }
    }

    private suspend fun getSearchBookList(
        query: String = keyword.value ?: "가",
        sort: String = "accuracy",
        page: Int? = this.page.value,
        size: Int = 10, // TODO: replace fixed value
        target: String = "title")
    {
            setIsLoading(true)
        //                setIsProgressVisible(true)

            if (keyword.value.equals("")) {
                setKeyword("가")
            }
            Log.d("yezzz viewmodel", "isLoading: ${isLoading.value.toString()}")
            Log.d("yezzz viewmodel", "query: $query, page: $page")


            // request search api
            val responseFlow = ApiRepository.getSearchBookListFlow(query, sort, page, size, target)
            responseFlow
                    // TODO: check Default나 Unconfied일 때, view에서 조작해보아 exception 만들기
//                .flowOn(Dispatchers.IO) // runtimeexception check Default, view에서 조작해보기
                .collect { flow ->
                    when(flow){
                        // TODO: sealed class 사용하여 State 처리
                        is Result.Success -> {
                            val data = flow.data

                            if (data.isNotEmpty()) {
                                val list = bookList.value.toMutableList()
                                list.addAll(data)
                                setBookList(list)
                            }
                        }
                        is Result.Error -> {

                        }
                    }

                    if (flow.documents != null) {
                        // success case
                        val documents = flow.documents
                        val meta = flow.meta
                        if (documents.isNotEmpty()) {
                            val list = bookList.value.toMutableList()
                            list.addAll(documents)
                            setBookList(list)
                        }
                        Log.d("yezzz viewmodel", "documents: ${documents}")
                        Log.d("yezzz viewmodel", "meta: ${meta}")
                        setIsEnd(meta?.is_end)
                        setPageableCount(meta?.pageable_count)
                        setTotalCount(meta?.total_count)

                        setLoadError(null)
                    }
                    else {
                        // failure case
                        onError("err msg: ${ flow.errorType}")
                    }

                    setIsLoading(false)

                    Log.d("yezzz viewModel", "isLoading: ${isLoading.value}")
                    Log.d("yezzz viewModel", "page: ${page}")

                }

    }

    private fun onError(message: String) {
        setLoadError(message)
        setIsLoading(false)
        setIsProgressVisible(false)
    }

    private fun clearSearchBookList() {
        setBookList(mutableListOf())
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
