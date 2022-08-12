package com.yeji.bookassignment.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object ApiFactory {
    // server url
    private const val API_URL = "https://dapi.kakao.com/v3/" // product

    // header key
    private const val AUTHORIZATION_KEY = "Authorization"
    private const val AUTHORIZATION_VAL = "KakaoAK 9c15e2ebe7be83c02ce7df05dc607307"

    class TokenInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val originRequest = chain.request()
            val newRequest = originRequest.newBuilder()
            newRequest.addHeader(AUTHORIZATION_KEY, AUTHORIZATION_VAL)

            return chain.proceed(newRequest.build())
        }
    }

    // okhttpclient
    val client: OkHttpClient = OkHttpClient.Builder()
        .readTimeout(5000, TimeUnit.MILLISECONDS)
        .connectTimeout(5000, TimeUnit.MILLISECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply { this.level = HttpLoggingInterceptor.Level.BODY })
        .addNetworkInterceptor(TokenInterceptor()) // header
        .build()

    // retrofit instance
    val retrofit: ApiService = Retrofit.Builder()
        .baseUrl(API_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

}