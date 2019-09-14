package com.karntrehan.starwars.characters.details.di

import com.karntrehan.starwars.characters.CharacterService
import com.karntrehan.starwars.characters.details.CharacterDetailsContract
import com.karntrehan.starwars.characters.details.CharacterDetailsFragment
import com.karntrehan.starwars.characters.details.CharacterDetailsRepo
import com.karntrehan.starwars.characters.details.CharacterDetailsVMF
import com.karntrehan.starwars.characters.di.CharacterComponent
import dagger.Component
import dagger.Module
import dagger.Provides


@CharacterDetailsScope
@Component(
    dependencies = [CharacterComponent::class],
    modules = [CharacterDetailsComponent.DetailsModule::class]
)
interface CharacterDetailsComponent {

    fun inject(characterDetailsFragment: CharacterDetailsFragment)

    @Module
    class DetailsModule {

        @CharacterDetailsScope
        @Provides
        fun characterDetailsVMF(repo: CharacterDetailsContract.Repo): CharacterDetailsVMF =
            CharacterDetailsVMF(repo)

        @CharacterDetailsScope
        @Provides
        fun characterDetailsContract(service: CharacterService): CharacterDetailsContract.Repo =
            CharacterDetailsRepo(service)
    }

}