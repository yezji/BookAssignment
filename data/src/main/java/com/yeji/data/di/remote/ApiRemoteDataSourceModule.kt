package com.yeji.data.di.remote

import com.yeji.data.api.remote.BookApiService
import com.yeji.data.model.BookResponseDto
import com.yeji.data.repository.ApiRepositoryImpl
import com.yeji.data.repository.remote.ApiRemoteDataSource
import com.yeji.data.repository.remote.ApiRemoteDataSourceImpl
import com.yeji.domain.repository.ApiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiRemoteDataSourceModule {
    @Singleton
    @Provides
    fun provideApiRemoteDataSource(bookApiService: BookApiService): ApiRemoteDataSource {
        return ApiRemoteDataSourceImpl(bookApiService)
    }
}

