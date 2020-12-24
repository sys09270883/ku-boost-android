package com.corgaxm.ku_alarmy

import android.app.Application
import com.corgaxm.ku_alarmy.di.fragmentModule
import com.corgaxm.ku_alarmy.di.networkModule
import com.corgaxm.ku_alarmy.di.repositoryModule
import com.corgaxm.ku_alarmy.di.viewModelModule
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
                    fragmentModule,
                    viewModelModule,
                    networkModule,
                    repositoryModule
                )
            )
        }
    }
}