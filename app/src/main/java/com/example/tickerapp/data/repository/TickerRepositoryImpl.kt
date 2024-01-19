package com.example.tickerapp.data.repository

import com.example.tickerapp.data.local.TickerDao
import com.example.tickerapp.data.local.model.FavouriteTicker
import com.example.tickerapp.data.remote.ApiService
import com.example.tickerapp.data.remote.model.TickerDetailsResults
import com.example.tickerapp.data.remote.model.TickersList

class TickerRepositoryImpl(
    private val tickerDao: TickerDao,
    private val apiService: ApiService
) : TickerRepository {

    override suspend fun getFavouriteTickers(): List<FavouriteTicker> {
        return tickerDao.getFavouriteTickers()
    }

    override suspend fun getFavouriteTicker(ticker: String): FavouriteTicker? {
        return tickerDao.getFavouriteTicker(ticker)
    }

    override suspend fun insertFavouriteTicker(ticker: FavouriteTicker) {
        tickerDao.insertFavouriteTicker(ticker)
    }

    override suspend fun deleteFavouriteTicker(ticker: String) {
        tickerDao.deleteFavouriteTicker(ticker)
    }

    override suspend fun getAllTickers(): Result<TickersList> {
        return apiService.getTickers()
    }

    override suspend fun getTickerDetails(ticker: String): Result<TickerDetailsResults> {
        return apiService.getTickerDetails(ticker)
    }
}
