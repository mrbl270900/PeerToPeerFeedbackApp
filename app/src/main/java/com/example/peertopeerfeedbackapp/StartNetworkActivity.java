package com.example.peertopeerfeedbackapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class StartNetworkActivity extends AppCompatActivity {

    private TextView infoText;

    private boolean serverCarryOn = true;

    int clientNumber = 0;

    private Thread serverThread = new Thread(new MyServerThread());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String localIp = intent.getStringExtra("localIp");
        setContentView(R.layout.activity_main2);

        infoText = findViewById(R.id.textView2);

        infoText.setText(localIp);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        serverThread.start();

    }

    class MyServerThread implements Runnable { //TODO implemnt multi thread for server
        @SuppressLint("SuspiciousIndentation")
        @Override
        public void run() {
            //Always be ready for next client
            boolean running = true;
            while (true) {
                try {
                    ServerSocket serverSocket = new ServerSocket(4444);

                    //Always be ready for next client
                    while (true) {
                        Socket clientSocket = serverSocket.accept();
                        clientNumber++;
                        new RemoteClient(clientSocket, clientNumber).start();

                    }//while listening for clients

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    class RemoteClient extends Thread {
        private final Socket client;
        private int number;

        public RemoteClient(Socket clientSocket, int number) {
            this.client = clientSocket;
            this.number = number;
        }

        public void run() {
            try {
                DataInputStream inNodeStream = new DataInputStream(client.getInputStream());
                DataOutputStream outNodeStream = new DataOutputStream(client.getOutputStream());
                String str;
                String response = "ok";
                String status = "200 ok";
                serverCarryOn = true;
                //Start conversation
                while (serverCarryOn) {
                    try {
                        str = (String) inNodeStream.readUTF();
                        try {
                            if(str.equalsIgnoreCase("")){//logic for commands
                            //logic for what to do
                            } else {
                                status = "400 bad rec";
                                response = "Fail";
                            }
                        } catch (RuntimeException e) {
                            e.printStackTrace();
                            System.out.println("server fail on command");
                            status = "400 bad rec";
                            response = "Fail";
                        }

                        String jsonString = status + response;
                        outNodeStream.writeUTF(jsonString);
                        outNodeStream.flush();
                        waitABit();
                        serverCarryOn = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }//serverCarryOn loop

                //Closing everything down
                client.shutdownInput();
                client.shutdownOutput();
                client.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void waitABit() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}