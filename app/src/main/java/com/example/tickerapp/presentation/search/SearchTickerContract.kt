package com.example.tickerapp.presentation.search

import com.example.tickerapp.presentation.model.TickerUI
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface SearchTickerContract {
    interface ViewModel {
        val state: StateFlow<State>
        val action: SharedFlow<Action>

        fun viewEvent(viewEvent: ViewEvent)
    }

    sealed interface ViewEvent {
        class SearchTickerByName(val searchQuery: String) : ViewEvent
        class AddToFavourites(val ticker: TickerUI) : ViewEvent
        data object BackClicked : ViewEvent
        data class FavouriteTickerClicked(val ticker: TickerUI) : ViewEvent
    }

    data class State(
        val tickers: List<TickerUI> = emptyList()
    )

    sealed interface Action {
        data object NavigateBack : Action
        class NavigateToTickerDetails(val ticker: TickerUI) : Action
        data object ShowTickerAlreadyAddedAlert : Action
        data object ShowSomethingWentWrongAlert : Action
    }
}