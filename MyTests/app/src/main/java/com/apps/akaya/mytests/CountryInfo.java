package com.apps.akaya.mytests;

public class CountryInfo {
    private String countryName;
    private long countryPopulation;
    private int countryFlag; // Populate it with our resource ID for the correct image.

    public CountryInfo(String cName, long cPopulation, int flagImage)
    {
        countryName = cName;
        countryPopulation = cPopulation;
        countryFlag = flagImage;
    }
    public String getCountryName()
    {
        return countryName;
    }
    public long getCountryPopulation()
    {
        return countryPopulation;
    }
    public int getCountryFlag()
    {
        return countryFlag;
    }
    public String toString()
    {
        return countryName;
    }
}