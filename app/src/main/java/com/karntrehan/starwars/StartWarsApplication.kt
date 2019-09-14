package com.karntrehan.starwars

import android.app.Application
import com.karntrehan.starwars.dependencies.AppModule
import com.karntrehan.starwars.dependencies.CoreComponent
import com.karntrehan.starwars.dependencies.DaggerCoreComponent

class StartWarsApplication : Application() {

    val coreComponent: CoreComponent by lazy {
        DaggerCoreComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}