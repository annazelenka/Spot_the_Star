package com.example.annaz.mp6;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;

public class scoreScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_screen);

        Intent receivedIntent = getIntent();
        int score = receivedIntent.getIntExtra("score", 0);
        TextView scoreLabel = findViewById(R.id.scoreLabel);
        scoreLabel.setText("final score: " + score);
    }

    public void launchGameScreen(View v) {



        Intent i = new Intent(this, GameActivity.class);


        startActivity(i);

    }
}
