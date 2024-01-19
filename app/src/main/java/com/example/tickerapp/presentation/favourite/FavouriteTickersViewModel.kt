package com.example.tickerapp.presentation.favourite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tickerapp.data.local.model.FavouriteTicker
import com.example.tickerapp.data.repository.TickerRepository
import com.example.tickerapp.presentation.favourite.FavouriteTickersContract.Action
import com.example.tickerapp.presentation.favourite.FavouriteTickersContract.Action.NavigateToTickerDetails
import com.example.tickerapp.presentation.favourite.FavouriteTickersContract.Action.NavigateToSearch
import com.example.tickerapp.presentation.favourite.FavouriteTickersContract.State
import com.example.tickerapp.presentation.favourite.FavouriteTickersContract.ViewEvent.Init
import com.example.tickerapp.presentation.favourite.FavouriteTickersContract.ViewEvent.AddClicked
import com.example.tickerapp.presentation.favourite.FavouriteTickersContract.ViewEvent.FavouriteTickerClicked
import com.example.tickerapp.presentation.favourite.FavouriteTickersContract.ViewEvent.RemoveFromFavourites
import com.example.tickerapp.presentation.model.TickerUI
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

class FavouriteTickersViewModel(application: Application) : AndroidViewModel(application),
    FavouriteTickersContract.ViewModel, DIAware {

    override val di: DI by closestDI(application)
    private val repository: TickerRepository by instance()

    private val _state: MutableStateFlow<State> = MutableStateFlow(State())
    override val state: StateFlow<State> = _state.asStateFlow()

    private val _action: MutableSharedFlow<Action> = MutableSharedFlow()
    override val action: SharedFlow<Action> = _action.asSharedFlow()

    override fun viewEvent(viewEvent: FavouriteTickersContract.ViewEvent) {
        when (viewEvent) {
            Init -> onInit()
            AddClicked -> onAddClicked()
            is RemoveFromFavourites -> onRemoveFromFavourites(viewEvent.ticker.ticker)
            is FavouriteTickerClicked -> onFavouriteTickerClicked(viewEvent.ticker)
        }
    }

    private fun onInit() {
        viewModelScope.launch {
            val tickers = repository.getFavouriteTickers()
            _state.emit(State(tickers = tickers.map { it.toUI() }))
        }
    }

    private fun onRemoveFromFavourites(ticker: String) {
        viewModelScope.launch {
            repository.deleteTicker(ticker)
            val tickers = repository
                .getFavouriteTickers()
                .map { it.toUI() }
            _state.emit(State(tickers = tickers))
        }
    }

    private fun onAddClicked() {
        viewModelScope.launch {
            _action.emit(NavigateToSearch)
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
}