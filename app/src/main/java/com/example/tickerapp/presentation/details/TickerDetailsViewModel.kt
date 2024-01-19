package com.example.tickerapp.presentation.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tickerapp.data.local.model.FavouriteTicker
import com.example.tickerapp.data.remote.model.TickerDetails
import com.example.tickerapp.data.repository.TickerRepository
import com.example.tickerapp.presentation.details.TickerDetailsContact.Action
import com.example.tickerapp.presentation.details.TickerDetailsContact.Action.NavigateBack
import com.example.tickerapp.presentation.details.TickerDetailsContact.Action.ShowSomethingWentWrongAlert
import com.example.tickerapp.presentation.details.TickerDetailsContact.State
import com.example.tickerapp.presentation.details.TickerDetailsContact.ViewEvent
import com.example.tickerapp.presentation.details.TickerDetailsContact.ViewEvent.BackClicked
import com.example.tickerapp.presentation.details.TickerDetailsContact.ViewEvent.ChangeFavouriteState
import com.example.tickerapp.presentation.details.TickerDetailsContact.ViewEvent.Init
import com.example.tickerapp.presentation.model.TickerDetailsUI
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

class TickerDetailsViewModel(application: Application) : AndroidViewModel(application),
    TickerDetailsContact.ViewModel, DIAware {

    override val di: DI by closestDI(application)
    private val repository: TickerRepository by instance()

    private val _state: MutableStateFlow<State> = MutableStateFlow(State())
    override val state: StateFlow<State> = _state.asStateFlow()

    private val _action: MutableSharedFlow<Action> = MutableSharedFlow()
    override val action: SharedFlow<Action> = _action.asSharedFlow()
    override fun viewEvent(viewEvent: ViewEvent) {
        when (viewEvent) {
            is Init -> onInit(viewEvent.ticker)
            BackClicked -> onBackClicked()
            ChangeFavouriteState -> onChangeFavouriteState()
        }
    }

    private fun onInit(tickerUI: TickerUI) {
        viewModelScope.launch {
            val result = repository.getTickerDetails(tickerUI.ticker)
            val ticker = result.getOrNull()
            if (ticker == null) {
                _action.emit(ShowSomethingWentWrongAlert)
            } else {
                _state.emit(
                    State(
                        tickerDetailsUI = ticker.tickerDetails.toUI(),
                        isFavourite = tickerUI.isFavourite
                    )
                )
            }
        }
    }

    private fun onBackClicked() {
        viewModelScope.launch {
            _action.emit(NavigateBack)
        }
    }

    private fun onChangeFavouriteState() {
        val prevState = _state.value
        prevState.tickerDetailsUI?.let {
            viewModelScope.launch {
                if (prevState.isFavourite) {
                    repository.deleteFavouriteTicker(it.ticker)
                } else {
                    repository.insertFavouriteTicker(it.toFavouriteTicker())
                }
                _state.emit(prevState.copy(isFavourite = !prevState.isFavourite))
            }
        }
    }

    private fun TickerDetails.toUI() =
        TickerDetailsUI(
            ticker = ticker,
            name = name,
            market = market,
            homepageUrl = homepageUrl,
            phoneNumber = phoneNumber
        )

    private fun TickerDetailsUI.toFavouriteTicker() =
        FavouriteTicker(
            ticker = ticker,
            name = name
        )
}