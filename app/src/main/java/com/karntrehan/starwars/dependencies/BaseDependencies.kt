package com.karntrehan.starwars.dependencies

import android.content.Context
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import com.karntrehan.starwars.BuildConfig
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Base Koin module provider
 * */
object BaseDependencies {

    val networkingModule = module {

        /*Retrofit instance*/
        single { providesRetrofit(get(), get(), get()) }

        /*Okhttp client instance*/
        single { providesOkHttpClient(get()) }

        /*Okhttp cache*/
        single { providesOkhttpCache(get()) }

        /*Retrofit Gson converter*/
        single { GsonConverterFactory.create(get()) }

        /*Retrofit Rx converter */
        single { RxJava2CallAdapterFactory.create() }

        /*Gson*/
        single { GsonBuilder().setExclusionStrategies(exlusionStrategy()).create() }
    }

    private fun providesRetrofit(
            gsonConverterFactory: GsonConverterFactory,
            rxJava2CallAdapterFactory: RxJava2CallAdapterFactory,
            okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.API_BASE)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJava2CallAdapterFactory)
                .client(okHttpClient)
                .build()
    }

    private fun providesOkHttpClient(cache: Cache): OkHttpClient {
        val client = OkHttpClient.Builder()
                .cache(cache)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            client.addInterceptor(logging)
        }

        return client.build()
    }

    private fun providesOkhttpCache(context: Context): Cache {
        val cacheSize = 10 * 1024 * 1024 // 10 MB
        return Cache(context.cacheDir, cacheSize.toLong())
    }

    private fun exlusionStrategy() = object : ExclusionStrategy {
        override fun shouldSkipClass(clazz: Class<*>?) = false
        override fun shouldSkipField(f: FieldAttributes?) =
                f?.getAnnotation(Exclude::class.java) != null //Skip fields annotated with Exclude
    }
}
