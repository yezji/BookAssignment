package com.yeji.bookassignment.data

data class MainViewState(
    val keyword: String? = "가",
    val bookList: List<BookData>? = mutableListOf(),
    val page: Int? = 1,
    val isEnd: Boolean = false,
    val pageableCount : Int? = 0,
    val totalCount: Int? = 0,
    val isLoading: Boolean = false,
    val loadError: String? = "",
    val isProgressVisible: Boolean = false
)