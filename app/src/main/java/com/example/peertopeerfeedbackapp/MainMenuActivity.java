package com.example.peertopeerfeedbackapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView infoText;

    private EditText networkIdInput;

    private Button startNetwork, joinNetwork;

    private String THIS_IP_ADDRESS = "";

    private String startButtonText = "Start network";

    private String joinButtonText = "Join a network";

    private String hint = "Input Network Id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        startNetwork = findViewById(R.id.button);
        joinNetwork = findViewById(R.id.button2);
        infoText = findViewById(R.id.textView);
        networkIdInput = findViewById(R.id.editTextText);

        startNetwork.setText(startButtonText);

        joinNetwork.setText(joinButtonText);

        startNetwork.setOnClickListener(this);
        joinNetwork.setOnClickListener(this);

        THIS_IP_ADDRESS = getLocalIpAddress();

        infoText.setText(THIS_IP_ADDRESS);

        networkIdInput.setHint(hint);
        networkIdInput.setText("");
    }//onCreate

    @Override
    public void onClick(View view) {
        if (view == startNetwork) {
            //logic for startNetwork
            Intent myIntent = new Intent(MainMenuActivity.this, StartNetworkActivity.class);
            myIntent.putExtra("localIp", THIS_IP_ADDRESS); //Optional parameters
            MainMenuActivity.this.startActivity(myIntent);
        }else if (view == joinNetwork){
            //logic for joinNetwork
            Intent myIntent = new Intent(MainMenuActivity.this, JoinNetworkActivity.class);
            myIntent.putExtra("localIp", THIS_IP_ADDRESS); //Optional parameters
            myIntent.putExtra("networkId", networkIdInput.getText().toString());
            MainMenuActivity.this.startActivity(myIntent);
        }
    }//onClick

    private String getLocalIpAddress() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        assert wifiManager != null;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipInt = wifiInfo.getIpAddress();
        String address = null;
        try {
            address = InetAddress.getByAddress(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ipInt).array()).getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        return address;
    }//getLocalIpAddress
}