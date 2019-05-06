package com.karntrehan.starwars.characters.details.models

import com.google.gson.annotations.SerializedName

data class CharacterSpeciesModel(
        @SerializedName("name") val name: String?,
        @SerializedName("language") val language: String?,
        @SerializedName("homeworld") val homeworldUrl: String?)
