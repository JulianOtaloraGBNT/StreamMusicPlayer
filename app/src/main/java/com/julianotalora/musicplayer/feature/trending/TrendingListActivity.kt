package com.julianotalora.musicplayer.feature.trending

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.julianotalora.musicplayer.feature.theme.MusicPlayerTheme

class TrendingListActivity : ComponentActivity() {


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicPlayerTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "FirstScreen") {
                    composable("FirstScreen") {
                        //FirstScreen(navigation = navController)
                    }
                    composable("SecondScreen") {
                        //SecondScreen(navigation = navController)
                    }
                }

                /*
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    //Greeting("Android")
                    Scaffold(
                        content = {
                            Column(modifier = Modifier.padding(it)) {
                                ContentViewList()
                            }
                        }
                    )
                }

                */
            }
        }
    }

}

@Composable
fun ContentViewList(){

}