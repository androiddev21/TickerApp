package com.example.tickerapp.di

import com.example.tickerapp.BuildConfig
import com.example.tickerapp.data.remote.ApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val RETROFIT_URL = "https://api.polygon.io/"
private const val API_KEY_PARAM = "apiKey"

val retrofitModule = DI.Module("retrofit") {
    bind<ApiService>() with singleton {
        Retrofit.Builder()
            .baseUrl(RETROFIT_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { apiKeyAsQuery(it) }
                    .build()
            )
            .build()
            .create(ApiService::class.java)
    }
}

private fun apiKeyAsQuery(chain: Interceptor.Chain) = chain.proceed(
    chain.request()
        .newBuilder()
        .url(
            chain.request().url().newBuilder()
                .addQueryParameter(API_KEY_PARAM, BuildConfig.POLYGON_IO_API_KEY).build()
        )
        .build()
)