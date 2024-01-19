package com.example.tickerapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tickerapp.data.local.model.FavouriteTicker

@Dao
interface TickerDao {
    @Query("SELECT * FROM favourite_tickers")
    suspend fun getFavouriteTickers(): List<FavouriteTicker>

    @Query("SELECT * FROM favourite_tickers WHERE ticker == :ticker")
    suspend fun getFavouriteTicker(ticker: String): FavouriteTicker?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteTicker(ticker: FavouriteTicker)

    @Query("DELETE FROM favourite_tickers WHERE ticker == :ticker")
    suspend fun deleteFavouriteTicker(ticker: String)
}