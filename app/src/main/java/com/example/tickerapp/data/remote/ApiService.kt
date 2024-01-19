package com.example.tickerapp.data.remote

import com.example.tickerapp.data.remote.model.Ticker
import com.example.tickerapp.data.remote.model.TickerDetails
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("v3/reference/tickers")
    suspend fun getTickers(): List<Ticker>

    @GET("v3/reference/tickers/{ticker}")
    suspend fun getTickerDetails(@Path("ticker") ticker: String): TickerDetails
}
