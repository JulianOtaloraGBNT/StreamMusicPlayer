package com.julianotalora.musicplayer.feature.favorites.repository

import com.julianotalora.musicplayer.feature.trending.state.TrendingElement
import kotlinx.coroutines.flow.Flow

interface GetFavoritesListRepository {
    suspend fun getAllFavoritesTracks(): List<TrendingElement>
}