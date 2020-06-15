package com.example.light_the_led;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;




public class Connection extends AsyncTask<String, String, String>
// class to handle the conversation with server
{
    static String IP_ADDRESS = "192.168.1.16";
    static int PORT = 12345;

    private final int BUFFER_SIZE = 1024;

    private final String LOG_TAG = "Connection";

    private Socket socket;

    private Scanner in;
    private PrintWriter out;

    String return_result = "";

    private boolean connected_to_server;
    private Context context;

    public Connection(Context context)
    {
        connected_to_server = true;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings)
    //the main method for the class this will send the messages in order to the server and get the response
    {
        try {
        this.socket = new Socket(IP_ADDRESS, PORT);

    } catch (IOException e) //create a socket and connect to server
        {
            connected_to_server = false;
            return "f";  //failed to connect
            //e.printStackTrace();
        }

        try {
            this.in = new Scanner(this.socket.getInputStream());

        } catch (IOException e) {                   //create a scanner for reading sent messages from server
            e.printStackTrace();
        }


        try {
            this.out = new PrintWriter(this.socket.getOutputStream() , true);

        } catch (IOException e) {                   //create a writer to send messages to server
            e.printStackTrace();
        }


    //---------------------------------------------------------------------------------------------------------------------


        int strings_amount = strings.length;                //the amount of strings to send
        String received_message = "";

        for(int curr_str = 0; curr_str < strings_amount ; curr_str++)
        {

            send(strings[curr_str]);


            try {

                 received_message =  recv();

            } catch (IOException e) {
                e.printStackTrace();
            }


            if(!received_message.equals("1")) {

                if (curr_str == 0)                   //first string is always user_name
                    this.return_result = received_message;  //we get latest action string from server


                else if (strings[curr_str].charAt(0) == 'n') //if the first character is n we need to change the action string
                    this.return_result = strings[curr_str].substring(1);

            }
        }




    //-------------------------------------------------------------------------------------------------------------------

        try {
            close_connection();

        } catch (IOException e) {           //close the connection to the server
            e.printStackTrace();
        }



        return this.return_result;
    }


    private void close_connection() throws IOException
    //stop the connection with the server
    {
        Log.d(LOG_TAG , "closing the connection with the server");
        this.socket.close();
    }



    private void send(String dataToSend)
    //send a message to the server
    {
        Log.d(LOG_TAG , "sending message to server: " + dataToSend);
        this.out.write(dataToSend);

        this.out.flush();
    }


    private String recv() throws IOException
    //receive message from server
    {
        String message = this.in.nextLine();

        Log.d(LOG_TAG , "receiving message from server: " + message);
        return message;


    }

    @Override
    protected void onPostExecute(String s)
    //this will be used to raise a toast if couldn't connect to server
    {
        super.onPostExecute(s);
        if(!connected_to_server)
            Toast.makeText(context , "can't connect to server" , Toast.LENGTH_LONG).show();
    }
}




