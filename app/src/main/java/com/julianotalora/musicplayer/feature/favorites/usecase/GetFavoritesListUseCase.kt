package com.julianotalora.musicplayer.feature.favorites.usecase

import com.julianotalora.musicplayer.feature.trending.state.TrendingElement

interface GetFavoritesListUseCase {
    suspend fun getAllFavoritesTracks(): List<TrendingElement>
}