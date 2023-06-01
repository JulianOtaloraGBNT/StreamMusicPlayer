package com.julianotalora.musicplayer.feature.favorites.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.julianotalora.musicplayer.R
import com.julianotalora.musicplayer.common.ProgressPopup
import com.julianotalora.musicplayer.feature.trending.state.TrendingElement

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesRoute(
    viewModel: FavoritesViewModel = hiltViewModel(),
    onBackClick: () -> Boolean
) {

    val trendingList = viewModel.favoritesListFlow.collectAsState(initial = emptyList())
    val loadingState = viewModel.loadingFlow.collectAsState(initial = false)

    FavoritesScreen(trendingList, loadingState, onBackClick)

}

@ExperimentalMaterial3Api
@Composable
fun FavoritesScreen(
    favoritesList: State<List<TrendingElement>>,
    loadingState: State<Boolean>,
    onBackClick: () -> Boolean
) {
    Column(
        modifier = Modifier
            .background(color = Color.Gray)
            .fillMaxSize()
    ){
        if(loadingState.value){
            ProgressPopup(loadingState = loadingState)
        }

        Scaffold(
            topBar = {
                Column(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .padding(horizontal = 8.dp)
                ) {
                    FavoritesTopBar(onBackClick)
                }
            },
            content = {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {



                    FavoritesGridList(favoritesList)

                }
            }
        )
    }
}


@Composable
fun FavoritesGridList(favoritesList: State<List<TrendingElement>>) {
    LazyVerticalGrid(columns = GridCells.Adaptive(150.dp), contentPadding = PaddingValues(4.dp)){
        items(favoritesList.value){
            FavoritesGridItem(it)
        }
    }
}

@Composable
fun FavoritesGridItem(
    element: TrendingElement
){
    Column(
        modifier = Modifier
            .wrapContentSize()
    ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clip(RoundedCornerShape(12.dp)),
                    fallback = painterResource(id = R.drawable.ic_launcher_foreground),
                    error = painterResource(id = R.drawable.ic_launcher_background),
                    model = element.image,
                    contentDescription = null,
                    contentScale = ContentScale.Fit
                )
            }

        Text(text = element.title, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Row(){
            Image(modifier = Modifier.size(20.dp),painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "")

            Text(text = element.artist+" - "+element.album, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
fun FavoritesTopBar(onBackClick: () -> Boolean) {
    Row(
        modifier = Modifier
            .height(75.dp)
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .background(color = Color.White)

    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(60.dp)
                .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                .clickable(onClick = {
                    onBackClick.invoke()
                }),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(id = android.R.drawable.ic_media_previous), contentDescription = "")
        }

        Divider(
            modifier = Modifier.width(10.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {}
    }
}