package com.example.aufbau;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.util.ULocale;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    TextView txt;

    Button btn_good;
    Button btn_bad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aufgabe_12_multiple_btns);


        btn_good = findViewById(R.id.button9);
        btn_good = findViewById(R.id.button10);
        txt = findViewById(R.id.textView22);
    }




    public void btnGoodClick(View v) {
            txt.setText("Das Freut uns!");
    }
    public void btnBadClick(View v) {
        txt.setText("Das ist schade!");
    }

}