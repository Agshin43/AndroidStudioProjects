package com.apps.agshin.countryquiz;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Created by agshin on 7/1/15.
 */
public class Question {
    public enum Type{
        imageQuestion,
        textQuestion
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

    public Type type;
    public Source source;
    public String questionS;
    public ArrayList<String> answersS;
    public Drawable questionD;
    public ArrayList<Drawable> answersD;

}
