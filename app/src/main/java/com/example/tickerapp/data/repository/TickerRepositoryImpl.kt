package com.example.tickerapp.data.repository

import com.example.tickerapp.data.local.TickerDao
import com.example.tickerapp.data.local.model.FavouriteTicker
import com.example.tickerapp.data.remote.ApiService
import com.example.tickerapp.data.remote.model.Ticker
import com.example.tickerapp.data.remote.model.TickerDetails

class TickerRepositoryImpl(
    private val tickerDao: TickerDao,
    private val apiService: ApiService
) : TickerRepository {

    override suspend fun getFavouriteTickers(): List<FavouriteTicker> {
        return tickerDao.getFavouriteTickers()
    }

    override suspend fun insertTicker(ticker: FavouriteTicker) {
        tickerDao.insertTicker(ticker)
    }

    override suspend fun deleteTicker(ticker: String) {
        tickerDao.deleteTicker(ticker)
    }

    override suspend fun getAllTickers(): List<Ticker> {
        return apiService.getTickers()
    }

    override suspend fun getTickerDetails(ticker: String): TickerDetails {
        return apiService.getTickerDetails(ticker)
    }
}
