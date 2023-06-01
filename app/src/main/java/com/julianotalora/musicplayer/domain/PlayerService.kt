package com.julianotalora.musicplayer.domain

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.snap
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.MediaStyle
import com.julianotalora.musicplayer.R
import com.julianotalora.musicplayer.common.PLAYING_LIST
import com.julianotalora.musicplayer.common.TRACK_ID
import com.julianotalora.musicplayer.common.runCatchingWithMinimumTime
import com.julianotalora.musicplayer.feature.TrendingCategories
import com.julianotalora.musicplayer.feature.main.MainActivity
import com.julianotalora.musicplayer.feature.trending.repository.GetTrendingListRepository
import com.julianotalora.musicplayer.feature.trending.state.TrendingElement
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

const val ACTION_PLAY: String = "com.julianotalora.musicplayer.action.PLAY"
const val ACTION_PAUSE: String = "com.julianotalora.musicplayer.action.PAUSE"
const val ACTION_SET_SONG: String = "com.julianotalora.musicplayer.action.SET"

@AndroidEntryPoint
class PlayerService : Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener{


    private var mediaPlayer: MediaPlayer? = null
    @Inject
    lateinit var repository : GetTrendingListRepository
    var genre: String = String()
    var playingList: String? = String()
    lateinit var playList: List<TrendingElement>
    var currentSongId: String? = String()
    var currentSong : TrendingElement = TrendingElement()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val action: String = intent?.action.orEmpty()
        when(action) {
            ACTION_PLAY -> {
                showNotificationControls(intent)
                if(mediaPlayer == null){
                    mediaPlayer = MediaPlayer()//.create(this, Uri.parse("https://cdns-preview-f.dzcdn.net/stream/c-fe1e4877e17554bc3e4528d3383724b8-6.mp3")) // initialize it here
                    mediaPlayer?.apply {
                        setOnPreparedListener(this@PlayerService)
                        setOnErrorListener(this@PlayerService)
                        setOnCompletionListener(this@PlayerService)
                        setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
                        Log.d("SONG", currentSong.preview.orEmpty())
                        setDataSource(currentSong.preview.orEmpty())
                        //wifiLock.acquire()
                        prepareAsync() // prepare async to not block main thread
                    }
                }else{
                    mediaPlayer?.start()
                }
            }
            ACTION_PAUSE -> {
                showNotificationControls(intent)
                mediaPlayer?.pause()
            }
            ACTION_SET_SONG -> {
                getList(intent)
            }
        }

        return START_NOT_STICKY
    }

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    fun getList(intent: Intent?){
        playingList = intent?.getStringExtra(PLAYING_LIST)
        val songId = intent?.getStringExtra(TRACK_ID)
        if(currentSongId != songId){
            mediaPlayer = null
            currentSongId = songId
        }
        currentSongId = intent?.getStringExtra(TRACK_ID)
        scope.launch {
            runCatchingWithMinimumTime {
                when(playingList){
                    TrendingCategories.TRENDING.name -> {
                        repository.getTrendingList()
                    }
                    else -> {
                        repository.getLocalTracksByGenre(getGenre(playingList.orEmpty()))
                    }
                }
            }.onSuccess {
                playList = it
                currentSong = findCurrentSong(currentSongId.orEmpty(), it)
            }.onFailure {
                it.cause
            }
        }
    }


    fun findCurrentSong(currentId: String, list: List<TrendingElement>): TrendingElement {
        return list.filter {
            it.id == currentId
        }.first()
    }
    fun getGenre(playing: String): String {
        return when(playing){
            TrendingCategories.ROCK.name -> "152"
            TrendingCategories.HIPHOP.name -> "116"
            TrendingCategories.ELECTRO.name -> "106"
            else -> ""
        }
    }
    private fun setOnCompletionListener(playerService: PlayerService) {

    }

    /** Called when MediaPlayer is ready */
    override fun onPrepared(mediaPlayer: MediaPlayer) {
        mediaPlayer.start()
    }

    override fun onError(mp: MediaPlayer, what: Int, extra: Int): Boolean {
        // ... react appropriately ...
        // The MediaPlayer has moved to the Error state, must be reset!
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    private fun generateAction(icon: Int, title: String, intentAction: String): NotificationCompat.Action {
        val intent = Intent(applicationContext, PlayerService::class.java)
        intent.action = intentAction
        val pendingIntent = PendingIntent.getService(applicationContext, 1, intent, PendingIntent.FLAG_MUTABLE)
        return NotificationCompat.Action(icon, title, pendingIntent)
    }
    fun showNotificationControls(intent: Intent?) {

        val action: String = intent?.action.orEmpty()

        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("my_service", "My Background Service")
            } else {
                // If earlier version channel ID is not used
                // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                "ForegroundServiceChannel"
            }
        val input = "CANCION"//intent?.getStringExtra(Constants.inputExtra)
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
// 1


        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("TITULO APP MEDIA PLAYER")
            .setContentText(input)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .setOngoing(false)
            .setStyle(MediaStyle())



        notification.addAction( when(action){
            ACTION_PLAY -> generateAction( android.R.drawable.ic_media_pause, "PAUSE", ACTION_PAUSE )
            ACTION_PAUSE -> generateAction( android.R.drawable.ic_media_play, "PLAY", ACTION_PLAY )
            else -> generateAction( android.R.drawable.ic_media_play, "PLAY", ACTION_PLAY )
        })


        startForeground(2, notification.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String{
        val chan = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }
}