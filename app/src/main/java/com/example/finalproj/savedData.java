package com.example.finalproj;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class savedData {
    Context context;

    public savedData(Context context) {
        this.context = context;
    }

    public String readSavedEmail(){
        String savedEmail = null;
        try {
            FileInputStream inputStream = context.openFileInput("savedEmail1.txt"); // replace "filename.txt" with the name of the file where you stored the word
            int length = inputStream.available();
            byte[] buffer = new byte[length];
            inputStream.read(buffer);
            savedEmail = new String(buffer);
            inputStream.close();

            // do something with the word, for example, log it
        } catch (Exception e) {
            e.printStackTrace();
        }
        return savedEmail;
    }

    public static void writeSavedEmail(Context context, byte[] email) {
        try {
            FileOutputStream outputStream = context.openFileOutput("savedEmail1.txt", Context.MODE_PRIVATE);
            outputStream.write(email);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readStoredEmail() {
        String storedEmail = null;
        try {
            FileInputStream inputStream = context.openFileInput("storedEmail1.txt"); // replace "filename.txt" with the name of the file where you stored the word
            int length = inputStream.available();
            byte[] buffer = new byte[length];
            inputStream.read(buffer);
            storedEmail = new String(buffer);
            inputStream.close();

            // do something with the word, for example, log it
        } catch (Exception e) {
            e.printStackTrace();
        }
        return storedEmail;
    }

    public static void writeStoredEmail(Context context, byte[] email) {
        try {
            FileOutputStream outputStream = context.openFileOutput("storedEmail1.txt", Context.MODE_PRIVATE);
            outputStream.write(email);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeNameWeight(Context context, byte[] email) {
        String em = new String(email, StandardCharsets.UTF_8);
        String name = fetchUserData(em)[0];
        String weight = fetchUserData(em)[1];
        Log.d("namw", name + " " +weight);
        try {
            FileOutputStream outputStream = context.openFileOutput("storedNameWeight1.txt", Context.MODE_PRIVATE);
            outputStream.write((name + "," + weight).getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readStoredName() {
        String storedEmail = null;
        try {
            FileInputStream inputStream = context.openFileInput("storedNameWeight1.txt"); // replace "filename.txt" with the name of the file where you stored the word
            int length = inputStream.available();
            byte[] buffer = new byte[length];
            inputStream.read(buffer);
            storedEmail = new String(buffer);
            inputStream.close();

            // do something with the word, for example, log it
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] splited = storedEmail.split(",", -1);
        return splited[0];
    }

    public String readStoredWeight() {
        String storedEmail = null;
        try {
            FileInputStream inputStream = context.openFileInput("storedNameWeight1.txt"); // replace "filename.txt" with the name of the file where you stored the word
            int length = inputStream.available();
            byte[] buffer = new byte[length];
            inputStream.read(buffer);
            storedEmail = new String(buffer);
            inputStream.close();

            // do something with the word, for example, log it
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("read", storedEmail);
        String[] splited = storedEmail.split(",", -1);
        return splited[1];
    }

    public static String[] fetchUserData(String email){
        String name;
        int weight = 0;
        ClientHandler client = new ClientHandler("fetchData," + email);
        Log.d("userdata",client.getReturnMessage());
        String[] messageData = client.getReturnMessage().split(",", -1);
        name = messageData[0];
        try {
            weight = Integer.parseInt(messageData[1]);
        }catch (Exception ignored){}

        return new String[]{name, String.valueOf(weight)};
    }

    public static void saveChanges(String email, String date, List<Workouts> workouts){
        String workoutData = "";
        for (int i = 0; i < workouts.size(); i++) {
            if(Objects.equals(workouts.get(i).getDate(), date)){
                workoutData = workoutData + workouts.get(i).getName() + ",";
                workoutData = workoutData + workouts.get(i).getWeight() + ",";
                workoutData = workoutData + workouts.get(i).getSets() + ",";
                workoutData = workoutData + workouts.get(i).getReps() + ",";
                workoutData = workoutData + workouts.get(i).getBodyW() + ",";
            }
        }
        if(workoutData.length()>0){
            workoutData = workoutData.substring(0, workoutData.length()-1);
        }
        Log.d("data", workoutData);

        ClientHandler client = new ClientHandler("workoutData," + email + "," + date + "," + workoutData);
    }
}
