package com.apps.agshin.countryquiz;

import java.util.ArrayList;

public class Country {
    public String continent;
    public String name;
    public String capital;
    public String domain;
    public String currency;
    public boolean selected;
    public ArrayList<String> bigCities;
    public int level;
    public float area;
    public int population;

    public Country(String continent, String name, String capital, String domain, String currency, ArrayList<String> bigCities, int level, float area, int population) {
        this.continent = continent;
        this.name = name;
        this.capital = capital;
        this.domain = domain;
        this.currency = currency;
        this.bigCities = bigCities;
        this.level = level;
        this.area = area;
        this.population = population;
        this.selected = false;
    }

    public Country()
    {

    }
}
