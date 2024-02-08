package m335.mueckenfang;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ConstraintLayout highscoreAdderContainer, highscoreDisplayContainer;
    EditText tbxHighscorer;
    TextView txvHighscorer;
    Button btnSpeichern, btnStart;

    int punkte = 0;

    private final int GAME_REQUESTCODE = 335;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        highscoreAdderContainer = findViewById(R.id.highscoreAdderContainer);
        tbxHighscorer = findViewById(R.id.tbxHighscorer);
        btnSpeichern = findViewById(R.id.btnSpeichern);
        btnSpeichern.setOnClickListener(this);

        highscoreDisplayContainer = findViewById(R.id.highscoreDisplayContainer);
        txvHighscorer = findViewById(R.id.txvHighscorer);

        btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);

        highscoreLaden();
    }

    @Override
    public void onClick(View view) {
        if (view == btnStart) {
            Intent gameIntent = new Intent(this, GameActivity.class);
            startActivityForResult(gameIntent, GAME_REQUESTCODE);
        } else if (view == btnSpeichern) {
            String name = tbxHighscorer.getText().toString();
            highscoreSpeichern(name, punkte);
            displayHighscore(name, punkte);
            highscoreAdderContainer.setVisibility(View.GONE);
            btnStart.setVisibility(View.VISIBLE);
        } else {
            Log.e("M335", "Unbekannter ClickEvent versender");
        }
    }

    private void highscoreSpeichern(String name, int punkte) {
        SharedPreferences sharedPreferences = getSharedPreferences("HIGHSCORE", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Spieler", name);
        editor.putInt("Punkte", punkte);
        editor.apply();
    }

    private void highscoreLaden() {
        SharedPreferences sharedPreferences = getSharedPreferences("HIGHSCORE", MODE_PRIVATE);
        String name = sharedPreferences.getString("Spieler", "");
        int punkte = sharedPreferences.getInt("Punkte", 0);

        if (!name.equals("") && punkte > 0) {
            displayHighscore(name, punkte);
        }
    }

    private void displayHighscore(String name, int punkte) {
        String highscore = name + ": " + punkte;
        highscoreDisplayContainer.setVisibility(View.VISIBLE);
        txvHighscorer.setText(highscore);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GAME_REQUESTCODE && resultCode == Activity.RESULT_OK) {
            punkte = data.getIntExtra("punkte", 0);
            highscoreAdderContainer.setVisibility(View.VISIBLE);
            highscoreDisplayContainer.setVisibility(View.GONE);
            btnStart.setVisibility(View.GONE);
        }
    }
}