package com.karntrehan.starwars.characters

import com.karntrehan.starwars.architecture.RemoteResponse
import com.karntrehan.starwars.characters.details.models.CharacterDetailsModel
import com.karntrehan.starwars.characters.details.models.response.FilmResponseModel
import com.karntrehan.starwars.characters.details.models.response.HomeworldResponseModel
import com.karntrehan.starwars.characters.details.models.response.SpeciesResponseModel
import com.karntrehan.starwars.characters.search.models.CharacterSearchModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface CharacterService {

    @GET
    suspend fun getCharacters(@Url url: String): RemoteResponse<List<CharacterSearchModel>>

    @GET("people")
    suspend fun searchCharacter(@Query("search") query: String): RemoteResponse<List<CharacterSearchModel>>

    @GET
    suspend fun getCharacterDetails(@Url url: String): CharacterDetailsModel

    @GET
    suspend fun getCharacterSpecies(@Url url: String?): SpeciesResponseModel

    @GET
    suspend fun getCharacterHomeworld(@Url url: String?): HomeworldResponseModel

    @GET
    suspend fun getCharacterFilms(@Url url: String?): FilmResponseModel

}
