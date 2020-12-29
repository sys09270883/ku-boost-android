package com.corgaxm.ku_alarmy.di

import com.corgaxm.ku_alarmy.BuildConfig
import com.corgaxm.ku_alarmy.api.AuthService
import com.corgaxm.ku_alarmy.api.GradeService
import com.corgaxm.ku_alarmy.persistence.SettingsManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val apiModule = module {
    fun provideHttpLoggingInterceptor() =
        HttpLoggingInterceptor().apply { HttpLoggingInterceptor.Level.BASIC }

    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ) = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    fun provideCookieClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        settingsManager: SettingsManager
    ) = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor {
            val original = it.request()
            var cookie = ""

            runBlocking {
                cookie = settingsManager.cookieFlow.first()
            }

            val authorized = original.newBuilder()
                .addHeader("Cookie", cookie).build()

            it.proceed(authorized)
        }.build()

    fun provideRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl).client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()

    fun provideCookieRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()

    fun provideAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    fun provideGradeService(retrofit: Retrofit): GradeService =
        retrofit.create(GradeService::class.java)

    single { provideHttpLoggingInterceptor() }
    single(named("default")) { provideOkHttpClient(get()) }
    single(named("cookie")) { provideCookieClient(get(), get()) }
    single(named("default-retrofit")) {
        provideRetrofit(
            get(named("default")),
            BuildConfig.BASE_URL
        )
    }
    single(named("cookie-retrofit")) {
        provideCookieRetrofit(
            get((named("cookie"))),
            BuildConfig.BASE_URL
        )
    }
    single { provideAuthService(get(named("default-retrofit"))) }
    single { provideGradeService(get(named("cookie-retrofit"))) }
}

