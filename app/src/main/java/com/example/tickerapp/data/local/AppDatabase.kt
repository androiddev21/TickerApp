package com.example.tickerapp.data.local

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tickerapp.data.local.model.FavouriteTicker

@Database(entities = [FavouriteTicker::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tickerDao(): TickerDao

    companion object {
        fun getInstance(app: Application) =
            Room.databaseBuilder(app, AppDatabase::class.java, "tickers.db")
                .build()
    }
}
