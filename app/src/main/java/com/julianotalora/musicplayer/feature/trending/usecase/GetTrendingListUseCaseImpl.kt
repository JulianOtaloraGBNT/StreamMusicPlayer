package com.julianotalora.musicplayer.feature.trending.usecase

import com.julianotalora.musicplayer.feature.trending.repository.GetTrendingListRepository
import com.julianotalora.musicplayer.feature.trending.state.TrendingElement
import javax.inject.Inject

class GetTrendingListUseCaseImpl @Inject constructor(
    val getTrendingListRepository: GetTrendingListRepository
) : GetTrendingListUseCase {
    override suspend fun getTrendingList(genre: String): List<TrendingElement> {
        val remoteList = getTrendingListRepository.getTrendingList(genre)
            .map { it.apply { it.isFavorite = getTrendingListRepository.isFavorite(it.id) } }
        return getTrendingListRepository.insertAllTracks(genre = genre, remoteList)
    }

    override suspend fun setFavorite(id: String, isFavorite: Boolean) = getTrendingListRepository.setFavorite(id, isFavorite)
}