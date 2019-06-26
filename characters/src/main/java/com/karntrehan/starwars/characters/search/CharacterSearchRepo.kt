package com.karntrehan.starwars.characters.search

import com.karntrehan.starwars.architecture.RemoteResponse
import com.karntrehan.starwars.characters.CharacterService
import com.karntrehan.starwars.characters.search.models.CharacterSearchModel
import kotlinx.coroutines.coroutineScope

class CharacterSearchRepo(private val service: CharacterService) : CharacterSearchContract.Repo {

    override suspend fun characters(url: String)
            : RemoteResponse<List<CharacterSearchModel>> = coroutineScope { service.getCharacters(url) }

    override suspend fun searchCharacter(query: String)
            : RemoteResponse<List<CharacterSearchModel>> = coroutineScope { service.searchCharacter(query) }

}