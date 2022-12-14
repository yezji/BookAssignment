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
class SearchMainViewModel @Inject constructor(
    private val getBookListUseCase: GetBookListUseCase
) : ViewModel() {
    /**
     * comments
     * - LiveData Backing Property 방식 사용하기
    //    private val _keyword = MutableLiveData<String>()
    //    val keyword: LiveData<String>
    //    get() = _keyword
     * - Immutable 스타일로 사용하기
    // private val books = mutableListOf<BookData>()
     */

    /** comments
     * - StateFlow
     * - get() 형태로 작성하지 않아도 된다. `.asStateFlow()`는 내부에서 ReadonlyStateFlow다. (immutable 스타일)
     *   // private val _isProgressVisible = MutableStateFlow<Boolean>(false)
    // val isProgressVisible  = _isProgressVisible.asStateFlow()
     */
    /*
    private val _keyword = MutableStateFlow<String>("가")
    val keyword = _keyword.asStateFlow()
//    val keyword : StateFlow<String?> get() = _keyword // .asStateFlow()와 동일한 방법
    fun setKeyword(query: String?) { _keyword.value = query ?: "가" }

    private val _bookList = MutableStateFlow<List<BookData?>>(mutableListOf())
    val bookList = _bookList.asStateFlow()
    fun setBookList(bookList: List<BookData?>) { _bookList.value = bookList }

    // paging
    private val _page = MutableStateFlow<Int>(1)
    val page = _page.asStateFlow()
    fun setPage(pageNumber: Int) { _page.value = pageNumber }
    fun incrementPage() = run { _page.value.plus(1) }

    private val _isEnd = MutableStateFlow<Boolean>(true)
    val isEnd = _isEnd.asStateFlow()
    fun setIsEnd(flag: Boolean?) { _isEnd.value = flag ?: true }

    private val _pageableCount = MutableStateFlow<Int>(0)
    val pageableCount = _pageableCount.asStateFlow()
    fun setPageableCount(count: Int?) { _pageableCount.value = count ?: 0 }

    private val _totalCount = MutableStateFlow<Int>(0)
    val totalCount = _totalCount.asStateFlow()
    fun setTotalCount(count: Int?) { _totalCount.value = count ?: 0 }

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()
    fun setIsLoading(flag: Boolean?) { _isLoading.value = flag ?: false }

    private val _loadError = MutableStateFlow<String?>("")
    val loadError = _loadError.asStateFlow()
    fun setLoadError(message: String?) { _loadError.value = message ?: "" }

    private val _isProgressVisible = MutableStateFlow<Boolean>(false)
    val isProgressVisible  = _isProgressVisible.asStateFlow()
    fun setIsProgressVisible(flag: Boolean?) { _isProgressVisible.value = flag ?: false }
*/

    private val _uiState = MutableStateFlow<SearchMainViewState>(SearchMainViewState())
    val uiState = _uiState.asStateFlow()

    init {
        handleEvent(event = GetBookDataListEvent.RequestBookDataList)
    }

    fun handleEvent(event: GetBookDataListEvent) {
        when (event) {
            GetBookDataListEvent.RequestBookDataList -> getAllList()
            GetBookDataListEvent.HandleErrorComplete -> handleError()
        }
    }

    // for debounce controll
    private var testFlow: Flow<String>? = null
    private var testJob: Job? = null

    fun setKeyword(keyword: String, isImmediate: Boolean) {
        // _uiState.value = _uiState.value.copy(page = 1) 이런식으로 바로 넣어주면 스레드 간 경쟁상태 생길 수 있다. 그래서 atomic하게 값을 변경하기 위해 update를 사용한다.
        // https://medium.com/geekculture/atomic-updates-with-mutablestateflow-dc0331724405
        _uiState.update { it.copy(keyword = keyword) }

        if (isImmediate) {
            flow<String> {
                emit(_uiState.value.keyword!!)
            }
            .filter { query ->
                query.isNotBlank() // Blank는 Empty까지 체크한다. Blank는 빈칸까지 체크.
            }
            .onEach {
                getAllList()
            }.launchIn(viewModelScope)
        }
        else {
            // 검색어 입력 후 2초 뒤에 api 요청하기 (수정 시간 주는 역할)
            testFlow =
                testFlowFunc()
                    .debounce(2000)
                    .filter { query ->
                        query.isNotBlank() // Blank는 Empty까지 체크한다. Blank는 빈칸까지 체크.
                    }
                    .distinctUntilChanged()
//                .flatMapLatest { str ->
//                    Log.d("yezzz mainviewmodel", "flatMapLatest string: $str")
//                    flow<String> {
//                        emit(str)
//                    }
//                }
                    .onEach {
                        getAllList()
                        Log.d("yezzz mainviewmodel", "debounce string: $it")
                    }

            // flow를
            if (testJob?.isActive == true) testJob?.cancel()
            testJob = testFlow?.launchIn(viewModelScope)
        }

        Log.d(
            "yezzz mainviewmodel",
            "after update :: uiState.value.keyword: ${uiState.value.keyword}"
        )
    }

    private fun testFlowFunc() = flow<String> {
        emit(_uiState.value.keyword!!)
        delay(2010)
        //delay(2001)
    }

    fun setBookList(bookList: List<BookData?>) {
        _uiState.update { it.copy(bookList = bookList) }
    }

    private fun setPage(page: Int) {
        _uiState.update { it.copy(page = page) }
    }

    private fun setIsEnd(isEnd: Boolean) {
        _uiState.update { it.copy(isEnd = isEnd) }
    }

    private fun setPageableCount(pageableCount: Int) {
        _uiState.update { it.copy(pageableCount = pageableCount) }
    }

    fun incrementPage() {
        _uiState.update { it.copy(page = (it.page ?: 1) + 1) }
    }

    private fun setTotalCount(totalCount: Int) {
        _uiState.update { it.copy(totalCount = totalCount) }
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

    private fun clearSearchBookList() {
        setBookList(mutableListOf())
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
        query: String = uiState.value.keyword ?: "가",
        sort: String = "accuracy",
        page: Int? = uiState.value.page,
        size: Int = 10, // TODO: replace fixed value
        target: String = "title"
    ) {
        setIsLoading(true)

        if (uiState.value.keyword.equals("")) {
            setKeyword("가", true)
        }
        Log.d("yezzz viewmodel", "isLoading: ${uiState.value.isLoading}")
        Log.d("yezzz viewmodel", "query: $query, page: $page")



        // request search api
        val responseFlow = getBookListUseCase(query, sort, page, size, target)
        responseFlow
            // viewModel에서는 어떤 dispatcher를 지정해도 retrofit에서 통신할 때 IO로 통신하기에 문제가 없다.
//                .flowOn(Dispatchers.IO) // runtimeexception check Default, view에서 조작해보기
            .collect { flow ->
                when (flow) {
                    // sealed class 사용하여 State 처리
                    is Result.Success -> {
                        // success case
                        val docs = flow.data.documents
                        val meta = flow.data.meta

                        docs?.let { newBookList ->
                            if (newBookList.isNotEmpty()) {
                                uiState.value.bookList.let { oldBookList ->
                                    val list = oldBookList.toMutableList()
                                    list.addAll(newBookList)
                                    setBookList(list)
                                }
                            }
                        }
                        meta?.let { metaData ->
                            metaData.is_end?.let { setIsEnd(it) }
                            metaData.pageable_count?.let { setPageableCount(it) }
                            metaData.total_count?.let { setTotalCount(it) }

                            Log.d("yezzz viewmodel", "documents: ${flow.data.documents}")
                            Log.d("yezzz viewmodel", "meta: ${meta}")
                        }

                        setIsLoading(false)
                    }
                    is Result.Loading -> {
                        setIsLoading(true)
                    }
                    is Result.Failure -> {
                        // failure case
                        onError("err msg: ${flow.exception.message}")
                        setIsLoading(false)
                    }
                }
            }
    }

    private fun handleError() {
        setLoadError(null)
    }

}


sealed class GetBookDataListEvent {
    // ViewState 적용
    object RequestBookDataList : GetBookDataListEvent()
    object HandleErrorComplete : GetBookDataListEvent()
}

data class SearchMainViewState (
    val keyword: String? = "가",
    val bookList: List<BookData?> = mutableListOf(),

    val page: Int? = 1,
    val isEnd: Boolean = false,
    val pageableCount : Int? = 0,
    val totalCount: Int? = 0,

    val isLoading: Boolean = false,
    val loadError: String? = null,
    val isProgressVisible: Boolean = false
)