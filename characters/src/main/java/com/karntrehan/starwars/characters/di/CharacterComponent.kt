package com.karntrehan.starwars.characters.di

import com.karntrehan.starwars.characters.CharacterService
import com.karntrehan.starwars.characters.search.CharacterSearchFragment
import com.karntrehan.starwars.dependencies.CoreComponent
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.create

@CharacterScope
@Component(
    dependencies = [CoreComponent::class],
    modules = [CharacterComponent.CharacterModule::class]
)
interface CharacterComponent {

    fun characterService(): CharacterService

    @Module
    class CharacterModule {

        @CharacterScope
        @Provides
        fun characterService(retrofit: Retrofit): CharacterService = retrofit.create()
    }
}