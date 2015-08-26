package com.apps.agshin.countryquiz;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class TestQuizActivity extends ActionBarActivity  {

    private String mPackageName;


    QuizManager quizManager;
    QuizManager.Achievement achievement;
    Question.Source source;
    Question.Type type;
    LinearLayout layImageAnswers;
    LinearLayout layTextAnswers;
    TextView tvQuestionText;
    ImageView ivQuestionImage;

    ImageButton btnAnswer1;
    ImageButton btnAnswer2;
    ImageButton btnAnswer3;
    ImageButton btnAnswer4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_quiz);

        mPackageName = getPackageName();
        quizManager = new QuizManager(getApplicationContext());

        btnAnswer1 = (ImageButton) findViewById(R.id.btn_answer1);
        btnAnswer2 = (ImageButton) findViewById(R.id.btn_answer2);
        btnAnswer3 = (ImageButton) findViewById(R.id.btn_answer3);
        btnAnswer4 = (ImageButton) findViewById(R.id.btn_answer4);

        layImageAnswers = (LinearLayout) findViewById(R.id.lay_image_answers);
        layTextAnswers = (LinearLayout) findViewById(R.id.lay_text_answers);
        tvQuestionText = (TextView) findViewById(R.id.tv_question_text);
        ivQuestionImage = (ImageView) findViewById(R.id.iv_question_image);


        achievement = QuizManager.Achievement.achievement_cool;
        source = Question.Source.flags;
        type = Question.Type.textToImages;
        generateQuestions(10);

        showQuestion(quizManager.nextQuestion());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

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
    public void nextQuestion(View v){
        showQuestion(quizManager.nextQuestion());
    }

    public void prevQuestion(View v){
        showQuestion(quizManager.nextQuestion());
    }

    public void answerSelected(View v)
    {

        Toast.makeText(getApplicationContext(), "Answer selected", Toast.LENGTH_SHORT).show();
        switch (v.getId())
        {
            case R.id.btn_answer1:
            {
                quizManager.answer(quizManager.currentQuestionId, 0);
                Log.i("","1");
                break;
            }
            case R.id.btn_answer2:
            {
                quizManager.answer(quizManager.currentQuestionId, 1);
            }
            case R.id.btn_answer3:
            {
                quizManager.answer(quizManager.currentQuestionId, 2);
                break;
            }
            case R.id.btn_answer4:
            {
                quizManager.answer(quizManager.currentQuestionId, 3);
                break;
            }
            default: break;
        }
    }

    private void generateQuestions(int count)
    {
        quizManager.generateQuestions(count, achievement, type, source);
    }


    public int flagResIdByDomain(String domain){
        Log.i("", "Flag "+domain+"_c");
        return getResources().getIdentifier(domain+"_c", "drawable", mPackageName); // + "_c" ***
    }
    public int coaResIdByDomain(String domain){
        return getResources().getIdentifier(domain+"_c", "drawable", mPackageName);
    }

    private void showQuestion(Question question){
        switch (question.type) {
            case imageToImages:

                tvQuestionText.setVisibility(TextView.GONE);
                ivQuestionImage.setVisibility(ImageView.VISIBLE);

                layImageAnswers.setVisibility(RelativeLayout.VISIBLE);
                layTextAnswers.setVisibility(RelativeLayout.GONE);

                break;


            case imageToTexts:

                tvQuestionText.setVisibility(TextView.GONE);
                ivQuestionImage.setVisibility(ImageView.VISIBLE);

                layImageAnswers.setVisibility(RelativeLayout.GONE);
                layTextAnswers.setVisibility(RelativeLayout.VISIBLE);

                break;


            case textToImages:

                Toast.makeText(getApplicationContext(),"Text to images",Toast.LENGTH_SHORT).show();

                tvQuestionText.setVisibility(TextView.VISIBLE);
                ivQuestionImage.setVisibility(ImageView.GONE);

                layImageAnswers.setVisibility(RelativeLayout.VISIBLE);
                layTextAnswers.setVisibility(RelativeLayout.GONE);

                ///////////////////////////////////////////

                switch (question.source){
                    case flags:

                        tvQuestionText.setText(getResources().getString(R.string.question_i2t_flags) + " "+question.questionC.name);


                        btnAnswer1.setImageResource(flagResIdByDomain(question.answersC.get(0).domain));
                        btnAnswer2.setImageResource(flagResIdByDomain(question.answersC.get(1).domain));
                        btnAnswer3.setImageResource(flagResIdByDomain(question.answersC.get(2).domain));
                        btnAnswer4.setImageResource(flagResIdByDomain(question.answersC.get(3).domain));

                        break;


                    case areas:
                        boolean lg = true;

                        for( int i = 0; i < question.answersC.size(); i++ )
                        {
                            if( question.questionC.area < question.answersC.get(i).area )
                            {
                                lg = false;
                                break;
                            }
                        }

                        if(lg)
                        {
                            tvQuestionText.setText(getResources().getString(R.string.question_area_largest));
                        }
                        else
                        {
                            tvQuestionText.setText(getResources().getString(R.string.question_area_smallest));
                        }

                        btnAnswer1.setImageResource(flagResIdByDomain(question.answersC.get(0).domain));
                        btnAnswer2.setImageResource(flagResIdByDomain(question.answersC.get(1).domain));
                        btnAnswer3.setImageResource(flagResIdByDomain(question.answersC.get(2).domain));
                        btnAnswer4.setImageResource(flagResIdByDomain(question.answersC.get(3).domain));

                        break;
                }

                break;

            case textToTexts:

                tvQuestionText.setVisibility(TextView.VISIBLE);
                ivQuestionImage.setVisibility(ImageView.GONE);

                layImageAnswers.setVisibility(RelativeLayout.GONE);
                layTextAnswers.setVisibility(RelativeLayout.VISIBLE);

                break;


            default: break;
        }
    }

    private void initQuestionView()
    {
        layImageAnswers = (LinearLayout) findViewById(R.id.lay_image_answers);
        layTextAnswers = (LinearLayout) findViewById(R.id.lay_text_answers);
        tvQuestionText = (TextView) findViewById(R.id.tv_question_text);
        ivQuestionImage = (ImageView) findViewById(R.id.iv_question_image);
    }

    ///////////////// Tools ////////////////

    private Drawable getDrawable(String name)
    {
        Resources resources = getApplicationContext().getResources();
        final int resourceId = resources.getIdentifier(name, "drawable",
                getApplicationContext().getPackageName());
        return resources.getDrawable(resourceId);
    }

    private void setBackgroundImageView(ImageView view, String imageName)
    {
        Resources resources = getApplicationContext().getResources();
        final int resourceId = resources.getIdentifier(imageName, "drawable",
                getApplicationContext().getPackageName());
        view.setImageResource(resourceId);
    }

    private void setBackgroundImageButton(ImageButton view, String imageName)
    {
        Resources resources = getApplicationContext().getResources();
        final int resourceId = resources.getIdentifier(imageName, "drawable",
                getApplicationContext().getPackageName());
        view.setImageResource(resourceId);
    }

    private void setAnswerImages(Question question, String imgSource)
    {
        setBackgroundImageButton(btnAnswer1, question.answersC.get(0).domain + imgSource);
        setBackgroundImageButton(btnAnswer2, question.answersC.get(1).domain + imgSource);
        setBackgroundImageButton(btnAnswer3, question.answersC.get(2).domain + imgSource);
        setBackgroundImageButton(btnAnswer4, question.answersC.get(3).domain + imgSource);
    }

    /////////////////////////////////////////
}
