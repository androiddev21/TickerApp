package com.example.tickerapp

import android.app.Application
import com.example.tickerapp.di.databaseModule
import com.example.tickerapp.di.repositoryModule
import com.example.tickerapp.di.retrofitModule
import com.example.tickerapp.di.viewModelModule
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.androidXModule

class App : Application(), DIAware {

    override val di: DI = DI.lazy {
        import(androidXModule(this@App))
        import(databaseModule)
        import(retrofitModule)
        import(repositoryModule)
        import(viewModelModule)
    }
}