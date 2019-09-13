package com.karntrehan.starwars.dependencies

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Singleton

@Module
class AppModule(val application: Application) {
    @Provides
    @Singleton
    fun providesContext(): Context {
        return application.applicationContext
    }

}