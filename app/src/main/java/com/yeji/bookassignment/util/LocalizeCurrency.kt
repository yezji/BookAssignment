package com.yeji.bookassignment.util

import java.text.NumberFormat
import java.util.*

object LocalizeCurrency {
    /**
     * 숫자 포멧이 적용된 값을 가져옴. (default locale 로 따라감)
     * @return USD, KRW
     */
    val currencyCode: String
        get() = Currency.getInstance(Locale.getDefault()).currencyCode

    /**
     * 숫자 포멧이 적용된 값을 가져옴. (default locale 로 따라감)
     * @return $, ₩
     */
    val currencySymbol: String
        get() = Currency.getInstance(Locale.getDefault()).symbol

    /**
     * 현지 화폐 단위가 적용된 값을 가져옴. (default locale 로 따라감)
     * @param _value
     * @return
     */
    fun getCurrency(_value: Double): String {
        val nFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
        nFormat.currency = Currency.getInstance(Locale.getDefault())
        return nFormat.format(_value)
    }
}