package com.konkuk.boost

import android.app.Application
import com.konkuk.boost.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            androidFileProperties()
            modules(
                listOf(
                    viewModelModule,
                    apiModule,
                    repositoryModule,
                    persistenceModule,
                    utilityModule,
                )
            )
        }
    }
}