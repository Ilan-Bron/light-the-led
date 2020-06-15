package com.example.light_the_led;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class Log_in_activity extends AppCompatActivity
//this class is for the log in screen
{

    EditText user_name_edit_text;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    //initialize all the objects on the screen
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_activity_layout);

        user_name_edit_text = (EditText)findViewById(R.id.Log_in_edit_text_user_name);


        startService(new Intent(this , musicService.class));
    }



    public void Log_in_button_method(View view) throws ExecutionException, InterruptedException
    //the method for log in button log into the main screen
    {
        // on login get user name and action string
        String user_name = user_name_edit_text.getText().toString().trim();

        if(user_name.isEmpty())
        {
            Toast.makeText(this , "put in a user name" , Toast.LENGTH_LONG).show();
            return;
        }



        Connection connect_to_server_first_time = new Connection(getApplicationContext());
        connect_to_server_first_time.execute(user_name);

        String action_string = connect_to_server_first_time.get(); //connection returns the action string after finishing

        if(action_string.equals("f")) //if couldn't connect to server just stop
            return;

        Intent start_main_activity = new Intent(this , MainActivity.class);

        start_main_activity.putExtra("action_string" , action_string);
        start_main_activity.putExtra("user_name" , user_name); // sending user name and action string to the main activity

        startActivity(start_main_activity);
    }



    public void open_settings_fab_method(View view)
    //the method for the floating action button open settings screen
    {
        Intent open_settings_intent = new Intent(this , Settings_activity.class);

        startActivityForResult(open_settings_intent , 2);
    }


}
