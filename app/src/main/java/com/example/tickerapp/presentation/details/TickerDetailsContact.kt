package com.example.tickerapp.presentation.details

import com.example.tickerapp.presentation.model.TickerDetailsUI
import kotlinx.coroutines.flow.StateFlow

interface TickerDetailsContact {
    interface ViewModel {
        val state: StateFlow<State>
        fun viewEvent(viewEvent: ViewEvent)
    }

    sealed interface ViewEvent {
        class Init(ticker: String) : ViewEvent
        data object ChangeFavouriteState : ViewEvent
    }

    data class State(
        val tickerDetailsUI: TickerDetailsUI? = null,
        val isFavourite: Boolean
    )
}