package com.yeji.data.di.remote

import com.yeji.data.repository.ApiRepositoryImpl
import com.yeji.data.repository.remote.ApiRemoteDataSource
import com.yeji.data.repository.remote.ApiRemoteDataSourceImpl
import com.yeji.domain.repository.ApiRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// TODO: Binds로 변경하기 (현재 코드에서는 컴파일할 때 미리 만들어두고 메모리에 항상 올라와있음 -> 효율 낮음 그래서 Binds로 변경)
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
//class RepositoryModule { // singleton으로 만들었으면 object
//    @Singleton
//    @Provides
//    fun provideApiRepository(dataSource: ApiRemoteDataSource): ApiRepository {
//        return ApiRepositoryImpl(dataSource)
//    }

    @Binds
    abstract fun provideApiRepository(apiRepositoryImpl: ApiRepositoryImpl): ApiRepository
}
/**
 * Binds + abstract fun 구조로 만들었을 때 이점?
 * 1. dataSource에 하나가 더 추가된다면 ApiRepositoryImpl에 가서 하나를 더 추가해야하고, 또 provideApiRepository쪽에서도 한번 더 추가를 해주어야 한다. (=두 번 추가하게 됨)
 * 2. retrofit에서 interface로 쓰는 것처럼 알아서 다 해주는 느낌이다. 그래서 return같은거도 필요 없게 됨
 */

