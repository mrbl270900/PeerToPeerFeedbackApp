package com.example.peertopeerfeedbackapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class JoinNetworkActivity extends AppCompatActivity {

    private TextView infoText;

    private String networkIp;

    private boolean clientCarryOn;

    private Boolean sendCommand;

    private String command;

    private String body;

    private Response response;

    private boolean serverCarryOn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String localIp = intent.getStringExtra("localIp");
        networkIp = intent.getStringExtra("networkId");
        setContentView(R.layout.activity_main3);

        infoText = findViewById(R.id.textView3);

        infoText.setText(networkIp);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Thread clientThread = new Thread(new MyClientThread());
        Thread serverThread = new Thread(new ServerClient());
        serverThread.start();
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
                clientCarryOn = true;
                sendCommand = true;
                command = "getData";
                body = "";

                while (clientCarryOn) {
                    //logic for client
                    if(sendCommand) {
                        outClientStream.writeUTF(new Request(command, body).toString());
                        outClientStream.flush();
                        messageFromServer = inClientStream.readUTF();
                        response = HandleApi.readHttpResponse(messageFromServer);
                        waitABit();
                    }
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

    class ServerClient extends Thread {
        public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(4444);
                Socket clientSocket = serverSocket.accept();
                DataInputStream inNodeStream = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream outNodeStream = new DataOutputStream(clientSocket.getOutputStream());
                String str;
                String response = "ok";
                String status = "200 ok";
                serverCarryOn = true;

                //Start conversation
                while (serverCarryOn) {
                    try {
                        str = (String) inNodeStream.readUTF();
                        Request request = HandleApi.readHttpRequest(str);
                        try {
                            if(request.method.equalsIgnoreCase("newData")){//logic for commands

                            }else if(request.method.equalsIgnoreCase("endnetwork")){

                            }else {
                                status = "400 bad rec";
                                response = "Fail";
                            }
                        } catch (RuntimeException e) {
                            e.printStackTrace();
                            System.out.println("server fail on command");
                            status = "400 bad rec";
                            response = "Fail";
                            serverCarryOn = false;
                        }

                        String jsonString = status + response;
                        outNodeStream.writeUTF(jsonString);
                        outNodeStream.flush();
                        waitABit();
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }//serverCarryOn loop

                //Closing everything down
                clientSocket.shutdownInput();
                clientSocket.shutdownOutput();
                clientSocket.close();
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