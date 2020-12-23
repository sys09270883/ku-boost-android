package com.corgaxm.ku_alarmy.di

import com.corgaxm.ku_alarmy.ui.HomeFragment
import com.corgaxm.ku_alarmy.ui.LoginFragment
import com.corgaxm.ku_alarmy.ui.SplashFragment
import org.koin.dsl.module

val fragmentModule = module {
    factory { SplashFragment() }
    factory { LoginFragment() }
    factory { HomeFragment() }
}