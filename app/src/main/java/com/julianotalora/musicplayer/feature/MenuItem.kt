package com.julianotalora.musicplayer.feature

import com.julianotalora.musicplayer.R

data class MenuItem(
    val id: ScreensRoute,
    val textId: Int
)

val drawerScreens = listOf(
    MenuItem(ScreensRoute.FAVORITES, R.string.favorites)
)

enum class ScreensRoute {
    TRENDING, FAVORITES, PLAYER
}

enum class TrendingCategories {
    TRENDING, ROCK, HIPHOP, ELECTRO
}