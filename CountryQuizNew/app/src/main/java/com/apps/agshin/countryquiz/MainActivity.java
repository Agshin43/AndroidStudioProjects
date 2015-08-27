package com.apps.agshin.countryquiz;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    private void startQuiz(int button){
        Intent quizIntent = new Intent(getApplicationContext(), TestQuizActivity.class);
//        quizIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Bundle bundle = new Bundle();
        bundle.putInt("source", button);

        quizIntent.putExtras(bundle);

        startActivity(quizIntent);
    }
    public void imageButtonClicked(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_flag:
            {
//                getApplicationContext().startActivity(new Intent(getApplicationContext(), TestQuizActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                startQuiz(Question.Source.flags.ordinal());

                break;
            }
            case R.id.btn_coat_of_arms:
            {
                startQuiz(Question.Source.coatOfArms.ordinal());
                break;
            }
            case R.id.btn_capital:
            {
                break;
            }
            case R.id.btn_area:
            {
                break;
            }
            case R.id.btn_currency:
            {
                break;
            }
            case R.id.btn_domain:
            {
                break;
            }
        }
    }
}
