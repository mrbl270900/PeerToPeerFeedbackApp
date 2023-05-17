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

    private String localIp;

    private Network network;

    int clientNumber = 0;

    private boolean clientCarryOn;

    private Boolean sendCommand;

    private String command;

    private String body;

    private Response response;

    private String networkIp;

    private Thread serverThread = new Thread(new MyServerThread());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        localIp = intent.getStringExtra("localIp");
        network = new Network(localIp);

        setContentView(R.layout.activity_main2);

        infoText = findViewById(R.id.textView2);

        infoText.setText(localIp);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        serverThread.start();

    }

    class MyServerThread implements Runnable {
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
                        networkIp = clientSocket.getRemoteSocketAddress().toString();
                        Thread clientThread = new Thread(new MyClientThread());
                        clientThread.start();

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

                network.addPeer(client.getRemoteSocketAddress().toString(), client.getRemoteSocketAddress().toString());

                //Start conversation
                while (serverCarryOn) {
                    try {
                        str = (String) inNodeStream.readUTF();
                        Request request = HandleApi.readHttpRequest(str);
                        infoText.setText(request.method);
                        try {
                            if(request.method.equalsIgnoreCase("addComment")){//logic for commands

                            }else if(request.method.equalsIgnoreCase("addSubComment")){

                            }else if(request.method.equalsIgnoreCase("getData")){
                                network.addPost(new Post("mads", "ide", 123));
                                response = network.getPostList();
                            }else if(request.method.equalsIgnoreCase("likeComment")){

                            }else if(request.method.equalsIgnoreCase("endConnection")){

                            }else if(request.method.equalsIgnoreCase("likePost")){

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
                client.shutdownInput();
                client.shutdownOutput();
                client.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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

                    //do something with responce from server


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