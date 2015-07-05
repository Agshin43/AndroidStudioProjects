package com.apps.agshin.countryquiz;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class QuizManager {
    ArrayList<Country> countries;
    ArrayList<City> allCities;
    Context context;


    public QuizManager(Context context)
    {
        this.context = context;
        loadCountries("countries");
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
        //   0      1      2        3             4          5  6  7
        //                                  (0     1   2)
        //Europe#Austria#83,858#8,169,929#Vienna+Graz+Linz#Euro#at#1
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
            Log.i("CITY", city.name);
        }

        City city = new City(country.capital, country.name, country.level);
        allCities.add(city);

        country.population = Integer.parseInt(columns[3]);
        country.area = Float.parseFloat(columns[2]);
        country.continent = columns[0];

//        Log.i(" >>>> ","COUNTRY "+country.name+" - "+country.area);

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

        int rID = context.getResources().getIdentifier(context.getPackageName()+":raw/" + fileName, null, null);
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
        ArrayList<Country> ret = new ArrayList<Country>();

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

    public ArrayList<Country> getQuestionsCountries(Achievement achieve, Question.Type type, Question.Source source, int questionCount)
    {
        ArrayList<Country> cList = null;
        switch (achieve)
        {
            case achievement_well_done:
            {
                cList = selectByLevel(1);
                break;
            }
            case achievement_cool:
            {
                cList = selectByLevel(1, 2);
                break;
            }
            case achievement_great:
            {
                cList = selectByLevel(1, 3);
                break;
            }
            case achievement_honorable:
            {
                cList = selectByLevel(2, 3);
                break;
            }
            case achievement_professional:
            {
                cList = selectByLevel(2, 3);
                break;
            }
            case achievement_genius:
            {
                cList = selectByLevel(3);
                break;
            }
        }

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
                        Log.i("QUESTION MEMBER",">>>>>  "+cList.get(i).name);
                        ++cnt;
                    }

                    if( cnt == (questionCount*4) )
                    {
                        break;
                    }
                }
            }

        }


        return returnList;
    }
}
