package com.example.aufbau;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MueckenfangActivity extends AppCompatActivity implements android.view.View.OnClickListener {
    private Button btn;
    private LinearLayout namenseingabe;
    private Button speichern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        btn = findViewById(R.id.btnStart);
        namenseingabe = findViewById(R.id.namenseingabe);
        speichern = findViewById(R.id.speichern);
        speichern.setOnClickListener(this);
        namenseingabe.setVisibility(View.INVISIBLE);

    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnStart) {
            startActivityForResult(new Intent(this, GameActivity.class),1);
        } else if(v.getId() == R.id.speichern) {
            schreibeHighscoreName();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode > leseHighscore()) {
                schreibeHighscore(resultCode);
            }
        }
        if (resultCode > leseHighscore()) {
            schreibeHighscore(resultCode);
            namenseingabe.setVisibility(View.VISIBLE);
        }
    }

    private int leseHighscore() {
        SharedPreferences pref = getSharedPreferences("GAME", 0);
        return pref.getInt("HIGHSCORE", 0);
    }

    private void schreibeHighscore(int highscore) {
        SharedPreferences pref = getSharedPreferences("GAME", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("HIGHSCORE", highscore);
        editor.commit();
    }

    private void highscoreAnzeigen() {
        TextView tv = findViewById(R.id.highscore);
        tv.setText(Integer.toString(leseHighscore()));
        int highscore = leseHighscore();
        if (highscore > 0) {
            tv.setText(Integer.toString(highscore) + " von " +
                    leseHighscoreName());
        } else {
            tv.setText("-");
        }
    }

    private void schreibeHighscoreName() {
        EditText et = (EditText) findViewById(R.id.spielername);
        String name = et.getText().toString().trim();
        SharedPreferences pref = getSharedPreferences("GAME", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("HIGHSCORE_NAME", name);
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        highscoreAnzeigen();
    }

    private String leseHighscoreName() {
        SharedPreferences pref = getSharedPreferences("GAME", 0);
        return pref.getString("HIGHSCORE_NAME", "");
    }
}