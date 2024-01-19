package com.example.tickerapp.data.repository

import com.example.tickerapp.data.local.model.FavouriteTicker
import com.example.tickerapp.data.remote.model.Ticker
import com.example.tickerapp.data.remote.model.TickerDetails

interface TickerRepository {
    suspend fun getFavouriteTickers(): List<FavouriteTicker>
    suspend fun insertTicker(ticker: FavouriteTicker)
    suspend fun deleteTicker(ticker: String)
    suspend fun getAllTickers(): List<Ticker>
    suspend fun getTickerDetails(ticker: String): TickerDetails
}
