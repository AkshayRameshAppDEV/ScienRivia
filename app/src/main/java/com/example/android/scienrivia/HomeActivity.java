package com.example.android.scienrivia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity
{
    Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Show and hide navigation bar (Immersive mode)
        View showAndHideBars = findViewById(R.id.mylayout);
        showAndHideBars.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN);

        playButton = (Button) findViewById(R.id.playButton);


    }

    public void playGame(View view)
    {
        Intent quizPage = new Intent(this, QuizActivity.class);
        startActivity(quizPage);
    }
}
