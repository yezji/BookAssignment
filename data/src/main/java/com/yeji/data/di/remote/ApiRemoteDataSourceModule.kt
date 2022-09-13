package com.yeji.data.di.remote

import com.yeji.data.repository.remote.ApiRemoteDataSource
import com.yeji.data.repository.remote.ApiRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiRemoteDataSourceModule {
    @Binds
    abstract fun provideApiRemoteDataSource(apiRemoteDataSourceImpl: ApiRemoteDataSourceImpl): ApiRemoteDataSource
}

