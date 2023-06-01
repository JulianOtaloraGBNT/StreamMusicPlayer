package com.julianotalora.musicplayer.feature.trending.api.dto

import com.google.gson.annotations.SerializedName

class ArtistDTO(
    @field:SerializedName("id") val id: String,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("picture_medium") val picture: String
)