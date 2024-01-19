package com.example.tickerapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class TickerDetailsResults(
    @SerializedName("results")
    val tickerDetails: TickerDetails
)

data class TickerDetails(
    @SerializedName("ticker") val ticker: String,
    @SerializedName("name") val name: String,
    @SerializedName("market") val market: String,
    @SerializedName("homepage_url") val homepageUrl: String?,
    @SerializedName("phone_number") val phoneNumber: String?
)

