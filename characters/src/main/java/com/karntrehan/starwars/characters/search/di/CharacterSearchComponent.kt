package com.karntrehan.starwars.characters.search.di

import com.karntrehan.starwars.characters.CharacterService
import com.karntrehan.starwars.characters.di.CharacterComponent
import com.karntrehan.starwars.characters.search.CharacterSearchContract
import com.karntrehan.starwars.characters.search.CharacterSearchFragment
import com.karntrehan.starwars.characters.search.CharacterSearchRepo
import com.karntrehan.starwars.characters.search.CharacterSearchVMF
import dagger.Component
import dagger.Module
import dagger.Provides



@CharacterSearchScope
@Component(
    dependencies = [CharacterComponent::class],
    modules = [CharacterSearchComponent.SearchModule::class]
)
interface CharacterSearchComponent {

    fun inject(characterSearchFragment: CharacterSearchFragment)

    @Module
    class SearchModule {

        @CharacterSearchScope
        @Provides
        fun characterSearchVMF(repo: CharacterSearchContract.Repo): CharacterSearchVMF =
            CharacterSearchVMF(repo)

        @CharacterSearchScope
        @Provides
        fun characterSearchContract(service: CharacterService): CharacterSearchContract.Repo =
            CharacterSearchRepo(service)
    }

}