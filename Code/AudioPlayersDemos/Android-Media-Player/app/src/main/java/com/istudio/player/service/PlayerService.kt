package com.istudio.player.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.istudio.player.APP_TAG
import com.istudio.player.R

class PlayerService : Service() {

    private lateinit var player : MediaPlayer
    private val context : Context by lazy { this }

    private val mBinder : IBinder = LocalBinder()

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
        // Initialize the media player
        player = MediaPlayer.create(context,R.raw.demo_music)
    }

    private fun initOnBind(): IBinder? {
        Log.d(APP_TAG, "PlayerService - onBind is called")
        return mBinder
    }

    private fun initOnStartCommand(): Int {
        Log.d(APP_TAG, "PlayerService - onStartCommand is called")

        player.setOnCompletionListener {
            // Stop self will stop the service immediately regardless if its working or not
            stopSelf()
        }

        // Don't recreate it if killed, Because if we restart the service, We will have a service started with no music playing
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
    inner class LocalBinder : Binder() {
        // We return the instance of this class in onBind method
        val service : PlayerService
            get() = this@PlayerService
    }
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