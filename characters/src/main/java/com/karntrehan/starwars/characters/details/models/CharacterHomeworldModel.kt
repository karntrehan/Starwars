package com.karntrehan.starwars.characters.details.models

import com.google.gson.annotations.SerializedName

data class CharacterHomeworldModel(
        @SerializedName("name") val name: String?,
        @SerializedName("population") val population: String?)
