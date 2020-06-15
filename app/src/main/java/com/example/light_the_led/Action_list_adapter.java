package com.example.light_the_led;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class Action_list_adapter extends ArrayAdapter<Led_action>
//this class will put the correct layout for every row in the list view
{

    Context context;
    int resource;

    public Action_list_adapter(Context context, int resource, List<Led_action> objects)
    //constructor for the class gets the layout for the action
    {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    //this method needs to be overridden
    {
        Led_action curr_action = getItem(position);


        String action_type =  curr_action.getAction_type_display_str();
        int action_time = curr_action.getAction_time();
        int led_num = curr_action.getLed_num();
        String additional_params = curr_action.getAdditional_params_str();//the information about the current action


        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(this.resource , parent ,false); //the layout for the row

        TextView action_type_text_view = (TextView)view.findViewById(R.id.Action_type);
        TextView action_time_text_view = (TextView)view.findViewById(R.id.Action_time);
        TextView action_led_text_view = (TextView)view.findViewById(R.id.Action_led_num);
        TextView action_add_params_text_view = (TextView)view.findViewById(R.id.Action_additional_params);//the views in the row



        action_type_text_view.setText(action_type);
        action_time_text_view.setText(action_time + " seconds");
        action_led_text_view.setText("led num: " + led_num);
        action_add_params_text_view.setText(additional_params);//setting all the information

        return view;
    }
}
