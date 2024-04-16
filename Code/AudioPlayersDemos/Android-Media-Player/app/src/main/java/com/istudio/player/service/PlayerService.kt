package com.istudio.player.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.os.Messenger
import android.util.Log
import androidx.annotation.RequiresApi
import com.istudio.player.APP_TAG
import com.istudio.player.Constants
import com.istudio.player.Constants.NOTIFICATION_CHANNEL_ID
import com.istudio.player.R
import com.istudio.player.handlers.PlayerHandler

class PlayerService : Service() {

    private lateinit var player : MediaPlayer
    private val context : Context by lazy { this }

    val mMessenger = Messenger(
        PlayerHandler(
            this@PlayerService
        )
    )

    /**
     * < ************************** > Service Methods < **************************>
     */

    override fun onCreate() {
        initOnCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return initOnBind()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return initOnStartCommand()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        initOnUnBind()
        return super.onUnbind(intent)
    }


    override fun onDestroy() {
        initOnDestroy()
        super.onDestroy()
    }
    /**
     * < ************************** > Service Methods < **************************>
     */



    /**
     * < ************************** > Init Methods < *****************************>
     */

    private fun initOnCreate() {
        Log.d(APP_TAG, "PlayerService - onCreate is called")
        // Initialize the media player - Because its called only once during the lifecycle of the player
        player = MediaPlayer.create(context,R.raw.demo_music)
    }

    private fun initOnBind(): IBinder? {
        Log.d(APP_TAG, "PlayerService - onBind is called")
        return mMessenger.binder
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ForegroundServiceType")
    private fun initOnStartCommand(): Int {
        Log.d(APP_TAG, "PlayerService - onStartCommand is called")
        // We perform NOT_STICKY flag because, Its possible that our service might crash and we should not restart it since there would be a player with no song

        val notificationBuilder = Notification.Builder(this@PlayerService, NOTIFICATION_CHANNEL_ID)
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
        val notification = notificationBuilder.build()
        startForeground(11, notification)

        player.setOnCompletionListener {
            // Also when the song has completed, We should stop the playing service.
            stopSelf()
            stopForeground(STOP_FOREGROUND_REMOVE)
        }

        return START_NOT_STICKY
    }

    private fun initOnUnBind() {
        Log.d(APP_TAG, "PlayerService - onUnBind is called")
    }

    private fun initOnDestroy() {
        Log.d(APP_TAG, "PlayerService - onDestroy is called")
        player.release()
    }
    /**
     * < ************************** > Init Methods < *****************************>
     */


    /**
     * < ************************** > Binder class < *****************************>
     */

    /**
     * Purpose of binder is to provide reference to our service for our activity
     */
    /*inner class LocalBinder : Binder() {
        // We return the instance of this class in onBind method
        val service : PlayerService
            get() = this@PlayerService
    }*/


    /**
     * < ************************** > Binder class < *****************************>
     */



    /**
     * < ************************** > Client Methods < *****************************>
     */

    fun isPlayerPlaying() = player.isPlaying

    fun play(){
        player.start()
    }

    fun pause(){
        player.pause()
    }

    fun stop(){
        if(player.isPlaying){
            player.stop()
        }
    }

    fun togglePlayPause(){
        if(player.isPlaying){ pause() } else{ play() }
    }

    /**
     * < ************************** > Client Methods < *****************************>
     */

}