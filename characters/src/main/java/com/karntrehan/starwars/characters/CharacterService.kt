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
    fun getCharacters(@Url url: String): Single<RemoteResponse<List<CharacterSearchModel>>>

    @GET("people")
    fun searchCharacter(@Query("search") query: String): Single<RemoteResponse<List<CharacterSearchModel>>>

    @GET
    fun getCharacterDetails(@Url url: String): Single<CharacterDetailsModel>

    @GET
    fun getCharacterSpecies(@Url url: String?): Single<SpeciesResponseModel>

    @GET
    fun getCharacterHomeworld(@Url url: String?): Single<HomeworldResponseModel?>

    @GET
    fun getCharacterFilms(@Url url: String?): Single<FilmResponseModel>

}
