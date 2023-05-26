package com.julianotalora.musicplayer.domain

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer

import android.net.Uri
import android.net.wifi.WifiManager
import android.os.IBinder
import android.os.PowerManager

const val ACTION_PLAY: String = "com.julianotalora.musicplayer.action.PLAY"

class PlayerService : Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener{

    private var mediaPlayer: MediaPlayer? = null



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        //val wifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager
        //val wifiLock: WifiManager.WifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock")

        val action: String = intent?.action.orEmpty()
        when(action) {
            ACTION_PLAY -> {
                mediaPlayer = MediaPlayer() // initialize it here
                mediaPlayer?.apply {
                    setOnPreparedListener(this@PlayerService)
                    setOnErrorListener(this@PlayerService)
                    setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
                    setDataSource("https://cdns-preview-f.dzcdn.net/stream/c-fe1e4877e17554bc3e4528d3383724b8-6.mp3")
                    //wifiLock.acquire()
                    prepareAsync() // prepare async to not block main thread
                }

            }
        }

        return START_STICKY
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
}