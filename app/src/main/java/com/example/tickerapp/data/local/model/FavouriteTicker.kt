package com.example.tickerapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "favourite_tickers")
data class FavouriteTicker(
    @PrimaryKey
    @SerializedName("ticker")
    val ticker: String,
    @SerializedName("name")
    val name: String
)
