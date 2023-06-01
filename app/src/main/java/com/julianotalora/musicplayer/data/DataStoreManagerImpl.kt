package com.julianotalora.musicplayer.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.julianotalora.musicplayer.common.TRACK_ALBUM
import com.julianotalora.musicplayer.common.TRACK_ARTIST
import com.julianotalora.musicplayer.common.TRACK_IMAGE
import com.julianotalora.musicplayer.common.TRACK_IS_FAVORITE
import com.julianotalora.musicplayer.common.TRACK_TITLE
import com.julianotalora.musicplayer.feature.trending.state.TrendingElement
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

const val PREF_FAVORITES = "favorite_tracks"

var PREF_TRACK_ID = stringPreferencesKey("")
var PREF_TRACK_ARTIST = stringPreferencesKey(TRACK_ARTIST)
var PREF_TRACK_IMAGE = stringPreferencesKey(TRACK_IMAGE)
var PREF_TRACK_TITLE = stringPreferencesKey(TRACK_TITLE)
var PREF_TRACK_ALBUM = stringPreferencesKey(TRACK_ALBUM)
var PREF_TRACK_IS_FAVORITE = booleanPreferencesKey(TRACK_IS_FAVORITE)

class DataStoreManagerImpl @Inject constructor(
    @ApplicationContext val context: Context
) : DataStoreManager {

    val Context.favoriteTracksDataStore: DataStore<Preferences> by preferencesDataStore(
        name = PREF_FAVORITES
    )

    override suspend fun saveFavoritesToPreferencesStore(id: String, isFavorite: Boolean) {
        setVars(id)
        context.favoriteTracksDataStore.edit {
            it[PREF_TRACK_IS_FAVORITE] = isFavorite
        }
    }

    override suspend fun isFavorite(id: String) : Flow<Boolean> {
        setVars(id)
        return context.favoriteTracksDataStore.data.map { it[PREF_TRACK_IS_FAVORITE] ?: false }
    }

    fun setVars(id: String){
        PREF_TRACK_IS_FAVORITE = booleanPreferencesKey(id+"_"+TRACK_IS_FAVORITE)
    }

}