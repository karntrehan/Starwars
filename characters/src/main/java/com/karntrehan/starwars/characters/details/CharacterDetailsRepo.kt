package com.karntrehan.starwars.characters.details

import com.karntrehan.starwars.characters.CharacterService
import com.karntrehan.starwars.characters.details.models.CharacterDetailsModel
import com.karntrehan.starwars.characters.details.models.response.FilmResponseModel
import com.karntrehan.starwars.characters.details.models.response.HomeworldResponseModel
import com.karntrehan.starwars.characters.details.models.response.SpeciesResponseModel
import io.reactivex.Single
import kotlinx.coroutines.coroutineScope

class CharacterDetailsRepo(private val service: CharacterService) : CharacterDetailsContract.Repo {

    override suspend fun getCharacterDetails(url: String): CharacterDetailsModel =
        coroutineScope { service.getCharacterDetails(url) }

    override suspend fun getSpecieDetails(url: String?): SpeciesResponseModel =
        coroutineScope { service.getCharacterSpecies(url) }

    override suspend fun getFilmDetails(url: String?): FilmResponseModel =
        coroutineScope { service.getCharacterFilms(url) }

    override suspend fun getHomeworldDetails(url: String?): HomeworldResponseModel =
        coroutineScope { service.getCharacterHomeworld(url) }

}