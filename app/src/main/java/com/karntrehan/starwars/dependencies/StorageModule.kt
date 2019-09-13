package com.karntrehan.starwars.dependencies

import android.content.Context
import android.content.OperationApplicationException
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.karntrehan.starwars.StartWarsApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule {
    @Provides
    @Singleton
    fun providesSharedPreferences(application: StartWarsApplication): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application.applicationContext)
    }
}