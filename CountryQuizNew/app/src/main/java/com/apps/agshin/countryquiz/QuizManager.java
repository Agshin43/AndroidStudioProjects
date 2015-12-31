package com.apps.agshin.countryquiz;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

public class QuizManager {
    ArrayList<Country> countries;
    ArrayList<City> allCities;
    ArrayList<Question> questions;
    Context context;



    public int currentQuestionId = 0;
    private int correctAnswers;
    private int answeredCount = 0;

    private int curQuizQuestionCount;

    public QuizManager(Context context)
    {
        this.context = context;
        loadCountries("countries");
    }

    public void reset(){
        for(int i = 0; i < countries.size(); i++){
            countries.get(i).selected = false;
        }
        currentQuestionId = 0;
        correctAnswers = 0;
        answeredCount = 0;
        curQuizQuestionCount = 0;
    }

    public int getAnsweredCount() {
        return answeredCount;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public int getCurQuizQuestionCount() {
        return curQuizQuestionCount;
    }

    public Question nextQuestion(){

        if(currentQuestionId < (questions.size() - 1)) {
            currentQuestionId++;
        }
//        //Log.i("QUIZ MANAGER","NEXT "+questions.get(currentQuestionId).answer);
        return questions.get(currentQuestionId);
    }

    public Question prevQuestion(){

        if(currentQuestionId > 0) {
            currentQuestionId--;
        }
        return questions.get(currentQuestionId);
    }

    public Question getQuestion(int index){
        if(index < (questions.size() - 1) && index > 0){
            return questions.get(index);
        } else {
            return null;
        }
    }

    public boolean answer(int questionId, int answerId){
        boolean ret = false;
        if(questions.get(questionId).answer == Question.Answer.notAnswered){

            answeredCount++;
            if(questions.get(questionId).correctAnswerId == answerId){
                questions.get(questionId).answer = Question.Answer.correct;
//                Toast.makeText(context,"Correct",Toast.LENGTH_SHORT).show();
                correctAnswers++;
                ret = true;
            } else {
                questions.get(questionId).answer = Question.Answer.incorrect;
//                Toast.makeText(context,"Wrong. Correct is "+questions.get(questionId).correctAnswerId + " - "+questions.get(questionId).questionC.name,Toast.LENGTH_SHORT).show();
            }

            if(answeredCount == curQuizQuestionCount) {
//                Toast.makeText(context, "Quiz is over. Your result is "+ correctAnswers+"/"+curQuizQuestionCount,Toast.LENGTH_LONG).show();
            }
        }
        return ret;
    }


    enum Achievement{
        achievement_well_done,
        achievement_cool,
        achievement_great,
        achievement_honorable,
        achievement_professional,
        achievement_genius
    }



    private Country generateCountry(String line)
    {
        String[] columns = line.split("#");
        String[] cities = columns[4].split("!");

        Country country = new Country();

        country.level = Integer.parseInt(columns[7]);
        country.domain = columns[6];
        country.currency = columns[5];
        country.capital = cities[0];
        country.bigCities = new ArrayList<String>();
        country.name = columns[1];
        for(int i = 1; i < cities.length; i++)
        {
            City city = new City(cities[i], country.name, country.level);
            allCities.add(city);
            country.bigCities.add(cities[i]);
//            //Log.i("CITY", city.name);
        }

        City city = new City(country.capital, country.name, country.level);
        allCities.add(city);

        country.population = Integer.parseInt(columns[3]);
        country.area = Float.parseFloat(columns[2]);
        country.continent = columns[0];

//        //Log.i(" >>>> ","COUNTRY "+country.name+" - "+country.area);

        return country;
    }
    private void loadCountries(String fileName)
    {
        countries = new ArrayList<Country>();
        allCities = new ArrayList<City>();
        try {
            int rID = context.getResources().getIdentifier(context.getPackageName()+":raw/" + fileName, null, null);
            InputStream inputStream = context.getResources().openRawResource(rID);

            if (inputStream != null) {
                InputStreamReader streamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(streamReader);

                String l;

                while (( l = bufferedReader.readLine()) != null) {
                    if(!l.contains(">>"))
                    {
                        countries.add(generateCountry(l));
                    }
                }

            }

            inputStream.close(); //close the file
        } catch (java.io.FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String loadFile(String fileName) throws IOException
    {
        InputStream iS;

        int rID = context.getResources().getIdentifier(context.getPackageName() + ":raw/" + fileName, null, null);
        iS = context.getResources().openRawResource(rID);

        byte[] buffer = new byte[iS.available()];
        iS.read(buffer);
        ByteArrayOutputStream oS = new ByteArrayOutputStream();
        oS.write(buffer);
        oS.close();
        iS.close();

        return oS.toString();
    }

    private void initCountries()
    {
        String fileString;
        try {
            fileString = loadFile("countries");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private ArrayList<Country> selectByArea( float areaSmall, float areaBig ) {
        ArrayList<Country> ret = new ArrayList<Country>();

        for (Country country : countries)
        {
            if( country.area >=areaSmall && country.area <= areaBig )
            {
                ret.add(country);
            }
        }

        return ret;
    }

    private ArrayList<Country> selectByPopulation( float popSmall, float popBig ) {
        ArrayList<Country> ret = new ArrayList<Country>();

        for (Country country : countries)
        {
            if( country.population >=popSmall && country.population <= popBig )
            {
                ret.add(country);
            }
        }

        return ret;
    }

    private ArrayList<Country> selectByContinent( String continent ) {
        ArrayList<Country> ret = new ArrayList<Country>();

        for (Country country : countries)
        {
            if( country.continent.equals(continent) )
            {
                ret.add(country);
            }
        }

        return ret;
    }

    private ArrayList<Country> selectByCurrency( String currency ) {
        ArrayList<Country> ret = new ArrayList<Country>();

        for (Country country : countries)
        {
            if( country.currency.equals(currency) )
            {
                ret.add(country);
            }
        }

        return ret;
    }

    private ArrayList<Country> selectByLevel( int... levels ) {
        ArrayList<Country> ret = new ArrayList<>();

        for (Country country : countries)
        {
            for(int y = 0; y < levels.length; ++y)
            {
                if( country.level == levels[y] )
                {
                    ret.add(country);
                    continue;
                }
            }
        }

        return ret;
    }

    private ArrayList<Country> countriesByAchievement(Achievement achievement)
    {
        ArrayList<Country> cList = null;
        switch (achievement)
        {
            case achievement_well_done:
            {
                cList = selectByLevel(1, 2, 3);
                break;
            }
            case achievement_cool:
            {
                cList = selectByLevel(2 , 3, 4);
                break;
            }
            case achievement_great:
            {
                cList = selectByLevel(4, 5);
                break;
            }
            case achievement_honorable:
            {
                cList = selectByLevel(6,7);
                break;
            }
            case achievement_professional:
            {
                cList = selectByLevel(8,9);
                break;
            }
            case achievement_genius:
            {
                cList = selectByLevel(8, 9, 10);
                break;
            }
        }

        return cList;

    }
    private ArrayList<Country> getRandomQuestionsCountries(Achievement achieve, int questionCount)
    {
        ArrayList<Country> cList = countriesByAchievement(achieve);

        ArrayList<Country> returnList = new ArrayList<Country>();
        int cnt = 0;
        Random rb = new Random();
        while(cnt < (questionCount*4))
        {
            for(int i = 0; i < cList.size(); i++)
            {
                if(!cList.get(i).selected)
                {
                    if(rb.nextInt() % 5 == 1)
                    {
                        cList.get(i).selected = true;
                        returnList.add(cList.get(i));
                        ++cnt;
                    }

                    if( cnt == ( questionCount * 4 ) )
                    {
                        break;
                    }
                }
            }

        }

        return returnList;
    }

    private ArrayList<Country> getRandomQuestionsCountriesByLevel(int level, int questionCount)
    {
        ArrayList<Country> cList = selectByLevel(level);

        ArrayList<Country> returnList = new ArrayList<Country>();
        int cnt = 0;
        Random rb = new Random();
        while(cnt < (questionCount*4))
        {
            for(int i = 0; i < cList.size(); i++)
            {
                if(!cList.get(i).selected)
                {
                    if(rb.nextInt() % 5 == 1)
                    {
                        cList.get(i).selected = true;
                        returnList.add(cList.get(i));
                        ++cnt;
                    }

                    if( cnt == ( questionCount * 4 ) )
                    {
                        break;
                    }
                }
            }

        }

        return returnList;
    }

    private Question.Type typeByLevel(int level){
        if(level <= 5){
            return Question.Type.textToImages;
        } else {
            return Question.Type.imageToTexts;
        }
    }

    private Question.Source sourceByQuestionNumber(int questionNumber){
        return Question.Source.values()[questionNumber % 4];
    }

    public ArrayList<Question> generateQuizQuestions(int countPerDifficulty){
        ArrayList<Question> ret = new ArrayList<>();

        int qc = 0;
        int ts = 8;
        curQuizQuestionCount = countPerDifficulty * ts;

        for(int di = 1; di <= ts; di++){
            ArrayList<Country> countries = getRandomQuestionsCountriesByLevel(di, countPerDifficulty);
            for( int i = 0; i < countries.size(); i += 4 )
            {
                Question question = new Question();
                Random r = new Random();

                int z = Math.abs(r.nextInt() % 4);

                question.answer = Question.Answer.notAnswered;
                question.type = typeByLevel(di);
                question.source = sourceByQuestionNumber(qc);

                question.answersC = new ArrayList<>();

                switch (question.source)
                {
                    case flags:
                    {
                        question.questionC = countries.get(i + z);
                        question.correctAnswerId  = z;
                        //Log.i("QUIZ MANAGER","===== FLAG QUESTION ===== Type: "+ question.type.name() + "  Level: "+ question.questionC.level );

                        for( int k = 0; k < 4; k++ )
                        {
                            question.answersC.add(countries.get(i + k));
                            //Log.i("QUIZ MANAGER", countries.get(i + k).domain+"_c.png");
                        }
                        //Log.i("QUIZ MANAGER","**********");
                        break;
                    }
                    case coatOfArms:
                    {
                        question.questionC = countries.get(i + z);
                        question.correctAnswerId  = z;
                        //Log.i("QUIZ MANAGER","===== COAT OF ARMS QUESTION ===== Type: "+ question.type.name() + "  Level: "+ question.questionC.level );

                        for( int k = 0; k < 4; k++ )
                        {
                            question.answersC.add(countries.get(i + k));
                            //Log.i("QUIZ MANAGER", countries.get(i + k).domain+"_c");
                        }
                        //Log.i("QUIZ MANAGER","**********");
                        break;
                    }
                    case capitals:
                    {

                        question.questionC = countries.get(i + z);
                        //Log.i("QUIZ MANAGER","===== CAPITALS QUESTIONS ===== Type: "+ question.type.name() + "  Level: "+ question.questionC.level );

                        // Force type
                        question.type = Question.Type.textToTexts;

                        question.correctAnswerId  = z;
                        for( int k = 0; k < 4; k++ )
                        {
                            question.answersC.add(countries.get(i + k));
                            //Log.i("QUIZ MANAGER", countries.get(i + k).domain+"_c.png");
                        }
                        //Log.i("QUIZ MANAGER","**********");
                        break;
                    }
                    case areas:
                    {


                        //Force type
                        if((question.type != Question.Type.textToImages) || (question.type != Question.Type.textToTexts))
                        {
                            question.type = Question.Type.textToTexts;
//                            Random rnf = new Random();
//                            question.type = rnf.nextBoolean() ? Question.Type.textToImages : Question.Type.textToTexts;
                        }


                        if(r.nextBoolean()){
                            int k = 0;
                            for( int t = 0; t < 4; t++ )
                            {
                                question.answersC.add(countries.get(i + t));
                                if(t > 0){
                                    if(countries.get(i + t).area > countries.get(i + k).area){
                                        k = t;
                                    }
                                }
                            }
                            question.questionC = countries.get(i + k);
                            //Log.i("QUIZ MANAGER","===== AREAS QUESTION ===== Type: "+ question.type.name() + "  Level: "+ question.questionC.level );
                            question.correctAnswerId  = k;
                        } else {
                            int k = 0;
                            for( int t = 0; t < 4; t++ )
                            {
                                question.answersC.add(countries.get(i + t));
                                if(t > 0){
                                    if(countries.get(i + t).area < countries.get(i + k).area){
                                        k = t;
                                    }
                                }
                            }
                            question.questionC = countries.get(i + k);
                            //Log.i("QUIZ MANAGER","===== AREAS QUESTION ===== Type: "+ question.type.name() + "  Level: "+ question.questionC.level );
                            question.correctAnswerId  = k;
                        }
                        break;
                    }
                }

                ret.add(question);
                qc++;
            }

        }


        this.questions = ret;
        return ret;
    }

    private ArrayList<Question> countriesToQuestions(ArrayList<Country> countries, Question.Source source, Question.Type  type){

        ArrayList<Question> ret = new ArrayList<>();

        for( int i = 0; i < countries.size(); i += 4 )
        {
            Question question = new Question();
            Random r = new Random();

            int z = Math.abs(r.nextInt() % 4);

            question.answer = Question.Answer.notAnswered;
            question.type = type;
            question.source = source;

            question.answersC = new ArrayList<Country>();

            switch (source)
            {
                case flags:
                {
                    question.questionC = countries.get(i + z);
                    question.correctAnswerId  = z;
                    for( int k = 0; k < 4; k++ )
                    {
                        question.answersC.add(countries.get(i + k));
                        //Log.i("QUIZ MANAGER", countries.get(i + k).domain+"_c.png");
                    }
                    //Log.i("QUIZ MANAGER","**********");
                    break;
                }
                case coatOfArms:
                {
                    question.questionC = countries.get(i + z);
                    question.correctAnswerId  = z;
                    for( int k = 0; k < 4; k++ )
                    {
                        question.answersC.add(countries.get(i + k));
                        //Log.i("QUIZ MANAGER", countries.get(i + k).domain+"_c");
                    }
                    //Log.i("QUIZ MANAGER","**********");
                    break;
                }
                case capitals:
                {
                    question.questionC = countries.get(i + z);
                    question.correctAnswerId  = z;
                    for( int k = 0; k < 4; k++ )
                    {
                        question.answersC.add(countries.get(i + k));
                        //Log.i("QUIZ MANAGER", countries.get(i + k).domain+"_c.png");
                    }
                    //Log.i("QUIZ MANAGER","**********");
                    break;
                }
                case areas:
                {
                    if(r.nextBoolean()){
                        int k = 0;
                        for( int t = 0; t < 4; t++ )
                        {
                            question.answersC.add(countries.get(i + t));
                            if(t > 0){
                                if(countries.get(i + t).area > countries.get(i + k).area){
                                    k = t;
                                }
                            }
                        }
                        question.questionC = countries.get(i + k);
                        question.correctAnswerId  = k;
                    } else {
                        int k = 0;
                        for( int t = 0; t < 4; t++ )
                        {
                            question.answersC.add(countries.get(i + t));
                            if(t > 0){
                                if(countries.get(i + t).area < countries.get(i + k).area){
                                    k = t;
                                }
                            }
                        }
                        question.questionC = countries.get(i + k);
                        question.correctAnswerId  = k;
                    }
                    break;
                }
                case currencies:
                {
                    question.questionC = countries.get(i + z);
                    question.correctAnswerId  = z;
                    for( int k = 0; k < 4; k++ )
                    {
                        question.answersC.add(countries.get(i + k));
                        //Log.i("QUIZ MANAGER", countries.get(i + k).domain+"_c.png");
                    }
                    //Log.i("QUIZ MANAGER","**********");
                    break;
                }
                case domains:
                {
                    question.questionC = countries.get(i + z);
                    question.correctAnswerId  = z;
                    for( int k = 0; k < 4; k++ )
                    {
                        question.answersC.add(countries.get(i + k));
                        //Log.i("QUIZ MANAGER", countries.get(i + k).domain+"_c.png");
                    }
                    //Log.i("QUIZ MANAGER","**********");
                    break;
                }
            }

            ret.add(question);
        }

        return ret;
    }

    public void generateQuestions( int count,Achievement achieve, Question.Type type, Question.Source source)
    {
        ArrayList<Country> countries = getRandomQuestionsCountries(achieve, count);
        this.questions = new ArrayList<>();

        curQuizQuestionCount = count;
        for( int i = 0; i < countries.size(); i += 4 )
        {
            Question question = new Question();
            Random r = new Random();

            int z = Math.abs(r.nextInt() % 4);

            question.answer = Question.Answer.notAnswered;
            question.type = type;
            question.source = source;

            question.answersC = new ArrayList<Country>();

            switch (source)
            {
                case flags:
                {
                    question.questionC = countries.get(i + z);
                    question.correctAnswerId  = z;
                    for( int k = 0; k < 4; k++ )
                    {
                        question.answersC.add(countries.get(i + k));
                        //Log.i("QUIZ MANAGER", countries.get(i + k).domain+"_c.png");
                    }
                    //Log.i("QUIZ MANAGER","**********");
                    break;
                }
                case coatOfArms:
                {
                    question.questionC = countries.get(i + z);
                    question.correctAnswerId  = z;
                    for( int k = 0; k < 4; k++ )
                    {
                        question.answersC.add(countries.get(i + k));
                        //Log.i("QUIZ MANAGER", countries.get(i + k).domain+"_c");
                    }
                    //Log.i("QUIZ MANAGER","**********");
                    break;
                }
                case capitals:
                {
                    question.questionC = countries.get(i + z);
                    question.correctAnswerId  = z;
                    for( int k = 0; k < 4; k++ )
                    {
                        question.answersC.add(countries.get(i + k));
                        //Log.i("QUIZ MANAGER", countries.get(i + k).domain+"_c.png");
                    }
                    //Log.i("QUIZ MANAGER","**********");
                    break;
                }
                case currencies:
                {
                    question.questionC = countries.get(i + z);
                    question.correctAnswerId  = z;
                    for( int k = 0; k < 4; k++ )
                    {
                        question.answersC.add(countries.get(i + k));
                        //Log.i("QUIZ MANAGER", countries.get(i + k).domain+"_c.png");
                    }
                    //Log.i("QUIZ MANAGER","**********");
                    break;
                }
                case areas:
                {
                    if(r.nextBoolean()){
                        int k = 0;
                        for( int t = 0; t < 4; t++ )
                        {
                            question.answersC.add(countries.get(i + t));
                            if(t > 0){
                                if(countries.get(i + t).area > countries.get(i + k).area){
                                    k = t;
                                }
                            }
                        }
                        question.questionC = countries.get(i + k);
                        question.correctAnswerId  = k;
                    } else {
                        int k = 0;
                        for( int t = 0; t < 4; t++ )
                        {
                            question.answersC.add(countries.get(i + t));
                            if(t > 0){
                                if(countries.get(i + t).area < countries.get(i + k).area){
                                    k = t;
                                }
                            }
                        }
                        question.questionC = countries.get(i + k);
                        question.correctAnswerId  = k;
                    }
                    break;
                }
                case domains:
                {
                    question.questionC = countries.get(i + z);
                    question.correctAnswerId  = z;
                    for( int k = 0; k < 4; k++ )
                    {
                        question.answersC.add(countries.get(i + k));
                        //Log.i("QUIZ MANAGER", countries.get(i + k).domain+"_c.png");
                    }
                    //Log.i("QUIZ MANAGER","**********");
                    break;
                }
            }
            this.questions.add(question);
        }


    }
}
