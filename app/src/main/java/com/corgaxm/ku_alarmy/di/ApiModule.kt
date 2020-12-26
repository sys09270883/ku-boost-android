package com.corgaxm.ku_alarmy.di

import com.corgaxm.ku_alarmy.BuildConfig
import com.corgaxm.ku_alarmy.api.CrawlService
import com.corgaxm.ku_alarmy.api.LoginService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun provideHttpLoggingInterceptor() =
    HttpLoggingInterceptor().apply { HttpLoggingInterceptor.Level.BASIC }

fun provideOkHttpClient(
    httpLoggingInterceptor: HttpLoggingInterceptor
) = OkHttpClient.Builder()
    .addInterceptor(httpLoggingInterceptor)
    .connectTimeout(1, TimeUnit.MINUTES)
    .readTimeout(30, TimeUnit.SECONDS)
    .build()

fun provideRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit = Retrofit.Builder()
    .baseUrl(baseUrl).client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create()).build()

val loginApiModule = module {
    fun provideAuthService(retrofit: Retrofit): LoginService =
        retrofit.create(LoginService::class.java)

    single(override = true) { provideHttpLoggingInterceptor() }
    single(override = true) { provideOkHttpClient(get()) }
    single(named("login")) { provideRetrofit(get(), BuildConfig.LOGIN_URL) }
    single { provideAuthService(get(named("login"))) }
}

val crawlApiModule = module {
    fun provideCrawlService(retrofit: Retrofit): CrawlService =
        retrofit.create(CrawlService::class.java)

    single(override = true) { provideHttpLoggingInterceptor() }
    single(override = true) { provideOkHttpClient(get()) }
    single(named("crawl")) { provideRetrofit(get(), "http://2b21de56bb32.ngrok.io/") }
    single { provideCrawlService(get(named("crawl"))) }
}
