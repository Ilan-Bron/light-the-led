package com.example.light_the_led;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
//the class for the main screen in the app
{

    public static String user_name = "ilan";
    public static String user_action_string = "";       // the user name and action string of the user, will change on log-in

    public final String LOG_TAG = "main activity";      // the tag used by this activity for logs

    Button turn_on, turn_off, flicker, change_line, run_line;

    ImageView battery_state;                    //green red orange image

    ArrayList<Led_action> action_list;          //the array which will hold all the actions in order
    Action_list_adapter adapter;                //the adapater for the list

    ListView action_list_view;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    //initialize all the objects on screen
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        turn_on = (Button) findViewById(R.id.Btn_turn_on);
        turn_off = (Button) findViewById(R.id.Btn_turn_off);             //initialization
        flicker = (Button) findViewById(R.id.Btn_flicker);
        change_line = (Button) findViewById(R.id.Btn_change_line);
        run_line = (Button) findViewById(R.id.Btn_run_line);

        action_list_view = (ListView) findViewById(R.id.action_list_view);

        battery_state = (ImageView)findViewById(R.id.Battery_state_image);

        BatteryReciever br = new BatteryReciever(battery_state);
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(br, filter); //this will change the image according to the battery level


        action_list = new ArrayList<>();          //the array which will hold all the actions in order
        adapter = new Action_list_adapter(MainActivity.this, R.layout.led_action_layout, action_list);


        Intent calling_intent =  getIntent();       //the intent that called this activity usually the log in activity which holds the user name and action string

        user_name = calling_intent.getStringExtra("user_name");
        user_action_string = calling_intent.getStringExtra("action_string"); // get the user name and action string from the intent

        generate_list_from_action_string(user_action_string);           //use the action string to generate the current list for the user

        action_list_view.setAdapter(adapter);


        action_list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {     //on long clicks i want to remove items from the list
                AlertDialog.Builder alert_dialog = new AlertDialog.Builder(MainActivity.this);

                alert_dialog.setTitle("Delete this action");
                alert_dialog.setMessage("Do you wish to delete this action");
                alert_dialog.setCancelable(true);                   //create the dialog that will remove from the list

                alert_dialog.setNegativeButton("No", null);     //on no just close the dialog
                alert_dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { // on yes delete from list and update it
                        action_list.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });


                alert_dialog.show(); //show the dialog

                return true;
            }
        });
    }


    public void simple_turn_on_off_buttons_method(final View view)
    //the method for turn on/off buttons create an action and either run it or save in the list view
    {
        final Dialog d = new Dialog(this);

        d.setContentView(R.layout.simple_action_dialog);
        d.setTitle("Choose time");                          //create the dialog
        d.setCancelable(true);

        final int action_type;
        switch (view.getId()) {             //determine if the action was turn on or turn off
            case R.id.Btn_turn_on:
                action_type = 0;
                break;

            case R.id.Btn_turn_off:
                action_type = 1;
                break;

            default:
                action_type = -1;

        }


        Button btn_save = (Button) d.findViewById(R.id.simple_action_dialog_btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) // the action for the button in the dialog it will generate a new line in the list based on the action and the time
            {
                EditText get_time = (EditText) d.findViewById(R.id.simple_action_dialog_edit_text_time);
                String time_text = get_time.getText().toString().trim();

                if(time_text.isEmpty()) //check the input for the time
                {
                    Toast.makeText(getApplicationContext(), "put a time value in the time box", Toast.LENGTH_LONG).show();
                    return;
                }

                int time = Integer.parseInt(time_text);

                Led_action curr_action = new Led_action(action_type, time);

                action_list.add(curr_action);
                adapter.notifyDataSetChanged();

                d.dismiss();
            }
        });


        Button btn_run_now = (Button) d.findViewById(R.id.simple_action_dialog_run_now_button);
        btn_run_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //this button will send the current action to server without changing the action string in the server
                String action_type_str;

                if (action_type == 0)
                    action_type_str = "ton";

                else
                    action_type_str = "tof";

                EditText get_time = (EditText) d.findViewById(R.id.simple_action_dialog_edit_text_time);
                String time_text = get_time.getText().toString().trim();

                if(time_text.isEmpty()) //check the input for the time
                {
                    Toast.makeText(getApplicationContext(), "put a time value in the time box", Toast.LENGTH_LONG).show();
                    return;
                }


                int time = Integer.parseInt(get_time.getText().toString());

                new Connection(getApplicationContext()).execute(user_name, "1" + action_type_str + "." + time + ".0"); // send the message to the server

                d.dismiss();
            }
        });

        d.show();

    }


    public void flicker_button_method(View view)
    //the method for flicker button create an action and either run it or save it in the list view
    {
        final Dialog d = new Dialog(this);

        d.setContentView(R.layout.flicker_action_dialog);
        d.setTitle("flicker action");                          //create the dialog
        d.setCancelable(true);


        Button btn_save = (Button) d.findViewById(R.id.flicker_action_dialog_save_btn);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // the button that will create a new line in the list
                EditText get_time = (EditText) d.findViewById(R.id.flicker_action_dialog_action_time);
                String time_text = get_time.getText().toString().trim();

                if(time_text.isEmpty()) //check the input for the time
                {
                    Toast.makeText(getApplicationContext(), "put a time value in the time box", Toast.LENGTH_LONG).show();
                    return;
                }

                int time = Integer.parseInt(time_text);

                EditText get_extra_params = (EditText) d.findViewById(R.id.flicker_action_dialog_add_params);
                String extra_params_text = get_extra_params.getText().toString().trim();

                if(extra_params_text.isEmpty()) //check the input for the flicker
                {
                    Toast.makeText(getApplicationContext(), "put a time value in the additional params box", Toast.LENGTH_LONG).show();
                    return;
                }

                int extra_params = Integer.parseInt(extra_params_text);


                Led_action curr_action = new Led_action(2, time, extra_params);

                action_list.add(curr_action);
                adapter.notifyDataSetChanged();

                d.dismiss();
            }
        });

        Button btn_run_now = (Button) d.findViewById(R.id.flicker_action_dialog_run_now_button);
        btn_run_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //button that will send one command to the server without changing the action string
                EditText get_time = (EditText) d.findViewById(R.id.flicker_action_dialog_action_time);
                String time_text = get_time.getText().toString().trim();

                if(time_text.isEmpty()) //check the input for the time
                {
                    Toast.makeText(getApplicationContext(), "put a time value in the time box", Toast.LENGTH_LONG).show();
                    return;
                }

                int time = Integer.parseInt(time_text);

                EditText get_extra_params = (EditText) d.findViewById(R.id.flicker_action_dialog_add_params);
                String extra_params_text = get_extra_params.getText().toString().trim();

                if(extra_params_text.isEmpty()) //check the input for the flicker
                {
                    Toast.makeText(getApplicationContext(), "put a time value in the additional params box", Toast.LENGTH_LONG).show();
                    return;
                }

                int extra_params = Integer.parseInt(extra_params_text);

                new Connection(getApplicationContext()).execute(user_name, "1f" + "." + time + ".0." + extra_params);

                d.dismiss();
            }
        });

        d.show();
    }


    public void change_in_server_button_method(View view)
    //this method will just change the action string in the serevr and won't run it
    {
        String curr_action_string = construct_action_String(); // this will generate using the list

        this.user_action_string = curr_action_string; // change the action string for the app

        new Connection(getApplicationContext()).execute(user_name, this.user_action_string); // send the action string to the server
    }


    public void run_current_line_button_method(View view)
    //just change and run the action string
    {
        String curr_action_string = construct_action_String(); // this will generate using the list

        this.user_action_string = curr_action_string; // change the action string for the app

        new Connection(getApplicationContext()).execute(user_name, this.user_action_string, "r");
    }


    public String construct_action_String()
    //this method will construct the action string from the list
    {
        String action_string = "n";

        for (int ind = 0; ind < action_list.size(); ++ind) { // traverse the list
            Led_action curr_action = action_list.get(ind);
            String curr_action_str = "";

            curr_action_str += curr_action.getAction_type_str();
            curr_action_str += ".";
            curr_action_str += curr_action.getAction_time();
            curr_action_str += ".";
            curr_action_str += curr_action.getLed_num();
            curr_action_str += ".";                             // build the current action

            if (curr_action.getAdditional_params() != -1)    //if action is flicker then add the additional params to the action string
                curr_action_str += curr_action.getAdditional_params();

            action_string += curr_action_str;

            if (ind != action_list.size() - 1)          //if not last action put a _ for the next one
                action_string += "_";

        }


        return action_string;
    }


    public void clear_list_fab_method(View view)
    //the method for the fab with x to clear the list
    {
        action_list.clear();
        adapter.notifyDataSetChanged();

    }


    public void open_settings_fab_method(View view)
    // the method for the fab that will open the settings
    {
        Intent open_settings_intent = new Intent(this , Settings_activity.class);

        startActivityForResult(open_settings_intent , 2);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    //when returned from settings recreate the list view
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2)
        {
            clear_list_fab_method(null); //make sure the list is clear
            generate_list_from_action_string(user_action_string);//generate the new list
        }
    }


    private void generate_list_from_action_string(String user_action_string)
    //create the list from the action string
    {
        if(user_action_string.isEmpty())
            return;

        String[] action_array = user_action_string.split("_"); //split string to separate actions

        for(int ind = 0; ind < action_array.length ; ind ++) //traverse all actions
        {
            String curr_action = action_array[ind];

            String[] arg_array = curr_action.split("\\.");  // \\ is required because of re

            String action_type = arg_array[0];
            int action_time = Integer.parseInt(arg_array[1]);
            int action_operand = Integer.parseInt(arg_array[2]);
            int additional_params = -1;  //default value

            if(arg_array.length > 3) //if additional params avilable then add them
                additional_params = Integer.parseInt(arg_array[3]);

            int action_type_num = -1;
            if(action_type.equals("ton"))
                action_type_num = 0;
            else if(action_type.equals("tof"))
                action_type_num = 1;
            else if(action_type.equals("f"))
                action_type_num = 2;        //determine correct number for action type



            Led_action led_action = new Led_action(action_type_num , action_time , additional_params);
            action_list.add(led_action);  //create and add the action to the list

        }

    adapter.notifyDataSetChanged(); //refresh the list
    }




}
