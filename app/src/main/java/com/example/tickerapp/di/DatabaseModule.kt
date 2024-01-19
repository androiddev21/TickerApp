package com.example.tickerapp.di

import com.example.tickerapp.data.local.AppDatabase
import com.example.tickerapp.data.local.TickerDao
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val databaseModule = DI.Module("database") {

    bind<AppDatabase>() with singleton { AppDatabase.getInstance(instance()) }

    bind<TickerDao>() with singleton { instance<AppDatabase>().tickerDao() }
}