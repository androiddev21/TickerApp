package com.example.tickerapp.presentation.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tickerapp.data.local.model.FavouriteTicker
import com.example.tickerapp.data.remote.model.Ticker
import com.example.tickerapp.data.repository.TickerRepository
import com.example.tickerapp.presentation.model.TickerUI
import com.example.tickerapp.presentation.search.SearchTickerContract.Action
import com.example.tickerapp.presentation.search.SearchTickerContract.Action.NavigateBack
import com.example.tickerapp.presentation.search.SearchTickerContract.Action.NavigateToTickerDetails
import com.example.tickerapp.presentation.search.SearchTickerContract.Action.ShowSomethingWentWrongAlert
import com.example.tickerapp.presentation.search.SearchTickerContract.Action.ShowTickerAlreadyAddedAlert
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

    private val cachedAddedTickers: MutableSet<TickerUI> = mutableSetOf()
    private val cachedTickersList: MutableList<TickerUI> = mutableListOf()

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
            if (cachedTickersList.isEmpty()) {
                val tickers = getAllTickers()
                if (tickers.isNullOrEmpty()) {
                    _action.emit(ShowSomethingWentWrongAlert)
                } else {
                    cachedTickersList.addAll(
                        tickers.map { it.toUI() }
                    )
                    _state.emit(State(tickers = cachedTickersList.filter(searchQuery)))
                }
            } else {
                _state.emit(State(tickers = cachedTickersList.filter(searchQuery)))
            }
        }
    }

    private suspend fun getAllTickers(): List<Ticker>? {
        val result = repository.getAllTickers()
        return result.getOrNull()?.list
    }

    private fun onAddToFavourites(ticker: TickerUI) {
        viewModelScope.launch {
            val isAlreadyAdded = repository.getFavouriteTicker(ticker.ticker) != null
            if (isAlreadyAdded) {
                _action.emit(ShowTickerAlreadyAddedAlert)
            } else {
                cachedAddedTickers.add(ticker)
                repository.insertFavouriteTicker(ticker.toLocal())
                val tickers = _state.value.tickers.filter { !cachedAddedTickers.contains(it) }
                _state.emit(State(tickers = tickers))
            }
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

    private fun Ticker.toUI() =
        TickerUI(
            ticker = ticker,
            name = name,
            isFavourite = false
        )

    private fun TickerUI.toLocal() =
        FavouriteTicker(
            ticker = ticker,
            name = name
        )

    private fun List<TickerUI>.filter(searchQuery: String) = filter {
        it.name.contains(searchQuery, ignoreCase = true) && !cachedAddedTickers.contains(it)
    }
}