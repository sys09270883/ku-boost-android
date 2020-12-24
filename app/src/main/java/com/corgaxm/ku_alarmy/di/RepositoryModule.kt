package com.corgaxm.ku_alarmy.di

import com.corgaxm.ku_alarmy.data.login.LoginRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { LoginRepository(get()) }
}