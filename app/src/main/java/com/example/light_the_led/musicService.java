package com.example.light_the_led;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.IBinder;

import java.net.URI;

public class musicService extends Service
//this class will play a song across the entire app
{
    MediaPlayer music_player;

    public static boolean is_running;


    public void onCreate()
    //initalize the class
    {
        super.onCreate();
        this.music_player = MediaPlayer.create(this, R.raw.audio);
        this.music_player.setLooping(true); // Set looping
        this.music_player.setVolume(100, 100);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    //when you get start service intent start playing music
    {
        this.music_player.start();

        is_running = true;

        return START_STICKY;
    }

    @Override
    public void onDestroy()
    //when you get stop service intent stop the music
    {
        super.onDestroy();

        this.music_player.stop();
        is_running = false;

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}