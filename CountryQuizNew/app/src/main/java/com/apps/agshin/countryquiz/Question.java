package com.apps.agshin.countryquiz;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Created by agshin on 7/1/15.
 */
public class Question {
    public enum Type{
        imageToImages,
        imageToTexts,
        textToImages,
        textToTexts
    }

    public enum Source{
        flags,
        coatOfArms,
        capitals,
        areas,
        populations,
        currencies,
        domains
    }

    public enum Answer{
        notAnswered,
        correct,
        incorrect
    }

    public Answer answer;
    public int correctAnswerId;
    public Type type;
    public Source source;
    public Country questionC;
    public ArrayList<Country> answersC;

}
