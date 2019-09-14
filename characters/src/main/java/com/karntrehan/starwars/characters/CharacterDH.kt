package com.karntrehan.starwars.characters

import com.karntrehan.starwars.StartWarsApplication
import com.karntrehan.starwars.characters.details.di.CharacterDetailsComponent
import com.karntrehan.starwars.characters.details.di.DaggerCharacterDetailsComponent
import com.karntrehan.starwars.characters.di.CharacterComponent
import com.karntrehan.starwars.characters.di.DaggerCharacterComponent
import com.karntrehan.starwars.characters.search.di.CharacterSearchComponent
import com.karntrehan.starwars.characters.search.di.DaggerCharacterSearchComponent

/*Dependency holder for Character module*/
object CharacterDH {

    var characterComponent: CharacterComponent? = null

    fun init(application: StartWarsApplication) {
        if (characterComponent == null)
            characterComponent = DaggerCharacterComponent
                .builder()
                .coreComponent(application.coreComponent)
                .build()
    }

    //Search
    val searchComponent: CharacterSearchComponent by lazy { initSearch() }

    private fun initSearch(): CharacterSearchComponent =
        DaggerCharacterSearchComponent.builder().characterComponent(characterComponent).build()

    //Details
    val detailsComponent: CharacterDetailsComponent by lazy { initDetails() }

    private fun initDetails(): CharacterDetailsComponent =
        DaggerCharacterDetailsComponent.builder().characterComponent(characterComponent).build()

}