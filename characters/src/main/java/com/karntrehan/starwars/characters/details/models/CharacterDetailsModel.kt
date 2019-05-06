package com.karntrehan.starwars.characters.details.models

import com.google.gson.annotations.SerializedName
import com.karntrehan.starwars.dependencies.Exclude

typealias Specie = String
typealias SpecieLanguage = String

typealias Homeworld = String
typealias HomeworldPopulation = String

typealias Title = String?
typealias ReleaseDate = String?
typealias OpeningCrawl = String?

data class CharacterDetailsModel(
        @SerializedName("url") val url: String?,
        @SerializedName("name") val name: String?,
        @SerializedName("birth_year") val birthYear: String?,
        @SerializedName("height") val heightCentimeters: String?,
        @Exclude val heightFt: String?,
        @Exclude val heightInches: String?,

        @SerializedName("species") val species: List<String>?,
        @Exclude val specieDetails: List<SpeciesDetails>?,

        @SerializedName("films") val films: List<String>?,
        @Exclude val filmDetails: List<Triple<Title, ReleaseDate, OpeningCrawl>>?
) {
    class SpeciesDetails(val species: String,
                         val language: String,
                         val homeworld: String,
                         val population: String)
}
