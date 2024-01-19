package com.example.tickerapp.data.remote

import com.example.tickerapp.data.remote.model.TickerDetailsResults
import com.example.tickerapp.data.remote.model.TickersList
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("v3/reference/tickers")
    suspend fun getTickers(): Result<TickersList>

    @GET("v3/reference/tickers/{ticker}")
    suspend fun getTickerDetails(@Path("ticker") ticker: String): Result<TickerDetailsResults>
}
