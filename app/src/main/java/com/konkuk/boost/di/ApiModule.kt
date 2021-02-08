package com.konkuk.boost.di

import com.konkuk.boost.BuildConfig
import com.konkuk.boost.api.*
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

    fun provideLibraryService(retrofit: Retrofit): LibraryService =
        retrofit.create(LibraryService::class.java)

    fun provideOzService(retrofit: Retrofit): OzService =
        retrofit.create(OzService::class.java)

//    fun provideOzService(): OzService =
//        object : OzService {
//            override suspend fun getRank(file: File): String {
//                Log.d("yoonseop", "absolute path: ${file.absolutePath}")
//                val url = URL(BuildConfig.OZ_URL)
//                val con: HttpsURLConnection = url.openConnection() as HttpsURLConnection
//                con.doInput = true
//                con.doOutput = true
//                con.requestMethod = "POST"
//
//                val contentBytes = file.readBytes()
//                val contentLength = contentBytes.size
//
//                con.setRequestProperty("Content-Type", "application/octet-stream")
//                con.setRequestProperty("Content-Length", contentLength.toString())
//                con.setHostnameVerifier { hostname, session ->
//                    true
//                }
//                con.connect()
//
//                val out = file.readBytes()
//                val outputStream: OutputStream = con.outputStream
//                outputStream.write(out, 0, out.size)
//                outputStream.flush()
//
//                val inputStream = BufferedReader(InputStreamReader(con.inputStream))
//                val content = StringBuffer()
//
//                var inputLine: String
//                while (inputStream.readLine().also { inputLine = it } != null) {
//                    content.append(inputLine)
//                }
//
//                inputStream.close()
//                con.disconnect()
//
//                return content.toString()
//            }
//        }

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
    single { provideAuthService(get(named("default-retrofit"))) }
    single { provideGradeService(get(named("cookie-retrofit"))) }
    single { provideCourseService(get(named("cookie-retrofit"))) }
    single { provideLibraryService(get(named("library-retrofit"))) }
    single { provideOzService(get(named("oz-retrofit"))) }
}

