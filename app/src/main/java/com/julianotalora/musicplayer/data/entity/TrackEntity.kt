package com.julianotalora.musicplayer.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    val title: String = String(),
    val artist: String = String(),
    val album: String = String(),
    val image: String? = String(),
    val isFavorite: Boolean = false,
    val genre: String = String(),
    val preview: String? = String()
)