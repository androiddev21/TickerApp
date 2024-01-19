package com.example.tickerapp.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TickerUI(
    val ticker: String,
    val name: String,
    val isFavourite: Boolean
) : Parcelable
