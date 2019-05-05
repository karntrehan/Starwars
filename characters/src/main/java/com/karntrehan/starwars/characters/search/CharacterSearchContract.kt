package com.karntrehan.starwars.characters.search

import com.karntrehan.starwars.architecture.RemoteResponse
import com.karntrehan.starwars.characters.search.models.CharacterResponseModel
import io.reactivex.Single

interface CharacterSearchContract {
    interface Repo {
        fun characters(url: String): Single<RemoteResponse<List<CharacterResponseModel>>>

        fun searchCharacter(query: String): Single<RemoteResponse<List<CharacterResponseModel>>>
    }
}