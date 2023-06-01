package com.julianotalora.musicplayer.feature.trending.state

class TrendingElement(
    val id: String = String(),
    val title: String = String(),
    val artist: String = String(),
    val album: String = String(),
    val image: String? = String(),
    val preview: String? = String(),
    var isFavorite: Boolean = false
)