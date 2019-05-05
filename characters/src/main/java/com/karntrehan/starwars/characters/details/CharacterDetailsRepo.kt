package com.karntrehan.starwars.characters.details

import com.karntrehan.starwars.characters.CharacterService
import com.karntrehan.starwars.characters.details.models.CharacterDetailsModel
import io.reactivex.Single

class CharacterDetailsRepo(private val service: CharacterService) : CharacterDetailsContract.Repo {

    override fun getCharacterDetails(url: String): Single<CharacterDetailsModel> = service.getCharacterDetails(url)

}