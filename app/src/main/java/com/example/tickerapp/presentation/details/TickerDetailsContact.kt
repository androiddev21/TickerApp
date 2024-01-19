package com.example.tickerapp.presentation.details

import com.example.tickerapp.presentation.model.TickerDetailsUI
import com.example.tickerapp.presentation.model.TickerUI
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface TickerDetailsContact {
    interface ViewModel {
        val state: StateFlow<State>
        val action: SharedFlow<Action>
        fun viewEvent(viewEvent: ViewEvent)
    }

    sealed interface ViewEvent {
        class Init(val ticker: TickerUI) : ViewEvent
        data object ChangeFavouriteState : ViewEvent
        data object BackClicked : ViewEvent
    }

    sealed interface Action {
        data object NavigateBack : Action
        data object ShowSomethingWentWrongAlert : Action
    }

    data class State(
        val tickerDetailsUI: TickerDetailsUI? = null,
        val isFavourite: Boolean = false
    )
}