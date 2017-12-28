package com.example.android.scienrivia;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
    protected void onCreate(Bundle savedInstanceState) {
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


        if (isInternetOn()) {
            playButton.setEnabled(true);
        } else {
            playButton.setEnabled(false);
            increaseButton.setEnabled(false);
            decreaseButton.setEnabled(false);
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.mylayout), "NO INTERNET CONNECTION", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onRestart();
                        }
                    });

// Changing message text color
            snackbar.setActionTextColor(Color.RED);

// Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }


    }

    @Override
    protected void onRestart() {


        super.onRestart();
        Intent i = new Intent(this, HomeActivity.class);  //your class
        startActivity(i);
        finish();

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


    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

            // if connected with internet


            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {


            return false;
        }
        return false;
    }
}
