package com.julianotalora.musicplayer.data

import com.julianotalora.musicplayer.feature.trending.state.TrendingElement
import kotlinx.coroutines.flow.Flow

interface DataStoreManager {
    suspend fun saveFavoritesToPreferencesStore(id: String, isFavorite: Boolean)
    suspend fun isFavorite(id: String) : Flow<Boolean>
}