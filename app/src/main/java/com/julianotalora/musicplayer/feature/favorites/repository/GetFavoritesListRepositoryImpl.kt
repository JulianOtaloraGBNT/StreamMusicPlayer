package com.julianotalora.musicplayer.feature.favorites.repository

import com.julianotalora.musicplayer.data.dao.TrackDao
import com.julianotalora.musicplayer.feature.trending.state.toTrendingElement
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class GetFavoritesListRepositoryImpl @Inject constructor(
    val trackDao: TrackDao
): GetFavoritesListRepository {
    override suspend fun getAllFavoritesTracks() = trackDao.getFavoriteTracks().first().map { it.toTrendingElement() }
}