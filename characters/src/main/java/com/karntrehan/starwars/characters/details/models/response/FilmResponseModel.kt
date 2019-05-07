package com.karntrehan.starwars.characters.details.models.response

import com.google.gson.annotations.SerializedName

data class FilmResponseModel(
        @SerializedName("title") val title: String,
        @SerializedName("release_date") val releaseDate: String,
        @SerializedName("opening_crawl") val openingCrawl: String)
