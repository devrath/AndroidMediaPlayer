package com.istudio.player

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.istudio.player.databinding.ActivityMainBinding
import com.istudio.player.service.PlayerService
import com.istudio.player.ui.theme.PlayerTheme

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // This flag is used to indicate if a service is bounded/unbounded at any particular point in time.
    private var isServiceBound = false
    private lateinit var mPlayerService : PlayerService

    /**
     * < ************************** > LifeCycle Methods < **************************>
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initOnCreate()
    }

    override fun onStart() {
        super.onStart()
        initOnStart()
    }

    override fun onResume() {
        super.onResume()
        initOnResume()
    }

    override fun onStop() {
        super.onStop()
        initOnStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        initOnDestroy()
    }
    /**
     * < ************************** > LifeCycle Methods < **************************>
     */


    /**
     * < ************************** > Init Methods < *****************************>
     */
    private fun initOnCreate() {
        Log.d(APP_TAG, "Activity - onCreate is called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setOnClickListeners()
    }

    private fun initOnStart() {
        Log.d(APP_TAG, "Activity - onStart is called")
        bindPlayerService()
    }

    private fun initOnResume() {
        Log.d(APP_TAG, "Activity - onResume is called")
    }

    private fun initOnStop() {
        Log.d(APP_TAG, "Activity - onStop is called")
        if(isServiceBound){
            // We call un-bind only if the service is actually bound
            unBindPlayerService()
            // Set the flag to false
            isServiceBound = false
        }
    }

    private fun initOnDestroy() {
        Log.d(APP_TAG, "Activity - onDestroy is called")
    }
    /**
     * < ************************** > Init Methods < *****************************>
     */

    /**
     * < ************************** > User Defined Methods < **********************>
     */
    private fun setOnClickListeners() {
        binding.apply {
            playAndPauseId.setOnClickListener {
                if(isServiceBound){
                    if (mPlayerService.isPlayerPlaying()){
                        // <-------- Player is playing -------->

                        // Pause the playing player
                        mPlayerService.pause()
                        // Indicate next action to be done as play
                        playAndPauseId.setText(R.string.str_play)
                    }else{
                        // <-------- Player is not playing -------->

                        // Play the paused player
                        mPlayerService.play()
                        // Indicate next action to be done as pause
                        playAndPauseId.setText(R.string.str_pause)
                    }
                }
            }
            stopId.setOnClickListener {

            }
        }
    }

    private fun bindPlayerService() {
        // BIND_AUTO_CREATE --> Have the service auto created when we bind to it.
        val service = Intent(this, PlayerService::class.java)
        bindService(service,serviceConn,Context.BIND_AUTO_CREATE)
    }

    private fun unBindPlayerService(){
        unbindService(serviceConn)
    }
    /**
     * < ************************** > User Defined Methods < **********************>
     */


    private val serviceConn = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, iBinder: IBinder?) {
            Log.d(APP_TAG, "Player Service onServiceConnected")
            isServiceBound = true

            // Get access to player service using IBinder reference from the service class
            val localBinder = iBinder as PlayerService.LocalBinder
            mPlayerService = localBinder.service

            // If the player is playing set the text to pause
            if (mPlayerService.isPlayerPlaying()){
                binding.playAndPauseId.setText(R.string.str_pause)
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(APP_TAG, "Player Service onServiceDisconnected")
            isServiceBound = false
        }

    }
}