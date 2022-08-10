package com.yeji.bookassignment.data

import android.util.SparseArray

enum class BarTypeEnum(val No: Int) {
    Search(0)
    ,Normal(1)
    ;

    companion object {
        val sparseArray: SparseArray<BarTypeEnum> = SparseArray()

        init {
            for (value in values()) {
                sparseArray.put(value.No, value)
            }
        }

        fun getEnumByType(type: Int) : BarTypeEnum {
            return when(type) {
                Search.No -> Search
                else -> Normal
            }
        }
    }
}