package com.apps.agshin.countryquiz;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.achievement.Achievement;
import com.google.android.gms.games.achievement.AchievementBuffer;
import com.google.android.gms.games.achievement.Achievements;
import com.google.android.gms.plus.Plus;
import com.google.example.games.basegameutils.BaseGameUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

public class PlayQuizActivity extends Activity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    final static int REQUEST_LEADERBOARD = 1254;
    final static int REQUEST_ACHIEVEMENTS = 1255;

    private static final String TAG = "Quiz";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;

    private boolean mResolvingConnectionFailure = false;

    private boolean mSignInClicked = false;

    private boolean mAutoStartSignInFlow = true;

    private MediaPlayer mpSoundEffectCorrect;
    private MediaPlayer mpSoundEffectWrong;

    AudioManager mAoudioManager;

    private boolean soundEffectsEnabled = false;

    private boolean doubleBackToExitPressedOnce = false;



    //////// Quiz ////////

    final static int PERMISSION_REQUEST_ACCOUNTS = 9998;

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

    Button btnAnswerText1;
    Button btnAnswerText2;
    Button btnAnswerText3;
    Button btnAnswerText4;

    Chronometer cmTime;
    ProgressBar pbDamage;
    ProgressBar pbProgress;

    TextView tvCorrectAnswers;
    TextView tvXP;
    TextView tvPoint;


    TextView tvQuestionLevel;


    private int XP = 1;

    private long point = 0;

    private long lastAnswerTS;

    private int maxUnlockAchieveId;

    //////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTheme(R.style.game_theme2);

        super.onCreate(savedInstanceState);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();

        if(isUserLogged()){
            mGoogleApiClient.connect();
        }
        setContentView(R.layout.activity_test_quiz);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.btn_start).setOnClickListener(this);
        findViewById(R.id.btn_achievements).setOnClickListener(this);
        findViewById(R.id.btn_leaderboards).setOnClickListener(this);

        showSignInBar();

        ///////////////
        AdView mAdView = (AdView) findViewById(R.id.ad);
        com.google.android.gms.ads.AdRequest adRequest = new com.google.android.gms.ads.AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        ///////////////

        ///////// Quiz /////////

        mPackageName = getPackageName();
        quizManager = new QuizManager(getApplicationContext());

        btnAnswer1 = (ImageButton) findViewById(R.id.btn_answer1);
        btnAnswer2 = (ImageButton) findViewById(R.id.btn_answer2);
        btnAnswer3 = (ImageButton) findViewById(R.id.btn_answer3);
        btnAnswer4 = (ImageButton) findViewById(R.id.btn_answer4);

        btnAnswerText1 = (Button) findViewById(R.id.btn_text_answer1);
        btnAnswerText2 = (Button) findViewById(R.id.btn_text_answer2);
        btnAnswerText3 = (Button) findViewById(R.id.btn_text_answer3);
        btnAnswerText4 = (Button) findViewById(R.id.btn_text_answer4);

        layImageAnswers = (LinearLayout) findViewById(R.id.lay_image_answers);
        layTextAnswers = (LinearLayout) findViewById(R.id.lay_text_answers);
        tvQuestionText = (TextView) findViewById(R.id.tv_question_text);
        ivQuestionImage = (ImageView) findViewById(R.id.iv_question_image);

        pbDamage = (ProgressBar) findViewById(R.id.pbDamage);
        pbProgress = (ProgressBar) findViewById(R.id.pbProgress);

        tvCorrectAnswers = (TextView) findViewById(R.id.tvAnswers);
        tvXP = (TextView) findViewById(R.id.tvXp);
        tvPoint = (TextView) findViewById(R.id.tvPoints);
        tvQuestionLevel = (TextView) findViewById(R.id.tvLevel);

        CheckBox cbSoundEffects = (CheckBox) findViewById(R.id.cbSoundEffects);
        setSoundEffectsEnabled();
        cbSoundEffects.setChecked(soundEffectsEnabled);
        cbSoundEffects.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                soundEffectsEnabled = isChecked;
                commitSoundEffects(isChecked);
            }
        });


        cmTime = (Chronometer) findViewById(R.id.cmTime);
        cmTime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer cArg) {
                long time = SystemClock.elapsedRealtime() - cArg.getBase();
                int h = (int) (time / 3600000);
                int m = (int) (time - h * 3600000) / 60000;
                int s = (int) (time - h * 3600000 - m * 60000) / 1000;
                String mm = m < 10 ? "0" + m : m + "";
                String ss = s < 10 ? "0" + s : s + "";
                cArg.setText(mm + ":" + ss);
            }
        });

        mpSoundEffectCorrect = MediaPlayer.create(getApplicationContext(), R.raw.au_correct);
        mpSoundEffectWrong = MediaPlayer.create(getApplicationContext(), R.raw.au_wrong);

        mAoudioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        ////////////////////////
    }

    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    public void onBackPressed()
    {
        if(findViewById(R.id.layQuiz).getVisibility() != View.VISIBLE){
            super.onBackPressed();
            return;
        }
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            switchMenuAndGame(false);
            return;
        }
        else {
            Toast.makeText(getBaseContext(), R.string.tap_again_to_end_game, Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }

    private void showSignInBar() {
        findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        findViewById(R.id.sign_out_button).setVisibility(View.GONE);

        findViewById(R.id.layLeaderboards).setVisibility(View.GONE);
        ImageView vw = (ImageView) findViewById(R.id.avatar);
        vw.setImageBitmap(null);
        TextView name = (TextView)findViewById(R.id.playerName);
        name.setText("");
        TextView email = (TextView)findViewById((R.id.playerEmail));
        email.setText("");

    }


    private void showSignOutBar() {
        findViewById(R.id.sign_in_button).setVisibility(View.GONE);
        findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);

        findViewById(R.id.layLeaderboards).setVisibility(View.VISIBLE);

        Player player = Games.Players.getCurrentPlayer(mGoogleApiClient);
        String url = player.getIconImageUrl();
        TextView name = (TextView)findViewById(R.id.playerName);
        name.setText(player.getDisplayName());
        if (url != null) {
            ImageView vw = (ImageView) findViewById(R.id.avatar);
            new DownloadImageTask(vw).execute(url);
         }

        String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
        TextView emailView = (TextView)findViewById((R.id.playerEmail));
        emailView.setText(email);
    }
    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap mIcon11 = null;
            String url = strings[0];
            try {
                InputStream in = new URL(url).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
            return mIcon11;
        }
        protected void onPostExecute(Bitmap result) {
            if(result == null){
                return;
            }
            MCircleDrawable mCircleDrawable = new MCircleDrawable(result, R.color.myWhite);
            bmImage.setImageDrawable(mCircleDrawable);
            bmImage.setVisibility(View.VISIBLE);
        }
    }

    private void switchMenuAndGame(boolean game){
        if(game){
            findViewById(R.id.layStart).setVisibility(View.GONE);
            findViewById(R.id.layQuiz).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.layStart).setVisibility(View.VISIBLE);
            findViewById(R.id.layQuiz).setVisibility(View.GONE);            
        }
    }
    
    private boolean isUserLogged(){
        SharedPreferences sp = this.getSharedPreferences("cquiz_", MODE_PRIVATE);
        boolean ret = sp.getBoolean("am i logged in", false);
        return ret;
    }

    private void logged(boolean in){
        SharedPreferences sp = this.getSharedPreferences("cquiz_", MODE_PRIVATE);

        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("am i logged in", in);
        ed.commit();
    }

    private void setSoundEffectsEnabled(){
        SharedPreferences sp = this.getSharedPreferences("cquiz_", MODE_PRIVATE);
        soundEffectsEnabled = sp.getBoolean("sound effects", false);
    }

    private void commitSoundEffects(boolean soundEffectsEnabled){
        SharedPreferences sp = this.getSharedPreferences("cquiz_", MODE_PRIVATE);

        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("sound effects", soundEffectsEnabled);
        ed.commit();
    }


    private String achieveIdByNumber(int id){

        switch (id){
            case 0:{
                return getResources().getString(R.string.achievement_beginner);
            }
            case 1:{
                return getResources().getString(R.string.achievement_talented);
            }
            case 2:{
                return getResources().getString(R.string.achievement_skilled);
            }
            case 3:{
                return getResources().getString(R.string.achievement_intermediate);
            }
            case 4:{
                return getResources().getString(R.string.achievement_professional);
            }
            case 5:{
                return getResources().getString(R.string.achievement_seasoned);
            }
            case 6:{
                return getResources().getString(R.string.achievement_proficient);
            }
            case 7:{
                return getResources().getString(R.string.achievement_experienced);
            }
            default:break;
        }

        return "-";
    }

    private int getMaxUnlockAchieveId(){
        Games.Achievements.load(mGoogleApiClient, true).setResultCallback(new MAchievement());
        return maxUnlockAchieveId;
    }

    private void processAchievements(int point){
        Games.Achievements.load(mGoogleApiClient, true).setResultCallback(new AchieveProcessor(point));
    }

    class AchieveProcessor implements ResultCallback<Achievements.LoadAchievementsResult> {

        private int aPoint;
        public AchieveProcessor(int point) {
            this.aPoint = point;
        }

        @Override
        public void onResult(Achievements.LoadAchievementsResult arg0) {
            Achievement ach;
            AchievementBuffer aBuffer = arg0.getAchievements();
            Iterator<Achievement> aIterator = aBuffer.iterator();

            maxUnlockAchieveId = -1;

            while (aIterator.hasNext()) {
                ach = aIterator.next();
                if (ach.getState() == Achievement.STATE_UNLOCKED) {
                    maxUnlockAchieveId++;
                    //Log.i("AchieveProcessor","LOCK "+ach.getName());

                } else {
                    //Log.i("AchieveProcessor","UNLOCKED "+ach.getName());
                    break;
                }
            }

            int mAchId = maxUnlockAchieveId+1;
            if(mAchId > 2){
                //Log.i("INCREMENT","ACHIEVE "+mAchId);
                Games.Achievements.increment(mGoogleApiClient,achieveIdByNumber(mAchId),aPoint);
            } else {
                switch (mAchId){
                    case 0:{
                        if(point >= 20){
                            //Log.i("UNLOCK","ACHIEVE "+mAchId);
                            Games.Achievements.unlock(mGoogleApiClient,achieveIdByNumber(mAchId));
                        }
                        break;
                    }
                    case 1:{
                        if(point >= 200){
                            //Log.i("UNLOCK","ACHIEVE "+mAchId);
                            Games.Achievements.unlock(mGoogleApiClient,achieveIdByNumber(mAchId));
                        }
                        break;
                    }
                    case 2:{
                        if(point >= 500){
                            //Log.i("UNLOCK","ACHIEVE "+mAchId);
                            Games.Achievements.unlock(mGoogleApiClient,achieveIdByNumber(mAchId));
                        }
                        break;
                    }
                }

            }
        }
    }


    class MAchievement implements ResultCallback<Achievements.LoadAchievementsResult> {

        @Override
        public void onResult(Achievements.LoadAchievementsResult arg0) {
            Achievement ach;
            AchievementBuffer aBuffer = arg0.getAchievements();
            Iterator<Achievement> aIterator = aBuffer.iterator();

            maxUnlockAchieveId = -1;

            while (aIterator.hasNext()) {
                ach = aIterator.next();
                if (ach.getState() == Achievement.STATE_UNLOCKED) {
                    maxUnlockAchieveId++;
                    //Log.i("","LOCK "+ach.getName());

                } else {
                    //Log.i("","UNLOCKED "+ach.getName());
                    //it is not unlocked
                }
            }
        }
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d(TAG, "onConnected() called. Sign in successful!");
        showSignOutBar();
        getMaxUnlockAchieveId();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended() called. Trying to reconnect.");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed() called, result: " + connectionResult);

        if (mResolvingConnectionFailure) {
            Log.d(TAG, "onConnectionFailed() ignoring connection failure; already resolving.");
            return;
        }

        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = BaseGameUtils
                    .resolveConnectionFailure(this, mGoogleApiClient,
                            connectionResult, RC_SIGN_IN, getString(R.string.signin_other_error));
        }
        showSignInBar();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(resultCode == 10001){
            showSignInBar();
        }

        switch (requestCode) {
            case RC_SIGN_IN: {
                Log.d(TAG, "onActivityResult with requestCode == RC_SIGN_IN, responseCode="
                        + resultCode + ", intent=" + data);
                mSignInClicked = false;
                mResolvingConnectionFailure = false;
                if (resultCode == RESULT_OK) {
                    mGoogleApiClient.connect();
                } else {
                    BaseGameUtils.showActivityResultError(this, requestCode, resultCode, R.string.signin_other_error);
                }
                break;
            }

            default:break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:

                if(requestAccountsPermission(this, cmTime)){
                    logged(true);
                    if (!BaseGameUtils.verifySampleSetup(this, R.string.app_id)) {
                        Log.w(TAG, "*** Warning: setup problems detected. Sign in may not work!");
                    }
                    mSignInClicked = true;
                    mGoogleApiClient.connect();
                }


                break;
            case R.id.sign_out_button:
                logged(false);
                Log.d(TAG, "Sign-out button clicked");
                mSignInClicked = false;
                Games.signOut(mGoogleApiClient);
                mGoogleApiClient.disconnect();
                showSignInBar();
                break;
            case R.id.btn_start:
            {
                switchMenuAndGame(true);
                XP = 1;
                point = 0;

                tvCorrectAnswers.setText("0 / 0 q");
                tvXP.setText("1 XP");
                tvPoint.setText(generatePointString(0));
                pbDamage.setProgress(100);
                pbProgress.setProgress(0);
                quizManager.reset();
                quizManager.generateQuizQuestions(3);
                showQuestion(quizManager.questions.get(0));
                cmTime.setBase(SystemClock.elapsedRealtime());
                cmTime.start();

                lastAnswerTS = System.currentTimeMillis();

                break;
            }

            case R.id.btn_achievements:{
                startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient),
                        REQUEST_ACHIEVEMENTS);
                break;
            }
            case R.id.btn_leaderboards:{
                showLeaderboards();
                break;
            }
        }
    }

    private void showLeaderboards(){

        new AlertDialog.Builder(PlayQuizActivity.this)
                .setTitle(R.string.leaderboards)
                .setMessage(R.string.choose_leaderboard)
                .setPositiveButton(R.string.leaderboard_striker_name, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
                                getResources().getString(R.string.leaderboard_striker)), REQUEST_LEADERBOARD);
                    }
                })
                .setNegativeButton(R.string.leaderboard_timeattack_name, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
                                getResources().getString(R.string.leaderboard_timeattack)), REQUEST_LEADERBOARD);
                    }
                })
                .setIcon(R.drawable.ic_launcher_64)
                .show();
    }

    ///// Quiz ////////

    public void answerSelected(View v)
    {

//        displayMaxPossibleScore();
        boolean cor = false;
        int qid = quizManager.currentQuestionId;
        int level = quizManager.questions.get(qid).questionC.level;
        switch (v.getId())
        {
            case R.id.btn_answer1:
            {
                if(!quizManager.answer(quizManager.currentQuestionId, 0)){
                    displayCorrectAnswer(quizManager.questions.get(qid));
                } else {
                    cor = true;
                }
                showQuestion(quizManager.nextQuestion());
                break;
            }
            case R.id.btn_answer2:
            {
                if(!quizManager.answer(quizManager.currentQuestionId, 1)){
                    displayCorrectAnswer(quizManager.questions.get(qid));
                } else {
                    cor = true;
                }
                showQuestion(quizManager.nextQuestion());
                break;
            }
            case R.id.btn_answer3:
            {
                if(!quizManager.answer(quizManager.currentQuestionId, 2)){
                    displayCorrectAnswer(quizManager.questions.get(qid));
                } else {
                    cor = true;
                }
                showQuestion(quizManager.nextQuestion());
                break;
            }
            case R.id.btn_answer4:
            {
                if(!quizManager.answer(quizManager.currentQuestionId, 3)){
                    displayCorrectAnswer(quizManager.questions.get(qid));
                } else {
                    cor = true;
                }
                showQuestion(quizManager.nextQuestion());
                break;
            }

            case R.id.btn_text_answer1:
            {
                if(!quizManager.answer(quizManager.currentQuestionId, 0)){
                    displayCorrectAnswer(quizManager.questions.get(qid));
                } else {
                    cor = true;
                }
                showQuestion(quizManager.nextQuestion());
                break;
            }
            case R.id.btn_text_answer2:
            {
                if(!quizManager.answer(quizManager.currentQuestionId, 1)){
                    displayCorrectAnswer(quizManager.questions.get(qid));
                } else {
                    cor = true;
                }
                showQuestion(quizManager.nextQuestion());
                break;
            }
            case R.id.btn_text_answer3:
            {
                if(!quizManager.answer(quizManager.currentQuestionId, 2)){
                    displayCorrectAnswer(quizManager.questions.get(qid));
                } else {
                    cor = true;
                }
                showQuestion(quizManager.nextQuestion());
                break;
            }
            case R.id.btn_text_answer4:
            {
                if(!quizManager.answer(quizManager.currentQuestionId, 3)){
                    displayCorrectAnswer(quizManager.questions.get(qid));
                } else {
                    cor = true;
                }
                showQuestion(quizManager.nextQuestion());
                break;
            }



            default: break;
        }



        if(soundEffectsEnabled){
            playAnswerSound(cor);
        }
        if(cor){
            long cts = System.currentTimeMillis();
            animateTvCorrect();
            if(XP > 10){
                animateXP();
//                pbDamage.setProgress(Math.min(pbDamage.getProgress() + XP, 100));
            }
            float f = 10000.f / (cts - lastAnswerTS);
            if(f > 1){
                XP += 1+(level/2);
            }

            point += ((float)XP * f);

            tvPoint.setText(generatePointString(point));


            lastAnswerTS = cts;

        } else {
            XP = 1;
            pbDamage.setProgress(pbDamage.getProgress() - 10);
            animatePbTimeAttack();
        }
        tvXP.setText(XP + " XP");

        tvCorrectAnswers.setText(quizManager.getCorrectAnswers() + " / " + quizManager.getAnsweredCount() + " q");
        int prg = (int) (((float)quizManager.getAnsweredCount() / (float) quizManager.getCurQuizQuestionCount()) * 100f);
        pbProgress.setProgress(prg);
        if(prg == 100){
            cmTime.stop();
            gameOver(pbDamage.getProgress() > 0);
        }

    }

    private void displayMaxPossibleScore(){
        int XP = 0;
        long point = 0;
        for(int i = 0; i < 24;i++){
            XP += 1 + (((i+1) % 3) / 2);
            point += XP * 20.f;
        }

        Toast.makeText(getApplicationContext(),"Max possible score is "+point+".", Toast.LENGTH_LONG).show();
        Log.i("MPS", "Max possible score is " + point + ".");
    }

    private void displayCorrectAnswer(Question question){


        Context context=getApplicationContext();
        LayoutInflater inflater=getLayoutInflater();

        View customToastroot;
        if(question.type == Question.Type.textToTexts || question.type == Question.Type.imageToTexts){
            customToastroot = inflater.inflate(R.layout.custom_correct_toast_text, null);
            TextView textView = (TextView) customToastroot.findViewById(R.id.tvMessage);
            switch (question.source){
                case capitals:{
                    textView.setText("Wrong. Correct is "+question.questionC.capital);
                    break;
                }
                case areas:{
                    textView.setText("Wrong. Correct is "+question.questionC.name);
                    break;
                }
                case flags:{
                    textView.setText("Wrong. Correct is "+question.questionC.name);
                    break;
                }
                case coatOfArms:{
                    textView.setText("Wrong. Correct is "+question.questionC.name);
                    break;
                }
            }

        } else {
            customToastroot = inflater.inflate(R.layout.custom_correct_toast_image, null);
            ImageView image = (ImageView) customToastroot.findViewById(R.id.ivImage);
            switch (question.source){
                case flags:{
                    image.setImageResource(flagResIdByDomain(question.questionC.domain));
                    break;
                }
                case coatOfArms:{
                    image.setImageResource(coaResIdByDomain(question.questionC.domain));
                    break;
                }


            }
        }

        Toast customtoast = new Toast(context);

        customtoast.setView(customToastroot);
        customtoast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        customtoast.setDuration(Toast.LENGTH_SHORT);
        customtoast.show();
    }


    private void animateTvCorrect()
    {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.tv_correct_animation);
        a.reset();
        tvCorrectAnswers.clearAnimation();
        tvCorrectAnswers.startAnimation(a);
    }

    private void animateQuestion()
    {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.tv_correct_animation);
        a.reset();
        tvQuestionText.clearAnimation();
        tvQuestionText.startAnimation(a);
    }

    private void animateXP()
    {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.bounce);
        a.reset();
        tvXP.clearAnimation();
        tvXP.startAnimation(a);
    }

    private void animatePbTimeAttack()
    {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.bounce);
        a.reset();
        pbDamage.clearAnimation();
        pbDamage.startAnimation(a);
    }


    public int flagResIdByDomain(String domain){
        if(domain.equals("do")) {
            domain = "do1";
        }
        return getResources().getIdentifier(domain, "drawable", mPackageName);
    }
    public int coaResIdByDomain(String domain){
        return getResources().getIdentifier(domain+"_ce", "drawable", mPackageName);
    }

    private void showQuestion(Question question){

        tvQuestionLevel.setText(""+question.questionC.level);

        for(int i = 0; i < question.answersC.size(); i++){
            //Log.i("PlayQuiz", question.answersC.get(i).name + " - "+question.answersC.get(i).domain);
        }
        //Log.i("PlayQuiz","Correct id "+question.correctAnswerId);
        //Log.i("PlayQuiz","====================");
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

                switch (question.source){
                    case flags:

                        ivQuestionImage.setImageResource(flagResIdByDomain(question.answersC.get(question.correctAnswerId).domain));

                        btnAnswerText1.setText(question.answersC.get(0).name);
                        btnAnswerText2.setText(question.answersC.get(1).name);
                        btnAnswerText3.setText(question.answersC.get(2).name);
                        btnAnswerText4.setText(question.answersC.get(3).name);

                        break;
                    case coatOfArms:

                        ivQuestionImage.setImageResource(coaResIdByDomain(question.answersC.get(question.correctAnswerId).domain));


                        btnAnswerText1.setText(question.answersC.get(0).name);
                        btnAnswerText2.setText(question.answersC.get(1).name);
                        btnAnswerText3.setText(question.answersC.get(2).name);
                        btnAnswerText4.setText(question.answersC.get(3).name);

                        break;

                }

                break;


            case textToImages:

                tvQuestionText.setVisibility(TextView.VISIBLE);
                ivQuestionImage.setVisibility(ImageView.GONE);

                layImageAnswers.setVisibility(RelativeLayout.VISIBLE);
                layTextAnswers.setVisibility(RelativeLayout.GONE);

                animateQuestion();

                switch (question.source){
                    case flags:

                        tvQuestionText.setText(getResources().getString(R.string.question_i2t_flags) + " "+question.questionC.name+"?");

                        btnAnswer1.setImageResource(flagResIdByDomain(question.answersC.get(0).domain));
                        btnAnswer2.setImageResource(flagResIdByDomain(question.answersC.get(1).domain));
                        btnAnswer3.setImageResource(flagResIdByDomain(question.answersC.get(2).domain));
                        btnAnswer4.setImageResource(flagResIdByDomain(question.answersC.get(3).domain));

                        break;
                    case coatOfArms:

                        tvQuestionText.setText(getResources().getString(R.string.question_i2t_coa) + " "+question.questionC.name+"?");

                        btnAnswer1.setImageResource(coaResIdByDomain(question.answersC.get(0).domain));
                        btnAnswer2.setImageResource(coaResIdByDomain(question.answersC.get(1).domain));
                        btnAnswer3.setImageResource(coaResIdByDomain(question.answersC.get(2).domain));
                        btnAnswer4.setImageResource(coaResIdByDomain(question.answersC.get(3).domain));

                        break;

                }

                break;

            case textToTexts:


                tvQuestionText.setVisibility(TextView.VISIBLE);
                ivQuestionImage.setVisibility(ImageView.GONE);

                layImageAnswers.setVisibility(RelativeLayout.GONE);
                layTextAnswers.setVisibility(RelativeLayout.VISIBLE);

                animateQuestion();

                switch (question.source){
                    case capitals:
                        tvQuestionText.setText(getResources().getString(R.string.question_capital) + " "+question.questionC.name+"?");

                        Random r = new Random();

                        String[] scaa = shuffledCapitalAnswers(question);

                        switch (question.correctAnswerId) {
                            case 0:
                                btnAnswerText1.setText(question.questionC.capital);

                                btnAnswerText2.setText(r.nextBoolean()?question.answersC.get(1).capital:scaa[0]);
                                btnAnswerText3.setText(r.nextBoolean()?question.answersC.get(2).capital:scaa[1]);
                                btnAnswerText4.setText(r.nextBoolean()?question.answersC.get(3).capital:scaa[2]);
                                break;
                            case 1:
                                btnAnswerText2.setText(question.questionC.capital);

                                btnAnswerText1.setText(r.nextBoolean()?question.answersC.get(0).capital:scaa[0]);
                                btnAnswerText3.setText(r.nextBoolean()?question.answersC.get(2).capital:scaa[1]);
                                btnAnswerText4.setText(r.nextBoolean()?question.answersC.get(3).capital:scaa[2]);
                                break;
                            case 2:
                                btnAnswerText3.setText(question.questionC.capital);

                                btnAnswerText2.setText(r.nextBoolean()?question.answersC.get(1).capital:scaa[0]);
                                btnAnswerText1.setText(r.nextBoolean()?question.answersC.get(0).capital:scaa[1]);
                                btnAnswerText4.setText(r.nextBoolean()?question.answersC.get(3).capital:scaa[2]);
                                break;
                            case 3:
                                btnAnswerText4.setText(question.questionC.capital);

                                btnAnswerText2.setText(r.nextBoolean()?question.answersC.get(1).capital:scaa[0]);
                                btnAnswerText3.setText(r.nextBoolean()?question.answersC.get(2).capital:scaa[1]);
                                btnAnswerText1.setText(r.nextBoolean()?question.answersC.get(0).capital:scaa[2]);
                                break;
                            default:break;

                        }
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

                        btnAnswerText1.setText(question.answersC.get(0).name);
                        btnAnswerText2.setText(question.answersC.get(1).name);
                        btnAnswerText3.setText(question.answersC.get(2).name);
                        btnAnswerText4.setText(question.answersC.get(3).name);

                        break;


                }

                break;


            default: break;
        }
    }


    private String[] shuffledCapitalAnswers(Question q){


        ArrayList<String> all = new ArrayList<String>();
        for(int i = 0; i < q.answersC.size(); i++){
            for(int j = 0; j < q.answersC.get(i).bigCities.size(); j++){
                all.add(q.answersC.get(i).bigCities.get(j));
            }
        }

        Integer[] arr = new Integer[all.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }

        Collections.shuffle(Arrays.asList(arr));

        String[] ret = new String[3];

        ret[0] = all.get(arr[0]);
        ret[1] = all.get(arr[1]);
        ret[2] = all.get(arr[2]);

        return ret;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_ACCOUNTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                /////////////////////

                logged(true);
                if (!BaseGameUtils.verifySampleSetup(this, R.string.app_id)) {
                    Log.w(TAG, "*** Warning: setup problems detected. Sign in may not work!");
                }
                mSignInClicked = true;
                mGoogleApiClient.connect();

                /////////////////////


                Snackbar.make(tvQuestionText, R.string.accounts_permission_was_granted,
                        Snackbar.LENGTH_SHORT)
                        .show();
            } else {
                Snackbar.make(tvQuestionText, R.string.accounts_permission_was_denied,
                        Snackbar.LENGTH_SHORT)
                        .show();

            }
        }

    }


    public static boolean requestAccountsPermission(final Activity activity, View view){
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_SMS)) {

                Snackbar.make(view, R.string.permission_accounts,
                        Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, new View.OnClickListener() {
                    final Activity act = activity;
                    @Override
                    public void onClick(View view) {
                        ActivityCompat.requestPermissions(act,
                                new String[]{Manifest.permission.GET_ACCOUNTS},
                                PERMISSION_REQUEST_ACCOUNTS);
                    }
                }).show();

            } else {

                Snackbar.make(view,
                        R.string.requesting_permission,
                        Snackbar.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.GET_ACCOUNTS},
                        PERMISSION_REQUEST_ACCOUNTS);
            }
        } else {
            return true;
        }
        return false;
    }

    private boolean isCurrentlySignedIn(){
        return (mGoogleApiClient != null && mGoogleApiClient.isConnected());
    }

    private String longToTimeString(long l){
        DateFormat formatter = new SimpleDateFormat("mm:ss:SS");
        return  formatter.format(l);
    }

    private void gameOver(final boolean submit){
        cmTime.stop();

        final long gameTime = SystemClock.elapsedRealtime() - cmTime.getBase();


        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Game over");
        builder1.setMessage("Your result is " + point + " in " + longToTimeString(gameTime) + ".");
        builder1.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                switchMenuAndGame(false);
                if (isCurrentlySignedIn()) {
                    showSignOutBar();

                    if (submit) {
                        Games.Leaderboards.submitScore(mGoogleApiClient, getResources().getString(R.string.leaderboard_timeattack), gameTime);
                        Games.Leaderboards.submitScore(mGoogleApiClient, getResources().getString(R.string.leaderboard_striker),point);

                        processAchievements((int)point);

                        String sjow = "<font color='blue'>"+getResources().getString(R.string.show)+"</font>.";
                        String subm = "<font color='white'>"+getResources().getString(R.string.scores_submitted)+"</font>.";
                        Snackbar snackbar = Snackbar
                                .make(tvQuestionText, Html.fromHtml(subm), Snackbar.LENGTH_LONG)
                                .setAction(Html.fromHtml(sjow), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        showLeaderboards();
                                    }
                                });

                        snackbar.show();

                    } else {
                        String subm = "<font color='white'>"+getResources().getString(R.string.scores_not_submitted)+"</font>.";
                        Snackbar.make(tvQuestionText, Html.fromHtml(subm), Snackbar.LENGTH_SHORT).show();
                    }

                }

            }
        });
        builder1.setIcon(android.R.drawable.ic_dialog_alert);
        builder1.show();



    }

    private String generatePointString(long point){
        int length = 6;
        String ps = String.valueOf(point);
        String pre = "";
        for(int i = 0; i < (length - ps.length()); i++ ){
            pre += "0";
        }

        return pre + ps;
    }

    private void playAnswerSound(boolean isTrue){


        if(mAoudioManager.isStreamMute(AudioManager.STREAM_SYSTEM)){
            return;
        }

        if(isTrue){
            mpSoundEffectCorrect.start();
        } else {
            mpSoundEffectWrong.start();
        }
    }

    ///////////////////
}
