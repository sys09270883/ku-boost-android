package com.konkuk.boost.di

import com.konkuk.boost.BuildConfig
import com.konkuk.boost.api.AuthService
import com.konkuk.boost.api.CourseService
import com.konkuk.boost.api.GradeService
import com.konkuk.boost.persistence.PreferenceManager
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
    ) = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .build()

    fun provideCookieClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        preferenceManager: PreferenceManager
    ) = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .addInterceptor {
            val original = it.request()
            val cookie = preferenceManager.cookie

            val authorized = original.newBuilder()
                .addHeader("Cookie", cookie).build()

            it.proceed(authorized)
        }.build()

    fun provideRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl).client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()

    fun provideCookieRetrofit(cookieClient: OkHttpClient, baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl).client(cookieClient)
            .addConverterFactory(GsonConverterFactory.create()).build()

    fun provideAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    fun provideGradeService(retrofit: Retrofit): GradeService =
        retrofit.create(GradeService::class.java)

    fun provideCourseService(retrofit: Retrofit): CourseService =
        retrofit.create(CourseService::class.java)

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
    single { provideCourseService(get(named("cookie-retrofit"))) }
}

