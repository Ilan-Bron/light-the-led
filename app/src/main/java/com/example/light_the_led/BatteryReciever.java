package com.example.light_the_led;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.ImageView;

import com.example.light_the_led.R;

public class BatteryReciever extends BroadcastReceiver
//this class is responsible for getting the current battety level and changing image color depending on it
{

    ImageView battery_state;

    public BatteryReciever(ImageView state)
    //constructor for the class state is the image view that will change color
    {
        super();
        this.battery_state = state;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    //on receive gets information about the current battery level
    {
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level * 100 / (float)scale;


        if(batteryPct < 50)
            this.battery_state.setImageResource(R.color.Red);
        else if( batteryPct < 75)
            this.battery_state.setImageResource(R.color.Orange);
        else
            this.battery_state.setImageResource(R.color.Lime);

    }


}
