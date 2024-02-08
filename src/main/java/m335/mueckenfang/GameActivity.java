package m335.mueckenfang;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener, Runnable {

    private static final long HOECHSTALTER_MS = 200;
    // other elements
    private final Random zufallsgenerator = new Random();
    private final Handler handler = new Handler();
    // fields
    private boolean spielLaeuft;
    private int runde, punkte, muecken, gefangeneMuecken, zeit;
    private float masstab;
    // controls
    private TextView txtPoints, txtRound, txtHits, txtTime;
    private FrameLayout spielbereich, frmHits, frmTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        txtPoints = findViewById(R.id.points);
        txtRound = findViewById(R.id.round);
        txtHits = findViewById(R.id.hits);
        txtTime = findViewById(R.id.time);

        spielbereich = findViewById(R.id.spielbereich);
        frmHits = findViewById(R.id.bar_hits);
        frmTime = findViewById(R.id.bar_time);

        masstab = getResources().getDisplayMetrics().scaledDensity;

        spielStarten();
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
        zeit = 10;
        bildschirmAktualisieren();

        handler.postDelayed(this, 1000);
    }

    @SuppressLint("SetTextI18n")
    private void bildschirmAktualisieren() {
        // statusleiste aktualisieren
        txtRound.setText(Integer.toString(runde));
        txtPoints.setText(Integer.toString(punkte));

        // treffer aktualisieren
        ViewGroup.LayoutParams lpHits = frmHits.getLayoutParams();
        lpHits.width = Math.round(masstab * 300 * Math.min(gefangeneMuecken, muecken) / muecken);
        txtHits.setText(Integer.toString(gefangeneMuecken));

        // zeit aktualisieren
        ViewGroup.LayoutParams lpTime = frmTime.getLayoutParams();
        lpTime.width = Math.round(masstab * zeit * 300 / 60);
        txtTime.setText(Integer.toString(zeit));
    }

    @Override
    public void run() {
        if (spielLaeuft) {
            zeitHerunterzaehlen();
        }
    }

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

        mueckenVerschwinden();
        bildschirmAktualisieren();

        if (!pruefeSpielEnde()) {
            if (!pruefeRundenende()) {
                handler.postDelayed(this, 1000);
            }
        }
    }

    private boolean pruefeRundenende() {
        if (gefangeneMuecken >= muecken) {
            starteRunde();
            return true;
        }
        return false;
    }

    private boolean pruefeSpielEnde() {
        if (zeit == 0 && gefangeneMuecken < muecken) {
            gameOver();
            return true;
        }
        return false;
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

    private void eineMueckeAnzeigen() {
        int hoehe = spielbereich.getWidth();
        int breite = spielbereich.getHeight();

        int muecke_breite = Math.round(masstab * 50);
        int muecke_hoehe = Math.round(masstab * 42);

        int links = zufallsgenerator.nextInt(breite - muecke_breite);
        int oben = zufallsgenerator.nextInt(hoehe - muecke_hoehe);

        ImageView muecke = new ImageView(this);
        muecke.setImageResource(R.drawable.muecke);
        muecke.setTag(R.id.geburtsdatum, new Date());
        muecke.setOnClickListener(this);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(muecke_breite, muecke_hoehe);
        params.leftMargin = links;
        params.topMargin = oben;
        params.gravity = Gravity.TOP + Gravity.START;

        spielbereich.addView(muecke, params);
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
        dialog.findViewById(R.id.btnOk).setOnClickListener((View view) -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("punkte", punkte);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
        dialog.show();
        spielLaeuft = false;
    }
}