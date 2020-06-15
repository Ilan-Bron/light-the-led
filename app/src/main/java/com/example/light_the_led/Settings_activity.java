package com.example.light_the_led;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class Settings_activity extends AppCompatActivity
//this class is for the settings screen
{

    EditText get_ip , get_port;

    Switch mute_sound;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    //initialize all the objects on screen
    {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.settings_layout);


    get_ip = (EditText)findViewById(R.id.Settings_edit_text_ip_adress);
    get_ip.setText(Connection.IP_ADDRESS);

    get_port = (EditText)findViewById(R.id.settings_edit_text_port_num);
    get_port.setText(Integer.toString(Connection.PORT));

    mute_sound = (Switch)findViewById(R.id.mute_sound_switch);

    mute_sound.setChecked(!musicService.is_running);
    mute_sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if(!isChecked)
                startService(new Intent(Settings_activity.this , musicService.class));
            else
                stopService(new Intent(Settings_activity.this , musicService.class));

        }
    });


    }


    public void save_and_return_to_prev_activity(View view)
    //when clicked on the floating action button save and return to the previous screen
    {
        String new_ip = get_ip.getText().toString().trim();

        if(new_ip.isEmpty() || !check_IP_adress(new_ip))
        {
            new_ip = Connection.IP_ADDRESS;
        }

        Connection.IP_ADDRESS = new_ip;

        String new_port = get_port.getText().toString().trim();
        int port_num = -1;

        if(new_port.isEmpty())
        {
            port_num = Connection.PORT;
        }
        else
            port_num = Integer.parseInt(new_port);

        Connection.PORT = port_num;


        Intent return_to_prev_activity = new Intent();
        setResult(2 , return_to_prev_activity);
        finish();

    }


    public boolean check_IP_adress(String ip_adress)
    //check whether an IP address is valid
    {
        String[] divided_adress = ip_adress.split("\\.");

        boolean is_viable_ip = true;

        if(divided_adress.length != 4)
            is_viable_ip = false;

        for(int ind = 0 ; ind < divided_adress.length && is_viable_ip ; ind++)
        {
            String curr_part = divided_adress[ind];
            try
            {
                int part_int = Integer.parseInt(curr_part);

                if(part_int > 255 || part_int <= 0) //can't be negative or greater than 255
                    is_viable_ip = false;


            }catch(NumberFormatException e)//if parseInt fails no number in the part
            {
                is_viable_ip = false;
            }
        }


        return is_viable_ip;
    }

}


