package com.example.android.scienrivia;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity
{
    Button playButton;
    Button decreaseButton, increaseButton;
    TextView updateTextView;
    int updateTextViewCounter = 5;

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


        decreaseButton = (Button) findViewById(R.id.decreaseButton);
        decreaseButton.setEnabled(false);
        increaseButton = (Button) findViewById(R.id.increaseButton);
        updateTextView = (TextView) findViewById(R.id.updateTextView);
        playButton = (Button) findViewById(R.id.playButton);


    }


    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Are you sure you want to exit ScienRivia ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //if user pressed "yes", then he is allowed to exit from application
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void close(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Are you sure you want to exit ScienRivia ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //if user pressed "yes", then he is allowed to exit from application
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                int pid = android.os.Process.myPid();
                android.os.Process.killProcess(pid);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();






    }

    public void incButton(View view) {
        decreaseButton.setEnabled(true);
        updateTextViewCounter++;
        if (updateTextViewCounter == 20) {
            increaseButton.setEnabled(false);
            updateTextView.setText("" + updateTextViewCounter);
        } else {
            updateTextView.setText("" + updateTextViewCounter);
        }

    }

    public void decButton(View view) {
        increaseButton.setEnabled(true);
        updateTextViewCounter--;
        if (updateTextViewCounter == 5) {

            decreaseButton.setEnabled(false);
            updateTextView.setText("" + updateTextViewCounter);
        } else {
            updateTextView.setText("" + updateTextViewCounter);
        }
    }

    public void playGame(View view)
    {
        Intent quizPage = new Intent(this, QuizActivity.class);
        startActivity(quizPage);
    }
}
