package com.karntrehan.starwars.architecture

import com.google.gson.annotations.SerializedName

data class RemoteResponse<T>(
        @SerializedName("count") val count: Int,
        @SerializedName("next") val next: String?,
        @SerializedName("previous") val previous: String?,
        @SerializedName("results") val results: T?
)