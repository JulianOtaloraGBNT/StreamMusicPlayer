package com.julianotalora.musicplayer.feature.trending.repository

import com.julianotalora.musicplayer.common.PlayerException
import com.julianotalora.musicplayer.data.DataStoreManager
import com.julianotalora.musicplayer.data.dao.TrackDao
import com.julianotalora.musicplayer.feature.trending.api.TrendingListAPI
import com.julianotalora.musicplayer.feature.trending.state.TrendingElement
import com.julianotalora.musicplayer.feature.trending.state.toTrackEntity
import com.julianotalora.musicplayer.feature.trending.state.toTrendingElement
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class GetTrendingListRepositoryImpl @Inject constructor(
    val trendingListAPI: TrendingListAPI,
    val dataStoreManager: DataStoreManager,
    val trackDao: TrackDao
) : GetTrendingListRepository {

    override suspend fun getTrendingList(genre: String) : List<TrendingElement> {
        val response = trendingListAPI.getTrendingList(genre)

        if (!response.isSuccessful) {
            throw PlayerException("Unsuccessful response and no error detail.")
        }
        return response.body()?.tracks?.data?.map { it.toTrendingElement() } ?: throw PlayerException("Unsuccessful response and no error detail.")
        /*
        return response.body()?.tracks?.data?.map {
            it.toTrendingElement().apply { this.isFavorite = dataStoreManager.isFavorite(this.id).first() }
        }.also { list ->
            list?.map { it.toTrackEntity(genre) }?.let { trackDao.insertAll(it) }
        } ?: throw PlayerException("Unsuccessful response and no error detail.")
        */
    }

    override suspend fun setFavorite(id: String, isFavorite: Boolean) {
        dataStoreManager.saveFavoritesToPreferencesStore(id, isFavorite)
        trackDao.updateFavorite(id, isFavorite)
    }
    override suspend fun insertAllTracks(genre: String, list: List<TrendingElement>): List<TrendingElement> {
        trackDao.insertAll(list.map { it.toTrackEntity(genre) })
        return trackDao.getTracksByGenre(genre).map {  it.toTrendingElement() }
    }

    override suspend fun getLocalTracksByGenre(genre: String): List<TrendingElement> {
        return trackDao.getTracksByGenre(genre).map {  it.toTrendingElement() }
    }

    override suspend fun isFavorite(id: String) = dataStoreManager.isFavorite(id).first()

}