package com.karntrehan.starwars

import android.app.Application
import com.karntrehan.starwars.dependencies.BaseDependencies
import com.karntrehan.starwars.dependencies.DaggerCoreComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class StartWarsApplication : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        initDI()
    }


    override fun applicationInjector() = DaggerCoreComponent.builder()
        .application(this)
        .build()

    private fun initDI() {
        startKoin {
            // Android context
            androidContext(this@StartWarsApplication)
            // modules
            modules(BaseDependencies.networkingModule)
        }
    }
}