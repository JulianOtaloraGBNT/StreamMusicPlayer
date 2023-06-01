package com.julianotalora.musicplayer.feature.trending.api

import com.julianotalora.musicplayer.feature.trending.api.dto.TrackDTO
import com.julianotalora.musicplayer.feature.trending.api.dto.TracksResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface TrendingListAPI {
    @GET("chart/{genre}")
    suspend fun getTrendingList(@Path("genre") genre: String = "") : Response<TracksResponseDTO>
}