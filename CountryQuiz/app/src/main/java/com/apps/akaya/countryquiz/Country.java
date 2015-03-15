package com.apps.akaya.countryquiz;

import android.graphics.Point;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

/**
 * Created by agshin on 12/23/14.
 */
public class Country {
    private int id;
    private int population;
    private float area;
    private String name;
    private String domain;
    private Drawable flag;
    private Drawable coat;
    private int difficulty;
    private ArrayList<Geometry> geometry;

    public Country(int id, int population, float area, String name, String domain, Drawable flag, Drawable coat, int difficulty, ArrayList<Geometry> geometry) {
        this.id = id;
        this.population = population;
        this.area = area;
        this.name = name;
        this.domain = domain;
        this.flag = flag;
        this.coat = coat;
        this.difficulty = difficulty;
        this.geometry = geometry;
    }

    public int getId() {
        return id;
    }

    public int getPopulation() {
        return population;
    }

    public float getArea() {
        return area;
    }

    public String getName() {
        return name;
    }

    public String getDomain() {
        return domain;
    }

    public Drawable getFlag() {
        return flag;
    }

    public Drawable getCoat() {
        return coat;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public ArrayList<Geometry> getGeometry() {
        return geometry;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public void setArea(float area) {
        this.area = area;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setFlag(Drawable flag) {
        this.flag = flag;
    }

    public void setCoat(Drawable coat) {
        this.coat = coat;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setGeometry(ArrayList<Geometry> geometry) {
        this.geometry = geometry;
    }

}
