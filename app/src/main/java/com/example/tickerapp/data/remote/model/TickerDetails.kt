package com.example.tickerapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class TickerDetails(
    @SerializedName("ticker") val ticker: String,
    @SerializedName("name") val name: String,
    @SerializedName("market") val market: String,
    @SerializedName("homepage_url") val homepageUrl: String,
    @SerializedName("branding") val branding: Branding
)

data class Branding(
    @SerializedName("logo_url") val logoUrl: String
)

