package com.example.aufbau;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.util.ULocale;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;


public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, android.view.View.OnClickListener {
    private Button button;
    private EditText editText;
    private TextToSpeech tts;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aufgabe_11_fixed);

        tts = new TextToSpeech(this, this);
        button = findViewById(R.id.button);
        editText = findViewById(R.id.editText);

        //button.setOnClickListener(this);
    }
/*
    @Override
    public void onClick(View v) {
        Bundle params = new Bundle();
        params.putInt(TextToSpeech.Engine.KEY_PARAM_STREAM, AudioManager.STREAM_MUSIC);
        tts.speak(editText.getText().toString(), TextToSpeech.QUEUE_FLUSH, params, null);
    }
*/
    public void btnClick(View v) {
        Bundle params = new Bundle();
        params.putInt(TextToSpeech.Engine.KEY_PARAM_STREAM, AudioManager.STREAM_MUSIC);
        tts.speak(editText.getText().toString(), TextToSpeech.QUEUE_FLUSH, params, null);
    }

    @Override
    public void onInit(int status) {

    }

    @Override
    public void onClick(View v) {

    }
}