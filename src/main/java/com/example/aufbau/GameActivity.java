package com.example.aufbau;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements android.view.View.OnClickListener, Runnable {


    private Random zufallsgenerator = new Random();
    private ViewGroup spielbereich;
    private static final long HOECHSTALTER_MS = 2000;
    private Handler handler = new Handler();

    // TEACHER STUFF///


    // values
    private boolean spielLaeuft;
    private int runde;
    private int punkte;
    private int muecken;
    private int gefangeneMuecken;
    private int zeit;
    private float massstab;

    // controls
    private TextView txtPoints;
    private TextView txtRound;

    private FrameLayout frameHitsDisplay;
    private TextView txtHits;

    private FrameLayout frameTimedDisplay;
    private TextView txtTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        // load controls
        txtPoints = findViewById(R.id.textViewPoints);
        txtRound = findViewById(R.id.textViewRound);
        frameHitsDisplay = findViewById(R.id.bar_hits);
        txtHits = findViewById(R.id.hits);
        frameTimedDisplay = findViewById(R.id.bar_time);
        txtTime = findViewById(R.id.time);

        // save massstab for later
        massstab = getResources().getDisplayMetrics().density;

        spielStarten();
    }

    // Ein neues Spiel starten
    // Zweck: .........................
    private void spielStarten() {
        spielLaeuft = true;
        runde = 0;        // ...
        punkte = 0;        // ...
        starteRunde();
    }

    // Eine Runde starten
    // Zweck: .........................
    private void starteRunde() {
        runde = runde + 1;        // ...
        muecken = runde * 10;    // ...
        gefangeneMuecken = 0;    // ...
        zeit = 60;    // ...
        bildschirmAktualisieren();
    }

    private void bildschirmAktualisieren() {
        txtRound.setText(Integer.toString(runde));
        txtPoints.setText(Integer.toString(punkte));

        ViewGroup.LayoutParams lpHits = frameHitsDisplay.getLayoutParams();
        lpHits.width = Math.round(massstab * 300 * Math.min(gefangeneMuecken, muecken) / muecken);
        txtHits.setText(Integer.toString(gefangeneMuecken));

        ViewGroup.LayoutParams lptime = frameTimedDisplay.getLayoutParams();
        lptime.width = Math.round(massstab * zeit * 300 / 60);
        txtTime.setText(Integer.toString(zeit));
        lptime.width = Math.round(massstab * zeit * 300 / 60);
        // TODO: idk if its right here
        float zufallszahl = zufallsgenerator.nextFloat();

        //
        if (zufallszahl < muecken * 1.5 / 60) {
            eineMueckeAnzeigen();
        }

        //
        double wahrscheinlichkeit = muecken * 1.5f / 60;
        if (wahrscheinlichkeit > 1) {
            eineMueckeAnzeigen();
            if (zufallszahl < wahrscheinlichkeit - 1) {
                eineMueckeAnzeigen();
            }
        } else {
            if (zufallszahl < wahrscheinlichkeit) {
                eineMueckeAnzeigen();
            }
        }
    }

    // TEACHER STUFF///DONE///


    // s 157
    private void zeitHerunterzaehlen() {
        zeit = zeit - 1;
        float zufallszahl = zufallsgenerator.nextFloat();
        double wahrscheinlichkeit = muecken * 1.5;
        if ( wahrscheinlichkeit > 1) {
            eineMueckeAnzeigen();
            if (zufallszahl < wahrscheinlichkeit -1 )
            {
                eineMueckeAnzeigen();
            }
        } else {
            if (zufallszahl < wahrscheinlichkeit) {
                eineMueckeAnzeigen();
            }
        }

        mueckenVerschwinden();

        if (!pruefeSpielEnde()) {
            if (!pruefeRundenende())
            {
                handler.postDelayed(this, 100);
            }
        }
    }

    // 157

    private boolean pruefeSpielEnde() {
        if (zeit == 0 && gefangeneMuecken < muecken) {
            gameOver();
            return true;
        }
        return false;
    }

    // 159
    private boolean pruefeRundenende() {
        if (gefangeneMuecken >= muecken) {
            starteRunde();
            return true;
        }
        return false;
    }

    private void eineMueckeAnzeigen() {
        int hoehe = spielbereich.getWidth();
        int breite = spielbereich.getHeight();

        int muecke_breite = Math.round(massstab*50);
        int muecke_hoehe = Math.round(massstab*42);

        int links = zufallsgenerator.nextInt(breite-muecke_breite);
        int oben = zufallsgenerator.nextInt(hoehe-muecke_hoehe);

        ImageView muecke = new ImageView(this);
        muecke.setImageResource(R.drawable.fly_fly);
        muecke.setTag(R.id.geburtsdatum, new Date());
        muecke.setOnClickListener(this);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(muecke_breite, muecke_hoehe);
        params.leftMargin = links;
        params.topMargin = oben;
        params.gravity = Gravity.TOP + Gravity.LEFT;

        spielbereich.addView(muecke,params);
    }

    private void mueckenVerschwinden() {
        int nummer = 0;
        while (nummer < spielbereich.getChildCount()) {
            ImageView muecke = (ImageView) spielbereich.getChildAt(nummer);
            Date geburtsdatum = (Date)muecke.getTag(R.id.geburtsdatum);
            long alter = (new Date()).getTime() - geburtsdatum.getTime();
            if (alter > HOECHSTALTER_MS) {
                spielbereich.removeView(muecke);
            } else {
                nummer++;
            }
        }
    }

    @Override
    public void onClick(View muecke) {
        gefangeneMuecken++;
        punkte += 100;
        bildschirmAktualisieren();
    }

    private void gameOver() {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.gameover);
        dialog.show();
        spielLaeuft = false;
    }

    @Override
    public void run() {
        zeitHerunterzaehlen();
    }
}