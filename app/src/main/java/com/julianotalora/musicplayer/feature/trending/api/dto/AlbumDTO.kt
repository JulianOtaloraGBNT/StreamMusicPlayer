package com.julianotalora.musicplayer.feature.trending.api.dto

import com.google.gson.annotations.SerializedName

class AlbumDTO(
    @field:SerializedName("id") val id: String,
    @field:SerializedName("title") val title: String,
    @field:SerializedName("cover_medium") val imageCover: String,
)