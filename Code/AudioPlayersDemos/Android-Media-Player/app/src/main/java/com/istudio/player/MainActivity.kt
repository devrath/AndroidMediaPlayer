package com.istudio.player

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.istudio.player.databinding.ActivityMainBinding
import com.istudio.player.handlers.ActivityHandler
import com.istudio.player.service.PlayerService
import com.istudio.player.ui.theme.PlayerTheme

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // This flag is used to indicate if a service is bounded/unbounded at any particular point in time.
    private var isServiceBound = false
    lateinit var mServiceMessenger : Messenger;
    private val mActivityMessenger = Messenger(ActivityHandler(this))

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(POST_NOTIFICATIONS)
        }
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
        if (isServiceBound) {
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


    public fun changePlayButtonText(text: String) {
        binding.apply {
            playAndPauseId.text = text
        }
    }

    private fun setOnClickListeners() {
        binding.apply {
            playAndPauseId.setOnClickListener {
                if (isServiceBound) {
                    // Here we shall make the service STARTED so that even if the activity is in background still the music plays
                    val intent = Intent(this@MainActivity, PlayerService::class.java)
                    startService(intent)

                    val message = Message.obtain()
                    message.arg1=2;

                    message.replyTo = mActivityMessenger
                    mServiceMessenger.send(message)
                }
            }
            stopId.setOnClickListener {

            }
        }
    }

    private fun bindPlayerService() {
        // BIND_AUTO_CREATE --> Have the service auto created when we bind to it.
        val service = Intent(this, PlayerService::class.java)
        bindService(service, serviceConn, Context.BIND_AUTO_CREATE)
    }

    private fun unBindPlayerService() {
        unbindService(serviceConn)
    }

    /**
     * Request permission to display the notification
     */
    private fun requestPermissions(vararg permissions: String) {

        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            result.entries.forEach {
                Log.d("MainActivity", "${it.key} = ${it.value}")
            }
        }
        requestPermissionLauncher.launch(permissions.asList().toTypedArray())
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
            message.arg1=2;
            message.arg2=1;

            message.replyTo = mActivityMessenger
            mServiceMessenger.send(message)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(APP_TAG, "Player Service onServiceDisconnected")
            isServiceBound = false
        }

    }
}