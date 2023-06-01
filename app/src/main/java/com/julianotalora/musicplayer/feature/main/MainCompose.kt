package com.julianotalora.musicplayer.feature.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.julianotalora.musicplayer.common.Destination
import com.julianotalora.musicplayer.common.PLAYING_LIST
import com.julianotalora.musicplayer.common.TRACK_ID
import com.julianotalora.musicplayer.feature.DrawerBody
import com.julianotalora.musicplayer.feature.NavigationHost
import com.julianotalora.musicplayer.feature.ScreensRoute
import com.julianotalora.musicplayer.feature.drawerScreens


@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainCompose(
    navController: NavHostController = rememberNavController(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    //vm: MainViewModel = hiltViewModel()
){

    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {

            ModalDrawerSheet(
                modifier = Modifier.width(250.dp)
            ) {
                DrawerBody(
                    menuItems = drawerScreens,
                    drawerState = drawerState,
                    scope
                ) {
                    navController.navigate(it.id.name) {
                        popUpTo(navController.graph.startDestinationId)
                    }
                }
            }
        }
    ) {
        NavigationHost(
            navController = navController,
            drawerState = drawerState,
            coroutineScope = scope,
            navigateToPlayer = { list, element ->
                navController.navigate(
                    Destination(ScreensRoute.PLAYER.name).destinationWithArguments(
                        PLAYING_LIST to list,
                        TRACK_ID to TRACK_ID
                    )
                )
            }
        )
    }
}