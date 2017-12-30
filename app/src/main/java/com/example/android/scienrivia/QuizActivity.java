package com.example.android.scienrivia;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    int valueFromNextButton = 0;

    SQLiteDatabase quizDB;
    Cursor c;
    int noOfScorer = 0;

    String question, option1, option2, option3, option4, answer;

    int updateCountFromHome = 0;

    int questionW;
    int option1W;
    int option2W;
    int option3W;
    int option4W;
    int correctAnswerW;

    TextView questionTextView, firstOption, secondOption, thirdOption, fourthOption;
    Random random;


    public void nextQuestion(View view) {
        deleteDatabase("QUIZ");
    }

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
        updateCountFromHome = getUpdateRangeIntent.getIntExtra("updateCounter", 0);
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
        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                deleteDatabase("QUIZ");
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                int pid = android.os.Process.myPid();
                android.os.Process.killProcess(pid);
                //Show and hide navigation bar (Immersive mode)
                View showAndHideBars = findViewById(R.id.quizlayout);
                showAndHideBars.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);


            }
        });
        builder.setNegativeButton("Restart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app

                deleteDatabase("QUIZ");
                onRestart();

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


                quizDB = getApplicationContext().openOrCreateDatabase("QUIZ", MODE_PRIVATE, null);
                quizDB.execSQL("CREATE TABLE IF NOT EXISTS quiz (questionDB VARCHAR, option1DB VARCHAR, option2DB VARCHAR, option3DB VARCHAR, option4DB VARCHAR, correctAnswerDB VARCHAR)");

                JSONArray jsonArray = new JSONArray(resultURL);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    question = jsonObject.getString("question");
                    option1 = jsonObject.getString("option1");
                    option2 = jsonObject.getString("option2");
                    option3 = jsonObject.getString("option3");
                    option4 = jsonObject.getString("option4");
                    answer = jsonObject.getString("answers");

                    if (question.contains("'")) {
                        question = question.replace("'", "''");
                    }
                    if (option1.contains("'")) {
                        option1 = option1.replace("'", "''");
                    }
                    if (option2.contains("'")) {
                        option2 = option2.replace("'", "''");

                    }
                    if (option3.contains("'")) {
                        option3 = option3.replace("'", "''");


                    }
                    if (option4.contains("'")) {
                        option4 = option4.replace("'", "''");

                    }

                    quizDB.execSQL("INSERT INTO " + "quiz (questionDB, option1DB,option2DB,option3DB, option4DB, correctAnswerDB)" + " VALUES ('" + question + "'," + "'" + option1 + "'," + "'" + option2 + "'," + "'" + option3 + "'," + "'" + option4 + "'," + "'" + answer + "'" + ");");

                    c = quizDB.rawQuery("SELECT * FROM quiz", null);

                    questionW = c.getColumnIndex("questionDB");
                    option1W = c.getColumnIndex("option1DB");
                    option2W = c.getColumnIndex("option2DB");
                    option3W = c.getColumnIndex("option3DB");
                    option4W = c.getColumnIndex("option4DB");
                    correctAnswerW = c.getColumnIndex("correctAnswerDB");


                    c.moveToFirst();




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
//                    String s = question + "\n" + "A." + option1 + "\n" + "B." + option2 + "\n" + "C." + option3 + "\n" + "D." + option4 + "\n" + "Answer is:" + ac;
//                    Log.i("po", s);
                }
                while ((!c.isAfterLast()) && noOfScorer < updateCountFromHome) {

                    Log.i("QUIZ - Question", c.getString(questionW));
                    Log.i("QUIZ - option1", c.getString(option1W));
                    Log.i("QUIZ - option2", c.getString(option2W));
                    Log.i("QUIZ - option3", c.getString(option3W));
                    Log.i("QUIZ - option4", c.getString(option4W));
                    Log.i("QUIZ - correctAnswer", c.getString(correctAnswerW));

                    c.moveToNext();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
