package com.karntrehan.starwars.characters.details.models

import com.google.gson.annotations.SerializedName

data class CharacterFilmModel(
        @SerializedName("title") val title: String?,
        @SerializedName("release_date") val releaseDate: String?,
        @SerializedName("opening_crawl") val openingCrawl: String?)
