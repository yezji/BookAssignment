package com.yeji.data.di.remote

import com.yeji.data.api.remote.BookApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BookDataModule {
    @Singleton
    @Provides
    fun provideBookApiService(retrofit: Retrofit.Builder): BookApiService {
        return retrofit.build().create(BookApiService::class.java)
    }
}