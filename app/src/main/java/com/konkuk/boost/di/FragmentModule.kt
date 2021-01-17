package com.konkuk.boost.di

import com.konkuk.boost.ui.GradeFragment
import com.konkuk.boost.ui.LoginFragment
import com.konkuk.boost.ui.SplashFragment
import org.koin.dsl.module

val fragmentModule = module {
    factory { SplashFragment() }
    factory { LoginFragment() }
    factory { GradeFragment() }
}