package com.julianotalora.musicplayer.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.julianotalora.musicplayer.data.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(track: TrackEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(tracks: List<TrackEntity>)

    @Query("UPDATE tracks SET isFavorite = :isFavorite WHERE id = :id")
    fun updateFavorite(id: String, isFavorite: Boolean): Int

    @Update
    fun update(track: TrackEntity)

    @Query("select * from tracks where genre = :genre order by id desc")
    fun getTracksByGenre(genre: String): List<TrackEntity>

    @Query("select * from tracks where isFavorite = :favorite order by id desc")
    fun getFavoriteTracks(favorite: Boolean = true): Flow<List<TrackEntity>>
}