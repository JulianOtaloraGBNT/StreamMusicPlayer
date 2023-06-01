package com.julianotalora.musicplayer.feature.favorites.usecase

import com.julianotalora.musicplayer.feature.favorites.repository.GetFavoritesListRepository
import com.julianotalora.musicplayer.feature.trending.state.TrendingElement
import javax.inject.Inject

class GetFavoritesListUseCaseImpl @Inject constructor(
    val repository: GetFavoritesListRepository
): GetFavoritesListUseCase {
    override suspend fun getAllFavoritesTracks(): List<TrendingElement> = repository.getAllFavoritesTracks()

}