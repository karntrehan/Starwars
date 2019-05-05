package com.karntrehan.starwars.characters.search

import com.karntrehan.starwars.architecture.RemoteResponse
import com.karntrehan.starwars.characters.CharacterService
import com.karntrehan.starwars.characters.search.models.CharacterResponseModel
import io.reactivex.Single

class CharacterSearchRepo(private val service: CharacterService) : CharacterSearchContract.Repo {

    override fun characters(url: String)
            : Single<RemoteResponse<List<CharacterResponseModel>>> = service.getCharacters(url)

    override fun searchCharacter(query: String)
            : Single<RemoteResponse<List<CharacterResponseModel>>> = service.searchCharacter(query)

}