@file:OptIn(ExperimentalMaterial3Api::class)

package com.julianotalora.musicplayer.feature.trending

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.julianotalora.musicplayer.R
import com.julianotalora.musicplayer.common.PLAYING_LIST
import com.julianotalora.musicplayer.common.ProgressPopup
import com.julianotalora.musicplayer.common.TRACK_ALBUM
import com.julianotalora.musicplayer.common.TRACK_ARTIST
import com.julianotalora.musicplayer.common.TRACK_ID
import com.julianotalora.musicplayer.common.TRACK_IMAGE
import com.julianotalora.musicplayer.common.TRACK_IS_FAVORITE
import com.julianotalora.musicplayer.common.TRACK_TITLE
import com.julianotalora.musicplayer.feature.TrendingCategories
import com.julianotalora.musicplayer.feature.TrendingCategories.*
import com.julianotalora.musicplayer.feature.player.PlayerActivity
import com.julianotalora.musicplayer.feature.trending.state.TrendingElement
import com.julianotalora.musicplayer.feature.trending.view.TrendingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendingRoute(
    drawerState: DrawerState,
    scope: CoroutineScope,
    viewModel: TrendingViewModel = hiltViewModel(),
    navigateToPlayer: (list: String, track: TrendingElement) -> Unit
){
    val trendingList = viewModel.trendingListFlow.collectAsState(initial = emptyList())
    val rockList = viewModel.rockListFlow.collectAsState(initial = emptyList())
    val hiphopList = viewModel.hipHopListFlow.collectAsState(initial = emptyList())
    val electroList = viewModel.electroListFlow.collectAsState(initial = emptyList())


    val loadingState = viewModel.loadingFlow.collectAsState(initial = false)
    val loadTabContentList = viewModel::getTabList
    val addOrRemoveFavorite = viewModel::addOrRemoveFavorite
    TrendingScreen(drawerState, scope, trendingList, loadingState, loadTabContentList, rockList, hiphopList, electroList, addOrRemoveFavorite, navigateToPlayer)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendingScreen(
    drawerState: DrawerState,
    scope: CoroutineScope,
    trendingList: State<List<TrendingElement>>,
    loadingState: State<Boolean>,
    loadTabContentList: (category: Int) -> Unit,
    rockList: State<List<TrendingElement>>,
    hiphopList: State<List<TrendingElement>>,
    electroList: State<List<TrendingElement>>,
    addOrRemoveFavorite: (favorite: Boolean, id: String) -> Unit,
    navigateToPlayer: (list: String, track: TrendingElement) -> Unit
) {

    if(loadingState.value){
        ProgressPopup(loadingState = loadingState)
    }
    Column(
        modifier = Modifier
            .background(color = Color.Gray)
            .fillMaxSize()
    ){

        Scaffold(
            topBar = {
                Column(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .padding(horizontal = 8.dp)
                ) {
                    TopBar(drawerState, scope)
                }
            },
            content = {
                Column(
                    modifier = Modifier
                        .padding(it)
                        .padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    TrendingElementList(trendingList, navigateToPlayer)
                
                    TabScreens(trendingList, loadTabContentList, rockList, hiphopList, electroList, addOrRemoveFavorite)
                }
            }
        )
    }
}

@Composable
fun TrendingElementList(
    trendingList: State<List<TrendingElement>>,
    navigateToPlayer: (list: String, track: TrendingElement) -> Unit
) {
    val context = LocalContext.current
    if(trendingList.value.size!=0){
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.trending),
            textAlign = TextAlign.Center,
            fontSize = 30.sp
        )

        LazyRow(
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth()
                .background(color = Color.Blue),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ){
            items(trendingList.value){
                TrendingItem(it, onClick = {

                    //navigateToPlayer.invoke(TRENDING.name, it)

                    val intent = Intent(context, PlayerActivity::class.java)
                    intent.putExtra(TRACK_ID, it.id)
                    intent.putExtra(TRACK_ARTIST, it.artist)
                    intent.putExtra(TRACK_IMAGE, it.image)
                    intent.putExtra(TRACK_TITLE, it.title)
                    intent.putExtra(TRACK_ALBUM, it.album)
                    intent.putExtra(TRACK_IS_FAVORITE, it.isFavorite)
                    intent.putExtra(PLAYING_LIST, TRENDING.name)
                    context.startActivity(intent)


                })
            }
        }
    }
}

@Composable
fun TrendingItem(
    element: TrendingElement,
    onClick: (TrendingElement) -> Unit
){
    Column(
        modifier = Modifier
            .wrapContentSize()
            .clickable(onClick = {
                onClick.invoke(element)
            })
    ) {

        ConstraintLayout(
            Modifier
                .fillMaxHeight()
                .width(160.dp)
        ) {

            val (image, textContainer) = createRefs()
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
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

            Row(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
                    .constrainAs(textContainer) {
                        bottom.linkTo(parent.bottom, 10.dp)
                        start.linkTo(parent.start, 10.dp)
                        end.linkTo(parent.end, 10.dp)
                    }
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = Color.LightGray),
                verticalAlignment = Alignment.CenterVertically
            ){
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(horizontal = 4.dp)
                        .weight(7f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = element.title, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Row(){
                        Image(modifier = Modifier.size(20.dp),painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "")

                        Text(text = element.artist+" - "+element.album, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(3f)
                        .padding(horizontal = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(painter = painterResource(id = R.drawable.ic_launcher_background), contentDescription = "")
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    drawerState: DrawerState,
    scope: CoroutineScope,
){
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
                .background(color = Color.Blue, shape = RoundedCornerShape(10.dp))
                .clickable(onClick = {
                    scope.launch {
                        drawerState.open()
                    }
                })

        ) {

        }

        Divider(
            modifier = Modifier.width(10.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Red, shape = RoundedCornerShape(10.dp))
        ) {

        }
    }
}

@Composable
fun TabScreens(
    trendingList: State<List<TrendingElement>>,
    loadTabContentList: (category: Int) -> Unit,
    rockList: State<List<TrendingElement>>,
    hiphopList: State<List<TrendingElement>>,
    electroList: State<List<TrendingElement>>,
    addOrRemoveFavorite: (favorite: Boolean, id: String) -> Unit
) {
    var tabIndex = remember { mutableStateOf(0) }

    val tabs = listOf(
        stringResource(id = R.string.trending),
        stringResource(id = R.string.rock),
        stringResource(id = R.string.hiphop),
        stringResource(id = R.string.electro)
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(selectedTabIndex = tabIndex.value) {
            tabs.forEachIndexed { index, title ->
                Tab(text = { Text(title) },
                    selected = tabIndex.value == index,
                    onClick = {
                        tabIndex.value = index

                    }
                )
            }
        }
        when (tabIndex.value) {
            0 -> TabContent(category = tabs.get(tabIndex.value), listTrack = trendingList, addOrRemoveFavorite = addOrRemoveFavorite)
            1 -> TabContent(category = tabs.get(tabIndex.value), listTrack = rockList, loadContent = loadTabContentList, genreId = 152, addOrRemoveFavorite = addOrRemoveFavorite) // rock
            2 -> TabContent(category = tabs.get(tabIndex.value), listTrack = hiphopList, loadContent = loadTabContentList, genreId = 116, addOrRemoveFavorite = addOrRemoveFavorite) // Rap/ Hip Hop
            3 -> TabContent(category = tabs.get(tabIndex.value), listTrack = electroList, loadContent = loadTabContentList, genreId = 106, addOrRemoveFavorite = addOrRemoveFavorite) // Electro
        }
    }
}

@Composable
fun TabContent(
    category: String,
    loadContent: (Int) -> Unit = {},
    listTrack: State<List<TrendingElement>>,
    genreId : Int? = null,
    addOrRemoveFavorite: (favorite: Boolean, id: String) -> Unit
){
    if(listTrack.value.size==0){
        genreId?.let(loadContent)
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(color = Color.Gray)
            .padding(horizontal = 4.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        ){
        items(listTrack.value){
            TabContentListItem(it, addOrRemoveFavorite)
        }
    }
}

@Composable
fun TabContentListItem(
    trendingElement: TrendingElement,
    addOrRemoveFavorite: (favorite: Boolean, id: String) -> Unit
) {

    val favoriteState = remember { mutableStateOf(trendingElement.isFavorite) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(2f)
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clip(RoundedCornerShape(8.dp)),
                fallback = painterResource(id = R.drawable.ic_launcher_foreground),
                error = painterResource(id = R.drawable.ic_launcher_background),
                model = trendingElement.image,
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(6f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = trendingElement.title, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Row(){
                Image(modifier = Modifier.size(20.dp),painter = painterResource(id = android.R.drawable.star_on), contentDescription = "")
                Text(text = trendingElement.artist, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .clickable(onClick = {
                    val favorite = !trendingElement.isFavorite
                    trendingElement.isFavorite = favorite
                    favoriteState.value = favorite
                    addOrRemoveFavorite(favorite, trendingElement.id)
                }),
            contentAlignment = Alignment.Center
        ) {
            Image(painter = painterResource(id = when(favoriteState.value){
                true -> android.R.drawable.ic_menu_close_clear_cancel
                false -> android.R.drawable.ic_menu_add
            }), contentDescription = "ADD FAVORITES")
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PreviewTrendingScreen(){
    TrendingScreen(
        drawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
        scope = rememberCoroutineScope(),
        trendingList = remember { mutableStateOf(emptyList()) },
        loadingState = remember { mutableStateOf(false) },
        loadTabContentList = {},
        rockList = remember { mutableStateOf(emptyList()) },
        hiphopList = remember { mutableStateOf(emptyList()) },
        electroList = remember { mutableStateOf(emptyList()) },
        addOrRemoveFavorite = {a,b ->  },
        navigateToPlayer = {a,b -> }
    )
}
