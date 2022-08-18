package com.yeji.bookassignment.network

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * comments
 * - Factory는 class로 정의하여 사용. 디자인패턴에서 class로 만들고 create()해서 사용하기에
 *   의도한대로 Singleton으로 사용하려면 object가 더 적합하다.
 */
object ApiClient {
    // server url
    private const val API_URL = "https://dapi.kakao.com/v3/" // product

    // header key
    private const val AUTHORIZATION_KEY = "Authorization"
    private const val AUTHORIZATION_VAL = "KakaoAK 9c15e2ebe7be83c02ce7df05dc607307"

    private val json = Json {
        isLenient = true // Json 큰따옴표 느슨하게 체크
        ignoreUnknownKeys = true // Field 값이 없는 경우 무시
        coerceInputValues = true // "null" 이 들어간경우 default Argument 값으로 대체
        prettyPrint = true // 읽기 편한 형태로 출력
    }

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
    @OptIn(ExperimentalSerializationApi::class)
    val retrofit: ApiService = Retrofit.Builder()
        .baseUrl(API_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
//        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(ApiService::class.java)


}