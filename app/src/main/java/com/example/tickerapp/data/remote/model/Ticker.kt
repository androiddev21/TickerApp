package com.example.tickerapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class Ticker(
    @SerializedName("ticker")
    val ticker: String,
    @SerializedName("name")
    val name: String
)
