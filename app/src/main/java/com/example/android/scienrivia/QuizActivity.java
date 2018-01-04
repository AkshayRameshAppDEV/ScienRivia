package com.example.android.scienrivia;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
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

    int nextItemAnswerListCounter = 0;

    int arrayListgetChooseAnswer = 0;


    int score = 0;


    TextView questionTextView, firstOption, secondOption, thirdOption, fourthOption;
    Random random;


    public void nextQuestion(View view) {
        if (nextQuestionCount == questionsList.size()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("Your Score is: " + score + "/" + updateCountFromHome);
            builder.setMessage("Do You Want To Play Again ?");
            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    questionsList.clear();
                    option1List.clear();
                    option2List.clear();
                    option3List.clear();
                    option4List.clear();
                    answerList.clear();
                    nextQuestionCount = 1;
                    nextItemAnswerListCounter = 0;
                    score = 0;
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

                    questionsList.clear();
                    option1List.clear();
                    option2List.clear();
                    option3List.clear();
                    option4List.clear();
                    answerList.clear();
                    nextItemAnswerListCounter = 0;
                    nextQuestionCount = 1;
                    score = 0;

                    onRestart();

                }
            });
            AlertDialog alert = builder.create();
            alert.show();
            nextQuestionCount = 1;


        } else {
            firstOption.setTextColor(Color.parseColor("#ffff00"));
            firstOption.setEnabled(true);
            firstOption.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_rounded_bg));
            firstOption.setTypeface(Typeface.DEFAULT_BOLD);
            firstOption.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

            secondOption.setTextColor(Color.parseColor("#ffff00"));
            secondOption.setEnabled(true);
            secondOption.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_rounded_bg));
            secondOption.setTypeface(Typeface.DEFAULT_BOLD);
            secondOption.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

            thirdOption.setTextColor(Color.parseColor("#ffff00"));
            thirdOption.setEnabled(true);
            thirdOption.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_rounded_bg));
            thirdOption.setTypeface(Typeface.DEFAULT_BOLD);
            thirdOption.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

            fourthOption.setTextColor(Color.parseColor("#ffff00"));
            fourthOption.setEnabled(true);
            fourthOption.setBackgroundDrawable(getResources().getDrawable(R.drawable.my_rounded_bg));
            fourthOption.setTypeface(Typeface.DEFAULT_BOLD);
            fourthOption.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

            questionTextView.setText(questionsList.get(nextQuestionCount).toString());
            firstOption.setText("A. " + option1List.get(nextQuestionCount).toString());
            secondOption.setText("B. " + option2List.get(nextQuestionCount).toString());
            thirdOption.setText("C. " + option3List.get(nextQuestionCount).toString());
            fourthOption.setText("D. " + option4List.get(nextQuestionCount).toString());
            nextQuestionCount++;
            arrayListgetChooseAnswer++;
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
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
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
                nextItemAnswerListCounter = 0;
                nextQuestionCount = 1;
                score = 0;

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

                for (int i = 0; i < answerList.size(); i++) {
                    Log.i("Answers", "Answer number " + i + " " + answerList.get(i));
                }


                firstOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if (Integer.parseInt(firstOption.getTag().toString()) == Integer.parseInt(answerList.get(nextItemAnswerListCounter))) {
                            score++;
                            firstOption.setTextColor(Color.GREEN);
                        } else {
                            firstOption.setTextColor(Color.RED);
                        }


                        firstOption.setEnabled(false);
                        secondOption.setTextColor(Color.GRAY);
                        secondOption.setEnabled(false);
                        thirdOption.setTextColor(Color.GRAY);
                        thirdOption.setEnabled(false);
                        fourthOption.setTextColor(Color.GRAY);
                        fourthOption.setEnabled(false);
                        nextItemAnswerListCounter++;


                    }
                });

                secondOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (Integer.parseInt(secondOption.getTag().toString()) == Integer.parseInt(answerList.get(nextItemAnswerListCounter))) {
                            score++;
                            secondOption.setTextColor(Color.GREEN);
                        } else {
                            secondOption.setTextColor(Color.RED);
                        }

                        secondOption.setEnabled(false);
                        firstOption.setTextColor(Color.GRAY);
                        firstOption.setEnabled(false);
                        thirdOption.setTextColor(Color.GRAY);
                        thirdOption.setEnabled(false);
                        fourthOption.setTextColor(Color.GRAY);
                        fourthOption.setEnabled(false);
                        nextItemAnswerListCounter++;

                    }
                });

                thirdOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if (Integer.parseInt(thirdOption.getTag().toString()) == Integer.parseInt(answerList.get(nextItemAnswerListCounter))) {
                            score++;
                            thirdOption.setTextColor(Color.GREEN);
                        } else {
                            thirdOption.setTextColor(Color.RED);
                        }

                        thirdOption.setEnabled(false);
                        firstOption.setTextColor(Color.GRAY);
                        firstOption.setEnabled(false);
                        secondOption.setTextColor(Color.GRAY);
                        secondOption.setEnabled(false);
                        fourthOption.setTextColor(Color.GRAY);
                        fourthOption.setEnabled(false);
                        nextItemAnswerListCounter++;

                    }
                });

                fourthOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if (Integer.parseInt(fourthOption.getTag().toString()) == Integer.parseInt(answerList.get(nextItemAnswerListCounter))) {
                            score++;
                            fourthOption.setTextColor(Color.GREEN);
                        } else {
                            fourthOption.setTextColor(Color.RED);
                        }


                        fourthOption.setEnabled(false);
                        firstOption.setTextColor(Color.GRAY);
                        firstOption.setEnabled(false);
                        thirdOption.setTextColor(Color.GRAY);
                        thirdOption.setEnabled(false);
                        secondOption.setTextColor(Color.GRAY);
                        secondOption.setEnabled(false);
                        nextItemAnswerListCounter++;

                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
