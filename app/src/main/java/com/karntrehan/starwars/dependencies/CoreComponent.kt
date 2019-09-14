package com.karntrehan.starwars.dependencies

import android.content.SharedPreferences
import com.google.gson.Gson
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class,NetworkModule::class, StorageModule::class])
interface CoreComponent {

    fun retrofit(): Retrofit

    fun gson(): Gson

    fun sharedPreferences(): SharedPreferences
}