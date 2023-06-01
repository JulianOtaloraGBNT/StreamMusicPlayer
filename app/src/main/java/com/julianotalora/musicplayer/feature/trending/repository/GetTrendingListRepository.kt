package com.julianotalora.musicplayer.feature.trending.repository

import com.julianotalora.musicplayer.feature.trending.state.TrendingElement

interface GetTrendingListRepository {
    suspend fun getTrendingList(genre: String = ""): List<TrendingElement>
    suspend fun setFavorite(id: String, isFavorite: Boolean)
    suspend fun insertAllTracks(genre: String, list: List<TrendingElement>): List<TrendingElement>
    suspend fun getLocalTracksByGenre(genre: String): List<TrendingElement>
    suspend fun isFavorite(id: String): Boolean
}