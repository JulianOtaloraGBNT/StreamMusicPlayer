package com.julianotalora.musicplayer.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.julianotalora.musicplayer.data.dao.TrackDao
import com.julianotalora.musicplayer.data.entity.TrackEntity

@Database(entities = [TrackEntity::class], version = 1)
abstract class PlayerDatabase: RoomDatabase() {
    abstract fun trackDao(): TrackDao
}