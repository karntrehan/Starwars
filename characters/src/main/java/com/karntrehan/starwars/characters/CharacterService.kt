package com.karntrehan.starwars.characters

import com.karntrehan.starwars.architecture.RemoteResponse
import com.karntrehan.starwars.characters.details.models.CharacterDetailsModel
import com.karntrehan.starwars.characters.details.models.CharacterFilmModel
import com.karntrehan.starwars.characters.details.models.CharacterHomeworldModel
import com.karntrehan.starwars.characters.details.models.CharacterSpeciesModel
import com.karntrehan.starwars.characters.search.models.CharacterSearchModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface CharacterService {

    @GET
    fun getCharacters(@Url url: String): Single<RemoteResponse<List<CharacterSearchModel>>>

    @GET("people")
    fun searchCharacter(@Query("search") query: String): Single<RemoteResponse<List<CharacterSearchModel>>>

    @GET
    fun getCharacterDetails(@Url url: String): Single<CharacterDetailsModel>

    @GET
    fun getCharacterSpecies(@Url url: String): Single<CharacterSpeciesModel>

    @GET
    fun getCharacterHomeworld(@Url url: String): Single<CharacterHomeworldModel>

    @GET
    fun getCharacterFilms(@Url url: String): Single<CharacterFilmModel>

}
