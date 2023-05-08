package com.example.peertopeerfeedbackapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class JoinNetworkActivity extends AppCompatActivity {

    private TextView infoText;

    private String networkIp;

    private String command;

    private boolean clientCarryOn = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String localIp = intent.getStringExtra("localIp");
        networkIp = intent.getStringExtra("networkId");
        setContentView(R.layout.activity_main3);

        infoText = findViewById(R.id.textView3);

        infoText.setText(networkIp);

        command = "hello";

        Thread clientThread = new Thread(new MyClientThread());
        clientThread.start();
    }

    class MyClientThread implements Runnable {
        @Override
        public void run() {

            try {
                Socket connectionToServer = new Socket(networkIp, 4444);
                DataInputStream inClientStream = new DataInputStream(connectionToServer.getInputStream());
                DataOutputStream outClientStream = new DataOutputStream(connectionToServer.getOutputStream());
                String messageFromServer;

                while (clientCarryOn) {

                    //logic for client

                    outClientStream.writeUTF(command);
                    outClientStream.flush();
                    messageFromServer = inClientStream.readUTF();
                    waitABit();
                }//while clientCarryOn

                connectionToServer.shutdownInput();
                connectionToServer.shutdownOutput();
                connectionToServer.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

        }//run()
    } //class MyClientThread


    private void waitABit() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}