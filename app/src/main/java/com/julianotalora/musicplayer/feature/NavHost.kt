package com.julianotalora.musicplayer.feature

import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.julianotalora.musicplayer.R
import com.julianotalora.musicplayer.feature.favorites.view.FavoritesRoute
import com.julianotalora.musicplayer.feature.trending.TrendingRoute
import com.julianotalora.musicplayer.feature.trending.state.TrendingElement
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationHost(
    navController: NavHostController,
    drawerState: DrawerState,
    coroutineScope: CoroutineScope,
    navigateToPlayer: (list: String, track: TrendingElement) -> Unit
) {
    //val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ScreensRoute.TRENDING.name) {
        composable(ScreensRoute.TRENDING.name) {
            TrendingRoute(
                drawerState = drawerState,
                scope = coroutineScope,
                navigateToPlayer = navigateToPlayer
            )
        }

        composable(ScreensRoute.FAVORITES.name) {
            FavoritesRoute { navController.popBackStack() }
        }

        composable(ScreensRoute.PLAYER.name) {
            ScreenContent(titleId = R.string.player)
        }

    }
}