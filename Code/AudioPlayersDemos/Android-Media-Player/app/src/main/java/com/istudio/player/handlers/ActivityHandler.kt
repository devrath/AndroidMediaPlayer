package com.istudio.player.handlers

import android.os.Handler
import android.os.Message
import android.os.RemoteException
import com.istudio.player.MainActivity

class ActivityHandler(private val mMainActivity: MainActivity) : Handler() {
    override fun handleMessage(msg: Message) {
        if (msg.arg1 == 0) {
            // Music is not playing
            if (msg.arg2 == 1) {
                mMainActivity.changePlayButtonText("Play")
            } else {
                // Play music
                val message = Message.obtain()
                message.arg1 = 0
                try {
                    msg.replyTo.send(message)
                } catch (e: RemoteException) {
                    throw RuntimeException(e)
                }
                // Change play button to say pause
                mMainActivity.changePlayButtonText("Pause")
            }
        } else if (msg.arg1 == 1) {
            // Music is playing
            if (msg.arg2 == 1) {
                mMainActivity.changePlayButtonText("Pause")
            } else {
                // Pause music
                val message = Message.obtain()
                message.arg1 = 1
                try {
                    msg.replyTo.send(message)
                } catch (e: RemoteException) {
                    throw RuntimeException(e)
                }
                // Change play button to say play
                mMainActivity.changePlayButtonText("Play")
            }
        }
    }
}
