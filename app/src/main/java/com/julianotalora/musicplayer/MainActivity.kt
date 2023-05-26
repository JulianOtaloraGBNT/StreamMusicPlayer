package com.julianotalora.musicplayer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.julianotalora.musicplayer.domain.ACTION_PLAY
import com.julianotalora.musicplayer.domain.PlayerService
import com.julianotalora.musicplayer.ui.theme.MusicPlayerTheme

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicPlayerTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    //Greeting("Android")
                    Scaffold(
                        content = {
                            ServiceUI()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ServiceUI(context: Context = LocalContext.current){
    // on below line creating variable
    // for service status and button value.
    val serviceStatus = remember { mutableStateOf(false) }
    val buttonValue = remember { mutableStateOf("Start Service") }

    Column(
        // on below line we are adding a modifier to it,
        modifier = Modifier
            .fillMaxSize()
            // on below line we are adding a padding.
            .padding(all = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ){

        Row() {

            Button(onClick = {
                if (serviceStatus.value) {
                    // service already running
                    // stop the service
                    serviceStatus.value = !serviceStatus.value
                    buttonValue.value = "Start Service"
                    context.stopService(Intent(context, PlayerService::class.java))

                } else {
                    // service not running start service.
                    serviceStatus.value = !serviceStatus.value
                    buttonValue.value = "Stop Service"

                    // starting the service
                    context.startService(Intent(context, PlayerService::class.java).apply {
                        this.action = ACTION_PLAY
                    })
                }
            }){
                // on below line creating a text for our button.
                Text(
                    // on below line adding a text,
                    // padding, color and font size.
                    text = "PLAY",
                    modifier = Modifier.padding(10.dp),
                    fontSize = 20.sp
                )
            }

        }

    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
    //MediaDevice()
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MusicPlayerTheme {
        ServiceUI()
        //Greeting("Android")
    }
}