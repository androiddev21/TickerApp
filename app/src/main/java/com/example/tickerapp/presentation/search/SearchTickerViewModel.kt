package com.example.tickerapp.presentation.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tickerapp.data.local.model.FavouriteTicker
import com.example.tickerapp.data.remote.model.Ticker
import com.example.tickerapp.data.repository.TickerRepository
import com.example.tickerapp.presentation.search.SearchTickerContract
import com.example.tickerapp.presentation.model.TickerUI
import com.example.tickerapp.presentation.search.SearchTickerContract.Action
import com.example.tickerapp.presentation.search.SearchTickerContract.Action.NavigateBack
import com.example.tickerapp.presentation.search.SearchTickerContract.Action.NavigateToTickerDetails
import com.example.tickerapp.presentation.search.SearchTickerContract.State
import com.example.tickerapp.presentation.search.SearchTickerContract.ViewEvent
import com.example.tickerapp.presentation.search.SearchTickerContract.ViewEvent.AddToFavourites
import com.example.tickerapp.presentation.search.SearchTickerContract.ViewEvent.BackClicked
import com.example.tickerapp.presentation.search.SearchTickerContract.ViewEvent.FavouriteTickerClicked
import com.example.tickerapp.presentation.search.SearchTickerContract.ViewEvent.SearchTickerByName
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.instance

class SearchTickerViewModel(application: Application) : AndroidViewModel(application),
    SearchTickerContract.ViewModel, DIAware {

    override val di: DI by closestDI(application)
    private val repository: TickerRepository by instance()

    private val _state: MutableStateFlow<State> = MutableStateFlow(State())
    override val state: StateFlow<State> = _state.asStateFlow()

    private val _action: MutableSharedFlow<Action> = MutableSharedFlow()
    override val action: SharedFlow<Action> = _action.asSharedFlow()

    private val addedTickers: MutableSet<TickerUI> = mutableSetOf()

    override fun viewEvent(viewEvent: ViewEvent) {
        when (viewEvent) {
            is SearchTickerByName -> onSearchTickerByName(viewEvent.searchQuery)
            BackClicked -> onBackClicked()
            is AddToFavourites -> onAddToFavourites(viewEvent.ticker)
            is FavouriteTickerClicked -> onFavouriteTickerClicked(
                viewEvent.ticker
            )
        }
    }

    private fun onSearchTickerByName(searchQuery: String) {
        viewModelScope.launch {
            val tickers = repository
                .getAllTickers()
                .map { it.toUI() }
                .filter { it.name == searchQuery && !addedTickers.contains(it) }
            _state.emit(State(tickers = tickers))
        }
    }

    private fun onAddToFavourites(ticker: TickerUI) {
        viewModelScope.launch {
            addedTickers.add(ticker)
            repository.insertTicker(ticker.toLocal())
            val tickers = _state.value.tickers.filter { !addedTickers.contains(it) }
            _state.emit(State(tickers = tickers))
        }
    }

    private fun onBackClicked() {
        viewModelScope.launch {
            _action.emit(NavigateBack)
        }
    }

    private fun onFavouriteTickerClicked(tickerUI: TickerUI) {
        viewModelScope.launch {
            _action.emit(NavigateToTickerDetails(tickerUI))
        }
    }

    private fun FavouriteTicker.toUI() =
        TickerUI(
            ticker = ticker,
            name = name,
            isFavourite = true
        )

    private fun Ticker.toUI() =
        TickerUI(
            ticker = ticker,
            name = name,
            isFavourite = true
        )

    private fun TickerUI.toLocal() =
        FavouriteTicker(
            ticker = ticker,
            name = name
        )
}