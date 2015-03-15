package com.apps.akaya.mytests;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import java.util.ArrayList;

public class SpinnerTest extends Activity
{

    Button button_UseSelectedItem;
    Spinner mySpinner;
    ArrayList<CountryInfo> myCountries;
    TextView myTextView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        myCountries = populateList();
        setContentView(R.layout.activity_main);
        mySpinner = (Spinner) findViewById(R.id.SpinnerCustom);
        Spinner OrginalSpinner = (Spinner) findViewById(R.id.SpinnerOrginal);
        button_UseSelectedItem = (Button) findViewById(R.id.buttonUseItem);
        myTextView = (TextView) findViewById(R.id.myTextView);

        CountryAdapter myAdapter = new CountryAdapter(this, android.R.layout.simple_spinner_item, myCountries);

        mySpinner.setAdapter(myAdapter);
        OrginalSpinner.setAdapter(myAdapter);

        button_UseSelectedItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Can also use mySpinner.setOnItemClickListener(......)
                // Using a separate button here as there's often other data to select
                // or if you choose the wrong item.
                CountryInfo myCountry;
                if(mySpinner.getSelectedItem() != null)
                {
                    myCountry = (CountryInfo) mySpinner.getSelectedItem();
                    myTextView.setText(String.format("Country: " + myCountry.getCountryName() + "\t Population: " + myCountry.getCountryPopulation()));
                }
            }
        });


        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                CountryInfo yCountry = (CountryInfo) mySpinner.getSelectedItem();
                Toast.makeText(SpinnerTest.this, yCountry.getCountryName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


    }

    public ArrayList<CountryInfo> populateList()
    {
        ArrayList<CountryInfo> myCountries = new ArrayList<CountryInfo>();
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("USA", 308745538, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Sweden", 9482855, R.drawable.pic_animal16));
        myCountries.add(new CountryInfo("Canada", 34018000, R.drawable.pic_animal16));
        return myCountries;
    }
    public class CountryAdapter extends ArrayAdapter<CountryInfo>
    {
        private Activity context;
        ArrayList<CountryInfo> data = null;

        public CountryAdapter(Activity context, int resource, ArrayList<CountryInfo> data)
        {
            super(context, resource, data);
            this.context = context;
            this.data = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {   // Ordinary view in Spinner, we use android.R.layout.simple_spinner_item
            return super.getView(position, convertView, parent);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent)
        {   // This view starts when we click the spinner.
            View row = convertView;
            if(row == null)
            {
                LayoutInflater inflater = context.getLayoutInflater();
                row = inflater.inflate(R.layout.spinner_layout, parent, false);
            }

            CountryInfo item = data.get(position);

            if(item != null)
            {   // Parse the data from each object and set it.
                ImageView myFlag = (ImageView) row.findViewById(R.id.imageIc);
                TextView myCountry = (TextView) row.findViewById(R.id.countryName);
                if(myFlag != null)
                {
                    myFlag.setBackgroundDrawable(getResources().getDrawable(item.getCountryFlag()));
                }
                if(myCountry != null)
                    myCountry.setText(item.getCountryName());

            }

            return row;
        }
    }
}
