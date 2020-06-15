package com.example.light_the_led;

public class Led_action
//class that represents a single led action this will hold all the paramaters of an action and the strings that will be displayed in the list view
{
    private int action_type ,action_time , additional_params;
    private int led_num = 0;

    private String action_type_str;
    private String action_type_display_str;
    private String additional_params_str;


    public Led_action(int action_type, int action_time , int additional_params)
    //constructor for flicker action
    {
        this.action_type = action_type;
        this.action_time = action_time;
        this.additional_params = additional_params;

        this.action_type_str = determine_action_type_str(action_type);
        this.action_type_display_str = determine_action_type_display_str(action_type);
        this.additional_params_str = determine_additional_params_str(action_type , additional_params);
    }


    public Led_action(int action_type, int action_time)
    //constructor for basic turn on/off actions
    {
        this.action_type = action_type;
        this.action_time = action_time;
        this.additional_params = -1;

        this.action_type_str = determine_action_type_str(action_type);
        //this.additional_params_str = "no additional parameters";
        this.additional_params_str = determine_additional_params_str(action_type , -1);
        this.action_type_display_str = determine_action_type_display_str(action_type);
    }


    private String determine_action_type_str(int action_type)
    //determine the string that will be displayed in the list view
    {
        switch(action_type)
        {
            case(0):                        // 0 == turn on
                return("ton");

            case(1):                        // 1 = turn off
                return("tof");

            case(2):                        // 2 = flicker
                return("f");

            default:
                return("");
        }
    }


    private String determine_action_type_display_str(int action_type)
    //determine the string that will be displayed in the list view
    {
        switch(action_type)
        {
            case(0):                        // 0 == turn on
                return("turn on");

            case(1):                        // 1 = turn off
                return("turn off");

            case(2):                        // 2 = flicker
                return("flicker");

            default:
                return("no action");
        }
    }


    private String determine_additional_params_str(int action_type , int additional_params)
    //determine the string that will be displayed in the list view
    {
        switch(action_type)
        {
            case(2):            //2 = flicker
                return("time between flicker " + additional_params  + " seconds");

            default:
                return("no additional parameters");
        }

    }

    //getters setterss

    public int getAction_type() {
        return action_type;
    }

    public int getAction_time() {
        return action_time;
    }

    public int getAdditional_params() {
        return additional_params;
    }

    public int getLed_num() {
        return led_num;
    }

    public String getAction_type_str() {
        return action_type_str;
    }

    public String getAdditional_params_str() {
        return additional_params_str;
    }

    public void setAction_type(int action_type) {
        this.action_type = action_type;
    }

    public void setAction_time(int action_time) {
        this.action_time = action_time;
    }

    public void setAdditional_params(int additional_params) {
        this.additional_params = additional_params;
    }

    public void setLed_num(int led_num) {
        this.led_num = led_num;
    }

    public void setAction_type_str(String action_type_str) {
        this.action_type_str = action_type_str;
    }

    public void setAdditional_params_str(String additional_params_str) {
        this.additional_params_str = additional_params_str;
    }

    public String getAction_type_display_str() {
        return action_type_display_str;
    }

    public void setAction_type_display_str(String action_type_display_str) {
        this.action_type_display_str = action_type_display_str;
    }
}
