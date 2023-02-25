package com.example.finalproj;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class Client extends AsyncTask<String,Void,Void> {
    Socket s;
    DataOutputStream dos;
    PrintWriter pw;

    @Override
    protected Void doInBackground(String... voids) {

        String message = voids[0];
        try {
            //s = new Socket("5.tcp.eu.ngrok.io", 18857);
            s = new Socket("192.168.108.203", 7800);
            pw = new PrintWriter(s.getOutputStream());

            String ip;
            try(final DatagramSocket socket = new DatagramSocket()){
                socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
                ip = socket.getLocalAddress().getHostAddress();
            }

            pw.write(message + " " + ip);
            pw.flush();
            pw.close();
            s.close();

        } catch (IOException e) {
           e.printStackTrace();
        }


        return null;
    }
}