package com.apps.agshin.countryquiz;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class TestQuizActivity extends ActionBarActivity {

    QuizManager quizManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_quiz);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        quizManager = new QuizManager(getApplicationContext());
        quizManager.getQuestionsCountries(QuizManager.Achievement.achievement_well_done, Question.Type.textQuestion, Question.Source.capitals, 10);
        getMenuInflater().inflate(R.menu.menu_test_quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void answerSelected(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_answer1:
            {
            }
            case R.id.btn_answer2:
            {

            }
            case R.id.btn_answer3:
            {

            }
            case R.id.btn_answer4:
            {

            }
        }
    }
}
