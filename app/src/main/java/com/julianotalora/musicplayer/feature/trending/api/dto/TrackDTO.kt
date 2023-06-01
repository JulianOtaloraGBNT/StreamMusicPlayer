package com.julianotalora.musicplayer.feature.trending.api.dto

import com.google.gson.annotations.SerializedName

class TrackDTO(
    @field:SerializedName("id") val id: String,
    @field:SerializedName("title") val title: String,
    @field:SerializedName("title_short") val titleShort: String,
    @field:SerializedName("preview") val preview: String,
    @field:SerializedName("artist") val artist: ArtistDTO,
    @field:SerializedName("album") val album: AlbumDTO
)