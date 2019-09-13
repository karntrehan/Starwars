package com.karntrehan.starwars.dependencies

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.karntrehan.starwars.StartWarsApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import retrofit2.Retrofit
import javax.inject.Singleton
import dagger.Binds



@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, /*AppModule::class,*/ NetworkModule::class, StorageModule::class])
interface CoreComponent : AndroidInjector<StartWarsApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: StartWarsApplication): Builder

        fun build(): CoreComponent
    }

    fun retrofit(): Retrofit

    fun gson(): Gson

    fun sharedPreferences(): SharedPreferences
}