package com.apps.agshin.countryquiz;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Random;

public class QuestionCreator {
    enum Achievement{
        achievement_well_done,
        achievement_cool,
        achievement_great,
        achievement_honorable,
        achievement_professional,
        achievement_genius
    }

    private QuizManager manager;
    private Achievement achieve;
    public int number;
    private ArrayList<Integer> prevCountryIds;
    public String question;

    public QuestionCreator(Achievement aLevel, QuizManager manager)
    {
        this.manager = manager;
        this.achieve = aLevel;
    }

    public void clearPreviousIds()
    {
        prevCountryIds.clear();
    }

    private Country countryByAchieveAndPrevious()
    {
        Country country = new Country();
        switch (achieve)
        {
            case achievement_well_done:
            {
                break;
            }
            case achievement_cool:
            {
                break;
            }
            case achievement_great:
            {
                break;
            }
            case achievement_honorable:
            {
                break;
            }
            case achievement_professional:
            {
                break;
            }
            case achievement_genius:
            {
                break;
            }
        }
        return country;
    }
    private ArrayList<Question> create(Achievement achieve, int[] prevCountryIds, int count )
    {
        ArrayList<Question> ret = new ArrayList<Question>();
        return ret;
    }

}


