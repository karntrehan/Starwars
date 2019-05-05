package com.karntrehan.starwars.characters.details

import com.karntrehan.starwars.characters.details.models.CharacterDetailsModel
import io.reactivex.Single

interface CharacterDetailsContract {
    interface Repo {
        fun getCharacterDetails(url: String): Single<CharacterDetailsModel>
    }
}