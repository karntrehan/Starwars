package com.karntrehan.starwars.characters.search.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CharacterSearchModel(
        @SerializedName("url") val url: String?,
        @SerializedName("name") val name: String?,
        @SerializedName("birth_year") val birthYear: String?
) : Parcelable
