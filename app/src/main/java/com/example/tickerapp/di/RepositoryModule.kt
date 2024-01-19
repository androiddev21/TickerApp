package com.example.tickerapp.di

import com.example.tickerapp.data.repository.TickerRepository
import com.example.tickerapp.data.repository.TickerRepositoryImpl
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.provider

val repositoryModule = DI.Module("repository") {
    bind<TickerRepository>() with provider { TickerRepositoryImpl(instance(), instance()) }
}