package com.julianotalora.musicplayer.feature.player

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.julianotalora.musicplayer.domain.ACTION_PAUSE
import com.julianotalora.musicplayer.domain.ACTION_PLAY
import com.julianotalora.musicplayer.domain.ACTION_SET_SONG
import com.julianotalora.musicplayer.domain.PlayerService
import com.julianotalora.musicplayer.feature.theme.MusicPlayerTheme

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class PlayerActivity : ComponentActivity(), LifecycleEventObserver {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        setContent {
            MusicPlayerTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    //Greeting("Android")
                    Scaffold(
                        topBar = {
                            PlayerTopBar {
                                onBackPressedDispatcher.onBackPressed()
                            }
                        },
                        content = {
                            ServiceUI(extras = intent.extras)
                        }
                    )
                }
            }
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when(event){
            Lifecycle.Event.ON_START -> {

            }
            Lifecycle.Event.ON_STOP -> {

            }
            else -> {

            }
        }
    }
}

@SuppressLint("PermissionLaunchedDuringComposition")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ServiceUI(extras: Bundle?, context: Context = LocalContext.current){

    val permissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

    val serviceStatus = remember { mutableStateOf(false) }
    val buttonValue = remember { mutableStateOf("Start Service") }


    LaunchService(context, serviceStatus,extras)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ){

        Box(modifier = Modifier){

        }

        Row() {
            Button(onClick = {

                with(permissionState) {
                    when{
                        hasPermission -> {
                            playAndPauseMusic(serviceStatus, buttonValue, context)
                        }
                        shouldShowRationale -> {

                            permissionState.launchPermissionRequest()
                        }
                        else -> {
                            permissionState.launchPermissionRequest()
                        }
                    }
                }
            }){
                // on below line creating a text for our button.
                Text(
                    // on below line adding a text,
                    // padding, color and font size.
                    text = buttonValue.value,
                    modifier = Modifier.padding(10.dp),
                    fontSize = 20.sp
                )
            }

        }

    }
}

@Composable
fun PlayerTopBar(onBackClick: () -> Unit) {
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

fun playAndPauseMusic(serviceStatus: MutableState<Boolean>, buttonValue: MutableState<String>, context: Context) {
    if (serviceStatus.value) {
        // service already running
        // stop the service
        serviceStatus.value = !serviceStatus.value
        buttonValue.value = "PLAY"
        context.startService(Intent(context, PlayerService::class.java).apply {
            this.action = ACTION_PAUSE
        })

    } else {
        // service not running start service.
        serviceStatus.value = !serviceStatus.value
        buttonValue.value = "PAUSE"

        // starting the service
        context.startService(Intent(context, PlayerService::class.java).apply {
            this.action = ACTION_PLAY
        })
    }
}
fun LaunchService(context: Context, serviceStatus: MutableState<Boolean>, extras: Bundle?){
    val intent = Intent(context, PlayerService::class.java)
    intent.putExtras(extras ?: Bundle())
    intent.apply {
        this.action = ACTION_SET_SONG
    }

    context.startService(intent)
    serviceStatus.value = true
    /*
    if (serviceStatus.value) {
        // service already running
        // stop the service
        serviceStatus.value = !serviceStatus.value
        buttonValue.value = "PLAY"
        context.startService(Intent(context, PlayerService::class.java).apply {
            this.action = ACTION_PAUSE
        })

    } else {
        // service not running start service.
        serviceStatus.value = !serviceStatus.value
        buttonValue.value = "PAUSE"

        // starting the service
        context.startService(Intent(context, PlayerService::class.java).apply {
            this.action = ACTION_PLAY
        })
    }
    */
}

@Composable
fun MediaDevice(){
    var localContext = LocalContext.current
    val mediaPlayer = MediaPlayer().apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
        setDataSource("https://cdns-preview-f.dzcdn.net/stream/c-fe1e4877e17554bc3e4528d3383724b8-6.mp3")
        prepare() // might take long! (for buffering, etc)
        //start()
    }

    //mediaPlayer.start()
}