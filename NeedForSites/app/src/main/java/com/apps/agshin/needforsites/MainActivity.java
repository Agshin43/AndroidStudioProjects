package com.apps.agshin.needforsites;


import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends ActionBarActivity {

    TouchAccessSitesView tasView;
    final public static int LAY_TOUCH_VIEW = 0;
    final public static int LAY_FAVORITES = 1;
    final public static int LAY_SEARCH = 2;

    final static String MY_PREFS_NAME = "zonda";

    public static ArrayList<Domain> favoriteList;
    public static ArrayList<String> allFavorites;

    public static FavoritesListAdapter favoriteListAdapdet;

    private ArrayList<Domain> searchList;
    private SearchListAdapter searchListAdapter;


    private ListView lvFavorites;
    private ListView lvSearch;
    private Cursor searchCursor;

    private static long backPressed;

    public static int visibleLayout;

    private Toolbar toolbar;

    public static RelativeLayout layTouchView;
    public static LinearLayout layFavorites;
    public static LinearLayout laySearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            tasView = new TouchAccessSitesView(this, 20 );
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        LinearLayout laySitesView = (LinearLayout) findViewById(R.id.lay_sites_view);
        laySitesView.addView(tasView);

        layTouchView = (RelativeLayout) findViewById(R.id.lay_touch_view);
        layFavorites = (LinearLayout) findViewById(R.id.lay_favorites);
        laySearch = (LinearLayout) findViewById(R.id.lay_search);

        favoriteList = new ArrayList<Domain>();
        searchList   = new ArrayList<Domain>();
        allFavorites = new ArrayList<String>();


        lvFavorites = (ListView) findViewById(R.id.lv_favorites);
        favoriteListAdapdet = new FavoritesListAdapter(getApplicationContext(), R.layout.lay_custom_favorite_view, favoriteList);
        lvFavorites.setAdapter(favoriteListAdapdet);

        lvSearch = (ListView) findViewById(R.id.lv_search);
        searchListAdapter = new SearchListAdapter(getApplicationContext(), R.id.lay_search, searchList);
        lvSearch.setAdapter(searchListAdapter);

        // toolbar///

        toolbar = (Toolbar) findViewById(R.id.toolbarv7main);


        if (toolbar != null) {
            setSupportActionBar(toolbar);
            Log.i(""," TOOLBAR >>>>>>>>>>>>>>>>>>>> ");
            // configuring toolbar options
//            toolbar.setLogo(R.drawable.ic_launch);
//            toolbar.setTitleTextColor(Color.rgb(255, 255, 255));
            toolbar.setTitle(getResources().getString(R.string.app_name));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (visibleLayout)
                    {
                        case LAY_SEARCH:
                            showLayout(LAY_TOUCH_VIEW);
                            break;
                        case LAY_FAVORITES:
                            showLayout(LAY_TOUCH_VIEW);
                            break;
                        case LAY_TOUCH_VIEW:
//                            showLayout(LAY_TOUCH_VIEW);
                            break;
                    }
                }
            });
            //------------------------------------------------
        }
        else
        {
            Toast.makeText(this, "null toolbar", Toast.LENGTH_SHORT).show();
        }


        showLayout(LAY_TOUCH_VIEW);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(MainActivity.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    actionSearch(s);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    if( visibleLayout == LAY_FAVORITES )
                    {
                        actionSearch(s);
                    }
                    return false;
                }
            });

            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
//                    search(visibleLayout,"");
                    return false;
                }
            });
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_favorites) {

            if(loadFavoriteList(getApplicationContext()))
            {
                showLayout(LAY_FAVORITES);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onButtonRandomClicked(View v)
    {
//        tasView.randomizeFingerPoint();
        tasView.beginRandomAnimation();
    }
    public void onButtonAddToFavoritesClicked(View v) {
        addToFavorites(tasView.siteName, getApplicationContext());
    }

    public void onButtonGoClicked(View v)
    {
        goThisSite(tasView.siteName, getApplicationContext());
    }


    public void onButtonShareClicked(View v)
    {
        shareDomain(tasView.siteName, getApplicationContext());
    }



    @Override
    public void onBackPressed() {
        if(visibleLayout == LAY_TOUCH_VIEW)
        {
            if(backPressed + 2000 > System.currentTimeMillis())
            {
                super.onBackPressed();
            }
            else
            {
                Toast.makeText(MainActivity.this, getString(R.string.press_again ), Toast.LENGTH_SHORT).show();
                backPressed = System.currentTimeMillis();
            }
        }
        showLayout(LAY_TOUCH_VIEW);
    }

    private void searchDomains(String keyword)
    {
//        searchList.clear();
//        searchListAdapter.notifyDataSetChanged();

        String hw = tasView.heyvanizeName(keyword);
        final String whereClause = " where nm like'%"+hw+"' or nm like '"+hw+"%' or nm like'%"+hw+"%'";


        final ProgressDialog progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.searching));

        searchList.clear();
        searchListAdapter.notifyDataSetChanged();


        searchListAdapter = new SearchListAdapter(getApplicationContext(), R.id.lay_search, searchList);
        lvSearch.setAdapter(searchListAdapter);

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params)
            {
                LinkedList<String> sr = tasView.myDbHelper.readNames(whereClause);

                for(String nm: sr)
                {
                    Domain dom = new Domain(tasView.humanizeName(nm));
                    searchList.add(dom);
                }

                return null;
            }
            @Override
            protected void onPostExecute(Void v)
            {
                searchListAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }


    private void searchFavorites(String keyword)
    {
        favoriteList.clear();
        favoriteListAdapdet.notifyDataSetChanged();

        for( int i = 0; i < allFavorites.size(); i++ )
        {
            if(!allFavorites.get(i).contains(keyword)) continue;

            Domain dom = new Domain(allFavorites.get(i));
            favoriteList.add(dom);
        }

        favoriteListAdapdet.notifyDataSetChanged();
    }

    public static boolean loadFavoriteList(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        String[] favs = prefs.getString("favorites","").split(",");

        if(favs[0].length() == 0)
        {
            return false;
        }

        allFavorites.clear();
        favoriteList.clear();
        favoriteListAdapdet.notifyDataSetChanged();

        for( int i = 0; i < favs.length; i++ )
        {
            Domain dom = new Domain(favs[i]);
            favoriteList.add(dom);
            allFavorites.add(favs[i]);
        }

        favoriteListAdapdet.notifyDataSetChanged();

        return true;
    }



    public static void addToFavorites(String name, Context context)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        String favs = prefs.getString("favorites", "");

        if(!favs.contains(name))
        {
            if(favs.length() > 0)
                editor.putString("favorites", favs+","+name);
            else
                editor.putString("favorites", name);
            Toast.makeText(context, context.getString(R.string.added_to_favorites), Toast.LENGTH_SHORT).show();
        }

        editor.commit();

    }




    private void actionSearch(String keyword)
    {
        switch (visibleLayout)
        {
            case LAY_SEARCH:
                if(keyword.length() < 3)
                    break;
                searchDomains(keyword);
                showLayout(LAY_SEARCH);
                break;
            case LAY_TOUCH_VIEW:
                if(keyword.length() < 3)
                    break;
                searchDomains(keyword);
                showLayout(LAY_SEARCH);

                break;
            case LAY_FAVORITES:
                searchFavorites(keyword);
                break;
        }
    }

    public static void showLayout(int which)
    {
        switch (which)
        {
            case LAY_TOUCH_VIEW:
            {
                visibleLayout = which;

                layFavorites.setVisibility(LinearLayout.GONE);
//                laySearch.setVisibility(RelativeLayout.GONE);
                layTouchView.setVisibility(RelativeLayout.ABOVE);
                break;
            }
            case LAY_FAVORITES:
            {
                visibleLayout = which;

                layFavorites.setVisibility(RelativeLayout.ABOVE);
                laySearch.setVisibility(RelativeLayout.GONE);
                layTouchView.setVisibility(RelativeLayout.GONE);
                break;
            }
            case LAY_SEARCH:
            {
                visibleLayout = which;

                layFavorites.setVisibility(RelativeLayout.GONE);
                laySearch.setVisibility(RelativeLayout.ABOVE);
                layTouchView.setVisibility(RelativeLayout.GONE);
                break;
            }
        }
    }


    public static void shareDomain(String text, Context context)
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");
        context.startActivity(intent);
    }

    public static void goThisSite(String site, Context context)
    {
        Intent intent = new Intent();
        intent.setType(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("http://www."+site));
        context.startActivity(intent);

    }


}
