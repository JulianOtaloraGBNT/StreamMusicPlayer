package com.julianotalora.musicplayer.feature.trending.api.dto

import com.google.gson.annotations.SerializedName

class TracksResponseDTO (
    @field:SerializedName("tracks") val tracks: DataResponseDTO
)

class DataResponseDTO(
    @field:SerializedName("data") val data: List<TrackDTO>
)