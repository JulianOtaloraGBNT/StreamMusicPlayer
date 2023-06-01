package com.julianotalora.musicplayer.feature.trending.state

import com.julianotalora.musicplayer.data.entity.TrackEntity
import com.julianotalora.musicplayer.feature.trending.api.dto.TrackDTO

fun TrackDTO.toTrendingElement() =
    TrendingElement(
        id = this.id,
        title = this.title,
        artist = this.artist.name,
        album = this.album.title,
        preview = this.preview,
        image = this.album.imageCover
    )

fun TrendingElement.toTrackEntity(genre: String) = TrackEntity(
    id = this.id,
    title = this.title,
    artist = this.artist,
    album = this.album,
    image = this.image,
    isFavorite = this.isFavorite,
    genre = genre,
    preview = this.preview
)

fun TrackEntity.toTrendingElement() = TrendingElement(
    id = this.id,
    title = this.title,
    artist = this.artist,
    image = this.image,
    album = this.album,
    isFavorite = this.isFavorite,
    preview = this.preview
)