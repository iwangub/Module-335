package com.example.aufbau;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MueckenfangActivity extends AppCompatActivity implements android.view.View.OnClickListener {
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        btn = findViewById(R.id.btnStart);

    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, GameActivity.class));
    }
}