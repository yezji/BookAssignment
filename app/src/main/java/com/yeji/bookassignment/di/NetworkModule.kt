package com.yeji.bookassignment.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.yeji.data.utils.API_URL
import com.yeji.data.utils.AUTHORIZATION_KEY
import com.yeji.data.utils.AUTHORIZATION_VAL
import com.yeji.data.utils.INT_TYPE_NETWORK_TIME_OUT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            with(chain) {
                val originRequest = request()
                val newRequest = originRequest.newBuilder()
                newRequest.addHeader(AUTHORIZATION_KEY, AUTHORIZATION_VAL)

                proceed(newRequest.build())
            }
        }
    }

    @Provides
    fun provideHttpLogginInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    fun provideOkHttpClient(
        headerInterceptor: Interceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addNetworkInterceptor(interceptor = headerInterceptor) // header
            .addInterceptor(interceptor = httpLoggingInterceptor)

            .readTimeout(INT_TYPE_NETWORK_TIME_OUT, TimeUnit.MILLISECONDS)
            .connectTimeout(INT_TYPE_NETWORK_TIME_OUT, TimeUnit.MILLISECONDS)
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit.Builder {
//        val json = Json {
//            isLenient = true // Json 큰따옴표 느슨하게 체크
//            ignoreUnknownKeys = true // Field 값이 없는 경우 무시
//            coerceInputValues = true // "null" 이 들어간경우 default Argument 값으로 대체
//            prettyPrint = true // 읽기 편한 형태로 출력
//        }
        return Retrofit.Builder()
            .baseUrl(API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
//            .addConverterFactory(json.asConverterFactory("application/json".toMediaType())
    }

    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder().create()
    }
}