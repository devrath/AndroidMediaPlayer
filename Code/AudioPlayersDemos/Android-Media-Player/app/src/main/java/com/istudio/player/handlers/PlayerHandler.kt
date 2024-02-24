package com.istudio.player.handlers

import android.os.Handler
import android.os.Message
import com.istudio.player.service.PlayerService

class PlayerHandler(private val mPlayerService: PlayerService) : Handler() {

    override fun handleMessage(msg: Message) {
        handleInCommingMessages(msg)
    }

    private fun handleInCommingMessages(msg: Message) {
        when (msg.arg1) {
            0 -> mPlayerService.play()
            1 -> mPlayerService.pause()
            2 -> {

                val isPlaying = if (mPlayerService.isPlayerPlaying()) 1 else 0
                // Prepare the message
                val message = Message.obtain()
                message.arg1 = isPlaying
               if(msg.arg2==1){
                   message.arg1 = 1
               }
                message.replyTo = mPlayerService.mMessenger
                try {
                    // msg.replyTo gives the messenger attached to this message
                    msg.replyTo.send(message)
                } catch (exception: Exception) {
                    exception.printStackTrace()
                }
            }
        }
    }

}
