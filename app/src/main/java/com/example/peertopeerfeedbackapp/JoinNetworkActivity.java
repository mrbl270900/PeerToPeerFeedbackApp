package com.example.peertopeerfeedbackapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class JoinNetworkActivity extends AppCompatActivity {

    private TextView infoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String localIp = intent.getStringExtra("localIp");
        setContentView(R.layout.activity_main3);

        infoText = findViewById(R.id.textView3);

        infoText.setText(localIp);
    }
}