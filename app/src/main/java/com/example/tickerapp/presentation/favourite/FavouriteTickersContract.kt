package com.example.tickerapp.presentation.favourite

import com.example.tickerapp.presentation.model.TickerUI
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface FavouriteTickersContract {
    interface ViewModel {
        val state: StateFlow<State>
        val action: SharedFlow<Action>

        fun viewEvent(viewEvent: ViewEvent)
    }

    sealed interface ViewEvent {
        data object Init : ViewEvent
        class RemoveFromFavourites(val ticker: TickerUI) : ViewEvent
        data object AddClicked : ViewEvent
        data class FavouriteTickerClicked(val ticker: TickerUI) : ViewEvent
    }

    sealed interface Action {
        data object NavigateToSearch : Action
        class NavigateToTickerDetails(val ticker: TickerUI) : Action
    }

    data class State(
        val tickers: List<TickerUI> = emptyList()
    )
}