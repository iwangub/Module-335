package com.example.aufbau;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements android.view.View.OnClickListener, Runnable {

    boolean spielLaeuft;
    int runde;
    int punkte;
    private float massstab;
    int gefangeneMuecken;
    int muecken;
    int zeit;
    private Random zufallsgenerator = new Random();
    private ViewGroup spielbereich;
    private static final long HOECHSTALTER_MS = 2000;
    private Handler handler = new Handler();

    //    private int randomPicture = 0;
    Random rand = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        spielbereich = (ViewGroup) findViewById(R.id.spielbereich);
        massstab = getResources().getDisplayMetrics().density;
        spielStarten();

        Log.d("Test: ", "onCreate");

    }

    private void spielStarten() {
        spielLaeuft = true;
        runde = 0;
        punkte = 0;
        starteRunde();
    }

    private void starteRunde() {
        runde = runde + 1;
        muecken = runde * 10;
        gefangeneMuecken = 0;
        zeit = 60;
        bildschirmAktualisieren();

        handler.postDelayed(this, 1000);
    }

    private void bildschirmAktualisieren() {
        TextView tvPunkte = (TextView) findViewById(R.id.textViewPoints);
        tvPunkte.setText(Integer.toString(punkte));

        TextView tvRunde = (TextView) findViewById(R.id.textViewRound);
        tvRunde.setText(Integer.toString(runde));

        TextView tvTreffer = (TextView) findViewById(R.id.hits);
        tvTreffer.setText(Integer.toString(gefangeneMuecken));
        TextView tvZeit = (TextView) findViewById(R.id.time);
        tvZeit.setText(Integer.toString(zeit));

        FrameLayout flTreffer = (FrameLayout) findViewById(R.id.bar_hits);
        FrameLayout flZeit = (FrameLayout) findViewById(R.id.bar_time);

        ViewGroup.LayoutParams lpTreffer = flTreffer.getLayoutParams();

        lpTreffer.width = Math.round(massstab * 300 * Math.min(gefangeneMuecken, muecken) / muecken);

        ViewGroup.LayoutParams lpZeit = flZeit.getLayoutParams();
        lpZeit.width = Math.round(massstab * zeit * 300 / 60);
    }

    // s 157
    private void zeitHerunterzaehlen() {
        zeit = zeit - 1;
        float zufallszahl = zufallsgenerator.nextFloat();
        double wahrscheinlichkeit = muecken * 1.5;
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
        // TODO: Optional
        mueckenVerschwinden();
        bildschirmAktualisieren();
        if (!pruefeSpielende()) {
            pruefeRundenende();
        }
        // p175
        if (!pruefeSpielende()) {
            if (!pruefeRundenende()) {
                handler.postDelayed(this, 1000);
            }
        }
    }

    // 157
    private boolean pruefeSpielende() {
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
        // 160
        int breite = spielbereich.getWidth();
        int hoehe = spielbereich.getHeight();

        // 160
        int muecke_breite = Math.round(massstab * 100);
        int muecke_hoehe = Math.round(massstab * 84);

        // 161
        int links = zufallsgenerator.nextInt(breite - muecke_breite);
        int oben = zufallsgenerator.nextInt(hoehe - muecke_hoehe);

        ImageView muecke = new ImageView(this);
        int randomPicture = rand.nextInt(14) + 1;
        if (randomPicture == 1) {
            muecke.setImageResource(R.drawable.error_03);
        } else if (randomPicture == 2) {
            muecke.setImageResource(R.drawable.error_04);
        } else if (randomPicture % 2 == 0) {
            muecke.setImageResource(R.drawable.error_02);
        } else if (randomPicture % 2 == 1) {
            muecke.setImageResource(R.drawable.error_error);
        }

        muecke.setOnClickListener(this);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(muecke_breite, muecke_hoehe);
        params.leftMargin = links;
        params.topMargin = oben;
        params.gravity = Gravity.TOP + Gravity.LEFT;
        spielbereich.addView(muecke, params);

        muecke.setTag(R.id.geburtsdatum, new Date());


    }

    private void mueckenVerschwinden() {
        int nummer = 0;
        while (nummer < spielbereich.getChildCount()) {
            ImageView muecke = (ImageView) spielbereich.getChildAt(nummer);
            Date geburtsdatum = (Date) muecke.getTag(R.id.geburtsdatum);
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
        spielbereich.removeView(muecke);
    }

    public void gameOver() {
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