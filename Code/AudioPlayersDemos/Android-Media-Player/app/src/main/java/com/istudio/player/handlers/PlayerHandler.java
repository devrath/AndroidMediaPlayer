package com.istudio.player.handlers;

import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.widget.Switch;

import androidx.annotation.NonNull;

import com.istudio.player.service.PlayerService;

/**
 * We need the reference to the player service inside this handler
 *
 */
public class PlayerHandler extends Handler {

    private PlayerService mPlayerService;

    public PlayerHandler(PlayerService playerService){
        this.mPlayerService = playerService;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        // Here we shall perform different operations based on the arguments
        switch(msg.arg1){
            case 0: // Play
                mPlayerService.play();
                break;
            case 1: // Pause
                mPlayerService.pause();
                break;
            case 2: // isPlaying
                // If player is playing return "1" else return "0"
                int isPlaying = mPlayerService.isPlayerPlaying() ? 1 : 0 ;
                Message message = Message.obtain();
                message.arg1 = isPlaying;
                if(msg.arg2==1){
                    message.arg2 = 1;
                }
                message.replyTo = mPlayerService.getMMessenger();
                // Send the message to the activity
                try {
                    msg.replyTo.send(message);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                break;
        }

    }



}
