package com.karntrehan.starwars.characters.details

import com.karntrehan.starwars.characters.CharacterService
import com.karntrehan.starwars.characters.details.models.CharacterDetailsModel
import com.karntrehan.starwars.characters.details.models.CharacterFilmModel
import com.karntrehan.starwars.characters.details.models.CharacterSpeciesModel
import io.reactivex.Single

class CharacterDetailsRepo(private val service: CharacterService) : CharacterDetailsContract.Repo {
    override fun getCharacterDetails(url: String): Single<CharacterDetailsModel> = service.getCharacterDetails(url)

    override fun getSpecieDetails(url: String): Single<CharacterSpeciesModel> = service.getCharacterSpecies(url)

    override fun getFilmDetails(url: String): Single<CharacterFilmModel> = service.getCharacterFilms(url)

}