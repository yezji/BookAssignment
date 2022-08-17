package com.yeji.bookassignment.data

import android.util.SparseArray
import com.yeji.bookassignment.ui.SearchDetailFragment
import com.yeji.bookassignment.ui.SearchMainFragment

enum class FragmentEnum(val No: Int, val resString: String, val barType: BarTypeEnum) {
    SearchMain(0, SearchMainFragment::class.java.toString(), BarTypeEnum.Search)
    , SearchDetail(1, SearchDetailFragment::class.java.toString(), BarTypeEnum.Normal)
    ;

    companion object {
        val sparseArray: SparseArray<FragmentEnum> = SparseArray()

        init {
            for (value in FragmentEnum.values()) {
                sparseArray.put(value.No, value)
            }
        }

        fun getEnumByResString(resString: String) : FragmentEnum {
            return when(resString) {
                SearchMain.resString -> SearchMain
                else -> SearchDetail
            }
        }
    }
}