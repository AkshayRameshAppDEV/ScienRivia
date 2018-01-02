package com.example.android.scienrivia;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import java.util.ArrayList;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {




    String question, option1, option2, option3, option4, answer;

    ArrayList<String> questionsList, option1List, option2List, option3List, option4List, answerList;


    int updateCountFromHome = 0;

    int nextQuestionCount = 1;



    TextView questionTextView, firstOption, secondOption, thirdOption, fourthOption;
    Random random;


    public void chooseAnswer(View view) {
//        int correctAn = 4;
//        int tagger = Integer.parseInt(view.getTag().toString());
//        Log.i("answer option touch","The Tag is: "+tagger);
    }

//    public void nextQuestion(View view) {
//        if (c == null) {
//            c = quizDB.rawQuery("SELECT * FROM quiz", null);
//            c.moveToFirst();
//        }
//        questionTextView.setText("");
//        firstOption.setText("");
//        secondOption.setText("");
//        thirdOption.setText("");
//        fourthOption.setText("");
//
//        if (c != null && c.getCount() > 0 && !c.isAfterLast()) {
//
//
////            questionW = c.getColumnIndex("questionDB");
////                    option1W = c.getColumnIndex("option1DB");
////                    option2W = c.getColumnIndex("option2DB");
////                    option3W = c.getColumnIndex("option3DB");
////                    option4W = c.getColumnIndex("option4DB");
//
//            String question = c.getString(c.getColumnIndex("questionDB"));
//            String option1 = c.getString(c.getColumnIndex("option1DB"));
//            String option2 = c.getString(c.getColumnIndex("option2DB"));
//            String option3 = c.getString(c.getColumnIndex("option3DB"));
//            String option4 = c.getString(c.getColumnIndex("option4DB"));
//
//            Log.i("Result", question + "\n" + option1 + "\n" + option2 + "\n" + option3 + "\n" + option4);
//            Log.i("Result", "-----------------------------------------------------------------------------------------");
//
//            questionTextView.setText(question);
//            firstOption.setText(option1);
//            secondOption.setText(option2);
//            thirdOption.setText(option3);
//            fourthOption.setText(option4);
//            c.moveToNext();
//        }
//
//
//    }

    public void nextQuestion(View view) {
        if (nextQuestionCount == questionsList.size()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("Your Score is: 20/20 ");
            builder.setMessage("Are you sure ?");
            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if user pressed "yes", then he is allowed to exit from application
//                deleteDatabase("QUIZ");
                    questionsList.clear();
                    option1List.clear();
                    option2List.clear();
                    option3List.clear();
                    option4List.clear();
                    answerList.clear();
                    nextQuestionCount = 1;
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

//                deleteDatabase("QUIZ");
                    questionsList.clear();
                    option1List.clear();
                    option2List.clear();
                    option3List.clear();
                    option4List.clear();
                    answerList.clear();
                    nextQuestionCount = 1;

                    onRestart();

                }
            });
            AlertDialog alert = builder.create();
            alert.show();
            nextQuestionCount = 1;


        } else {
            questionTextView.setText(questionsList.get(nextQuestionCount).toString());
            firstOption.setText("A. " + option1List.get(nextQuestionCount).toString());
            secondOption.setText("B. " + option2List.get(nextQuestionCount).toString());
            thirdOption.setText("C. " + option3List.get(nextQuestionCount).toString());
            fourthOption.setText("D. " + option4List.get(nextQuestionCount).toString());
            nextQuestionCount++;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionsList = new ArrayList<>();
        option1List = new ArrayList<>();
        option2List = new ArrayList<>();
        option3List = new ArrayList<>();
        option4List = new ArrayList<>();
        answerList = new ArrayList<>();

//        deleteDatabase("QUIZ");
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
//                deleteDatabase("QUIZ");
                questionsList.clear();
                option1List.clear();
                option2List.clear();
                option3List.clear();
                option4List.clear();
                answerList.clear();
                nextQuestionCount = 1;
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

//                deleteDatabase("QUIZ");
                questionsList.clear();
                option1List.clear();
                option2List.clear();
                option3List.clear();
                option4List.clear();
                answerList.clear();
                nextQuestionCount = 1;

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

                    questionsList.add(question);
                    option1List.add(option1);
                    option2List.add(option2);
                    option3List.add(option3);
                    option4List.add(option4);
                    answerList.add(answer);
                }

                questionTextView.setText(questionsList.get(0).toString());
                firstOption.setText("A. " + option1List.get(0).toString());
                secondOption.setText("B. " + option2List.get(0).toString());
                thirdOption.setText("C. " + option3List.get(0).toString());
                fourthOption.setText("D. " + option4List.get(0).toString());


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
