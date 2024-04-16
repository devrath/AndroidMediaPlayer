package com.istudio.player.handlers;

import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;

import androidx.annotation.NonNull;

import com.istudio.player.MainActivity;

public class ActivityHandler extends Handler {

    private MainActivity mMainActivity;

    public ActivityHandler(MainActivity mainActivity){
        this.mMainActivity = mainActivity;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        if(msg.arg1==0){
            // Music is not playing
            if(msg.arg2==1){
                mMainActivity.changePlayButtonText("Play");
            }else{
                // Play the music
                Message message = Message.obtain();
                message.arg1 = 0;
                try {
                    msg.replyTo.send(message);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                // Change Play button to say "pause"
                mMainActivity.changePlayButtonText("Pause");
            }
        }else if(msg.arg1==1){
            // Music is playing
            if(msg.arg2==1){
                mMainActivity.changePlayButtonText("Pause");
            }else {
                // Pause the music
                Message message = Message.obtain();
                message.arg1 = 1;
                try {
                    msg.replyTo.send(message);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                // Change Play button to say "Play"
                mMainActivity.changePlayButtonText("Play");
            }
        }
    }


}
