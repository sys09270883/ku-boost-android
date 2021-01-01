package com.konkuk.boost.di

import com.konkuk.boost.ui.HomeFragment
import com.konkuk.boost.ui.LoginFragment
import com.konkuk.boost.ui.SplashFragment
import org.koin.dsl.module

val fragmentModule = module {
    factory { SplashFragment() }
    factory { LoginFragment() }
    factory { HomeFragment() }
}