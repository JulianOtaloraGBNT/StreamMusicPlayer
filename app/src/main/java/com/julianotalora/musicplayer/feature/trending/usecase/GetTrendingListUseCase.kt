package com.julianotalora.musicplayer.feature.trending.usecase

import com.julianotalora.musicplayer.feature.trending.state.TrendingElement

interface GetTrendingListUseCase {
    suspend fun getTrendingList(genre: String = "") : List<TrendingElement>
    suspend fun setFavorite(id: String, isFavorite: Boolean)
}