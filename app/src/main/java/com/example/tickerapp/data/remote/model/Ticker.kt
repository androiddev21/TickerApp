package com.example.tickerapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class Ticker(
    @SerializedName("ticker")
    val ticker: String,
    @SerializedName("name")
    val name: String
)

data class TickersList(
    @SerializedName("results")
    val list: List<Ticker>
)
