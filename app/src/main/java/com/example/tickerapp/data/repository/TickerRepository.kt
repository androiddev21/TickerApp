package com.example.tickerapp.data.repository

import com.example.tickerapp.data.local.model.FavouriteTicker
import com.example.tickerapp.data.remote.model.TickerDetailsResults
import com.example.tickerapp.data.remote.model.TickersList

interface TickerRepository {
    suspend fun getFavouriteTickers(): List<FavouriteTicker>
    suspend fun getFavouriteTicker(ticker: String): FavouriteTicker?
    suspend fun insertFavouriteTicker(ticker: FavouriteTicker)
    suspend fun deleteFavouriteTicker(ticker: String)
    suspend fun getAllTickers(): Result<TickersList>
    suspend fun getTickerDetails(ticker: String): Result<TickerDetailsResults>
}
