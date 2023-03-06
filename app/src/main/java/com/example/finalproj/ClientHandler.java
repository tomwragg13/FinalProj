package com.example.finalproj;

public class ClientHandler {
    String message;

    String returnMessage;

    public ClientHandler(String message) {
        this.message = message;

        MyClient myClient = new MyClient(message);
        Thread myThread = new Thread(myClient);
        myThread.start();

        try {
            myThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        returnMessage = myClient.getReturnString().substring(1);
    }

    public String getReturnMessage() {
        return returnMessage;
    }
}
