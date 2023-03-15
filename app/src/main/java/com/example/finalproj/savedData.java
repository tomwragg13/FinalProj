package com.example.finalproj;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
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

    public static void changeRecyclerSize(Context context, List<Workouts> workouts, RecyclerView recyclerView){
        final float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (75 * scale + 0.5f);
        if(workouts.size() < 6 && workouts.size() > 0){
            recyclerView.getLayoutParams().height = workouts.size()*pixels;
        }
        if(workouts.size() == 0){
            recyclerView.getLayoutParams().height = pixels;
        }
        if(workouts.size() > 5){
            recyclerView.getLayoutParams().height = 5*pixels;
        }
    }

    public static void addIfEmpty(RecyclerView recyclerView, Context context, List<Workouts> workouts, String email, String date, ImageView expandButton, float recyclerHeight,
                                  List<personalBests> PBs, String expandType, ConstraintLayout clickBlocker) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        workoutAdapter adapter = new workoutAdapter(context, workouts, email, date, recyclerView, expandButton, recyclerHeight, PBs, expandType, clickBlocker);
        recyclerView.setAdapter(adapter);
        if(workouts.size() == 0){

            workouts.add(new Workouts("Add Workouts", 0, 0, 0, 0, date));
        }
    }
}
