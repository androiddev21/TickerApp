package com.example.tickerapp.di

import com.example.tickerapp.presentation.details.TickerDetailsViewModel
import com.example.tickerapp.presentation.favourite.FavouriteTickersViewModel
import com.example.tickerapp.presentation.search.SearchTickerViewModel
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

val viewModelModule = DI.Module("viewModel") {
    bindProvider { FavouriteTickersViewModel(instance()) }
    bindProvider { TickerDetailsViewModel(instance()) }
    bindProvider { SearchTickerViewModel(instance()) }
}