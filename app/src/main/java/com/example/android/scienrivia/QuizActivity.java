package com.example.android.scienrivia;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class QuizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        //Show and hide navigation bar (Immersive mode)
        View showAndHideBars = findViewById(R.id.quizlayout);
        showAndHideBars.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
        DownloadTask task = new DownloadTask();
        task.execute("https://opentdb.com/api.php?amount=20&category=17&difficulty=easy&type=multiple");

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
        builder.setMessage("Are you sure ?");
        builder.setPositiveButton("Restart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //if user pressed "yes", then he is allowed to exit from application
                onRestart();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
                //Show and hide navigation bar (Immersive mode)
                View showAndHideBars = findViewById(R.id.quizlayout);
                showAndHideBars.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);


            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray results = jsonObject.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    String question = results.getJSONObject(i).getString("question");
                    String correctAnswer = results.getJSONObject(i).getString("correct_answer");
                    String incorrectOne = (String) results.getJSONObject(i).getJSONArray("incorrect_answers").get(0);
                    String incorrectTwo = (String) results.getJSONObject(i).getJSONArray("incorrect_answers").get(1);
                    String incorrectThree = (String) results.getJSONObject(i).getJSONArray("incorrect_answers").get(2);
                    String whole = question + "\n" + correctAnswer + "\n" + incorrectOne + "\n" + incorrectTwo + "\n" + incorrectThree;
                    Log.i("po", whole);
                }


//                    String correctAnswer = results.getJSONObject(0).getString("correct_answer");
//


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
