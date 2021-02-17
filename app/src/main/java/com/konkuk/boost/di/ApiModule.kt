package com.konkuk.boost.di

import com.konkuk.boost.BuildConfig
import com.konkuk.boost.api.*
import com.konkuk.boost.persistence.pref.PreferenceManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val apiModule = module {
    fun provideHttpLoggingInterceptor() =
        HttpLoggingInterceptor().apply { HttpLoggingInterceptor.Level.BASIC }

    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ) = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()

    fun provideCookieClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        preferenceManager: PreferenceManager
    ) = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).addInterceptor {
        val original = it.request()
        val cookie = preferenceManager.cookie

        val authorized = original.newBuilder()
            .addHeader("Cookie", cookie).build()

        it.proceed(authorized)
    }.build()

    fun provideRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit =
        Retrofit.Builder().baseUrl(baseUrl).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()).build()

    fun provideCookieRetrofit(cookieClient: OkHttpClient, baseUrl: String): Retrofit =
        Retrofit.Builder().baseUrl(baseUrl).client(cookieClient)
            .addConverterFactory(GsonConverterFactory.create()).build()

    fun provideKuisService(retrofit: Retrofit): KuisService =
        retrofit.create(KuisService::class.java)

    fun provideAuthorizedKuisService(retrofit: Retrofit): AuthorizedKuisService =
        retrofit.create(AuthorizedKuisService::class.java)

    fun provideLibraryService(retrofit: Retrofit): LibraryService =
        retrofit.create(LibraryService::class.java)

    fun provideOzService(retrofit: Retrofit): OzService =
        retrofit.create(OzService::class.java)

    fun provideKupisService(retrofit: Retrofit): KupisService =
        retrofit.create(KupisService::class.java)

    single { provideHttpLoggingInterceptor() }

    single(named("default")) { provideOkHttpClient(get()) }
    single(named("cookie")) { provideCookieClient(get(), get()) }

    single(named("default-retrofit")) {
        provideRetrofit(
            get(named("default")),
            BuildConfig.KUIS_URL
        )
    }
    single(named("library-retrofit")) {
        provideRetrofit(
            get(named("default")),
            BuildConfig.LIB_URL
        )
    }
    single(named("cookie-retrofit")) {
        provideCookieRetrofit(
            get((named("cookie"))),
            BuildConfig.KUIS_URL
        )
    }
    single(named("oz-retrofit")) {
        provideRetrofit(
            get(named("default")),
            BuildConfig.OZ_URL
        )
    }
    single(named("kupis-retrofit")) {
        provideRetrofit(
            get(named("default")),
            BuildConfig.KUPIS_URL
        )
    }

    single { provideKuisService(get(named("default-retrofit"))) }
    single { provideAuthorizedKuisService(get(named("cookie-retrofit"))) }
    single { provideLibraryService(get(named("library-retrofit"))) }
    single { provideOzService(get(named("oz-retrofit"))) }
    single { provideKupisService(get(named("kupis-retrofit"))) }
}

