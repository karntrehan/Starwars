package com.karntrehan.starwars.characters.details

import com.karntrehan.starwars.characters.details.models.CharacterDetailsModel
import com.karntrehan.starwars.characters.details.models.CharacterFilmModel
import com.karntrehan.starwars.characters.details.models.CharacterSpeciesModel
import io.reactivex.Single

interface CharacterDetailsContract {
    interface Repo {
        fun getCharacterDetails(url: String): Single<CharacterDetailsModel>
        fun getSpecieDetails(url: String): Single<CharacterSpeciesModel>
        fun getFilmDetails(url: String): Single<CharacterFilmModel>
    }
}