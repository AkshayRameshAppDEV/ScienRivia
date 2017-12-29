package com.example.android.scienrivia;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    int totalScore = 0;
    TextView questionTextView, firstOption, secondOption, thirdOption, fourthOption;
    Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        random = new Random();
        int randomNumberForURL = random.nextInt(24);

        questionTextView = (TextView) findViewById(R.id.questiontextView);
        firstOption = (TextView) findViewById(R.id.textView3);
        secondOption = (TextView) findViewById(R.id.textView7);
        thirdOption = (TextView) findViewById(R.id.textView6);
        fourthOption = (TextView) findViewById(R.id.textView8);




        Intent getUpdateRangeIntent = getIntent();
        int updateCountFromHome = getUpdateRangeIntent.getIntExtra("updateCounter", 0);
//
        //Show and hide navigation bar (Immersive mode)
        View showAndHideBars = findViewById(R.id.quizlayout);
        showAndHideBars.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
        DownloadTask task = new DownloadTask();
//        task.execute("https://opentdb.com/api.php?amount=" + updateCountFromHome + "&category=17&difficulty=easy&type=multiple");
        task.execute("https://qriusity.com/v1/categories/20/questions?page=" + randomNumberForURL + "&limit=" + updateCountFromHome);


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
        protected void onPostExecute(String resultURL) {
            super.onPostExecute(resultURL);
            try {

                JSONArray jsonArray = new JSONArray(resultURL);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    String question = jsonObject.getString("question");
                    String option1 = jsonObject.getString("option1");
                    String option2 = jsonObject.getString("option2");
                    String option3 = jsonObject.getString("option3");
                    String option4 = jsonObject.getString("option4");
                    String answer = jsonObject.getString("answers");
                    int ans = Integer.parseInt(answer);
                    String ac = "";
                    if (ans == 1) {
                        ac = "A";
                    } else if (ans == 2) {
                        ac = "B";
                    } else if (ans == 3) {
                        ac = "C";
                    } else if (ans == 4) {
                        ac = "D";
                    }
                    String s = question + "\n" + "A." + option1 + "\n" + "B." + option2 + "\n" + "C." + option3 + "\n" + "D." + option4 + "\n" + "Answer is:" + ac;
                    Log.i("po", s);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
