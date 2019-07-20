package com.karntrehan.starwars.characters.details

import com.karntrehan.starwars.characters.details.models.CharacterDetailsModel
import com.karntrehan.starwars.characters.details.models.response.FilmResponseModel
import com.karntrehan.starwars.characters.details.models.response.HomeworldResponseModel
import com.karntrehan.starwars.characters.details.models.response.SpeciesResponseModel
import io.reactivex.Single

interface CharacterDetailsContract {

    interface Repo {
        /**
         * Get character details [CharacterDetailsModel] from the passed url
         * */
        suspend fun getCharacterDetails(url: String): CharacterDetailsModel

        /**
         * Get specie details [SpeciesResponseModel] from the passed url
         * */
        suspend fun getSpecieDetails(url: String?): SpeciesResponseModel

        /**
         * Get film details [FilmResponseModel] from the passed url
         * */
        suspend fun getFilmDetails(url: String?): FilmResponseModel

        /**
        * Get homeworld details [HomeworldResponseModel] from the passed url
        * */
        suspend fun getHomeworldDetails(url: String?): HomeworldResponseModel
    }

}