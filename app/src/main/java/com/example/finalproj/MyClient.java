package com.example.finalproj;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class MyClient implements Runnable{
    String server;
    int port;

    String toSend;
    Socket s;

    String returnString, returnS;


    public MyClient(String toSend) {
        this.server = "192.168.0.104";
        this.port = 1888;
        this.toSend = toSend;
    }

    public String getReturnString() {
        return returnString;
    }

    @Override
    public void run() {
        try {
            s = new Socket(server,port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (
                PrintWriter out =
                        new PrintWriter(s.getOutputStream(), true);
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(s.getInputStream()));
        ) {
            out.println(toSend);
            returnString = in.readLine();

            //returnString = returnS;
            s.close();


        } catch (IOException ignored) {
        }


    }
}