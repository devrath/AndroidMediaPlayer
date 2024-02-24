package com.istudio.player

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.istudio.player.databinding.ActivityMainBinding
import com.istudio.player.handlers.ActivityHandler
import com.istudio.player.service.PlayerService

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mServiceMessenger : Messenger
    private var mActivityMessenger : Messenger = Messenger(ActivityHandler(this))
    private var isServiceBound = false


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
                    startService(serviceInstance())

                    val message = Message.obtain()
                    message.arg1 = 2
                    message.replyTo = mActivityMessenger
                    try {
                        mServiceMessenger.send(message)
                    } catch (e: RemoteException) {
                        throw RuntimeException(e)
                    }
                }
            }
            stopId.setOnClickListener {

            }
        }
    }

    public fun changePlayButtonText(text: String){
        binding.playAndPauseId.text = text
    }

    private fun bindPlayerService() {
        bindService(serviceInstance(),serviceConn,Context.BIND_AUTO_CREATE)
    }

    private fun serviceInstance() = Intent(this, PlayerService::class.java)

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
            mServiceMessenger = Messenger(iBinder)

            val message = Message.obtain()
            message.arg1 = 2
            message.arg2 = 1
            message.replyTo = mActivityMessenger
            try {
                mServiceMessenger.send(message)
            } catch (e: RemoteException) {
                throw RuntimeException(e)
            }

        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(APP_TAG, "Player Service onServiceDisconnected")
            isServiceBound = false
        }

    }
}