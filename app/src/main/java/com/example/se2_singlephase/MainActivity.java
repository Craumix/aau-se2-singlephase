package com.example.se2_singlephase;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    EditText numInput;
    TextView answerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numInput = (EditText) findViewById(R.id.editTextNumber);
        answerView = (TextView) findViewById(R.id.answerText);
    }

    public void onSend(View view) {
        new Thread(() -> {
            try {
                Socket conn = new Socket("se2-isys.aau.at", 53212);

                String matrikelNummer = numInput.getText().toString() + "\n";

                OutputStream out = conn.getOutputStream();
                InputStream in = conn.getInputStream();

                out.write(matrikelNummer.getBytes(StandardCharsets.UTF_8));

                byte[] buffer = new byte[512];
                String msg = "";
                int read;
                while((read = in.read(buffer)) != -1)
                    msg += new String(buffer, 0, read, StandardCharsets.UTF_8);

                final String serverAnswer = msg;
                runOnUiThread(() -> {
                    answerView.setText(serverAnswer);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void onCalculate(View view) {
        int quersumme = 0;
        for(char c : numInput.getText().toString().toCharArray())
            quersumme += Integer.parseInt(c + "");
        answerView.setText(Integer.toBinaryString(quersumme));
    }
}