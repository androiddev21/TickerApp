package com.example.tickerapp.presentation.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.tickerapp.data.repository.TickerRepository
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.instance

class TickerDetailsViewModel(application: Application) : AndroidViewModel(application),
    TickerDetailsContact, DIAware {

    override val di: DI by closestDI(application)
    val repository: TickerRepository by instance()
}