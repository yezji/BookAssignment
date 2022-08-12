package com.yeji.bookassignment

import android.app.Application
import com.yeji.bookassignment.repository.ApiRepository

class App : Application() {
    val repository = ApiRepository()

    override fun onCreate() {
        super.onCreate()

    }
}