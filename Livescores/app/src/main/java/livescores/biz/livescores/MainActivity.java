package livescores.biz.livescores;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private Spinner spSource;
    ViewPager mViewPager;
    TabLayout mTabLayout;
    private  HTTPFunctions http;
    private  JParse parse;
    private  ArrayList<ArrayList<Match>> matchesArray;
    MPagerAdapter mPagerAdapter;

    private boolean loadingLeagueTask;

    private String mAppId;
    private ArrayList<RecyclerAdapter> fragmentAdapters;

    FloatingActionButton btnCloseTab;


//    private static ArrayList<LeagueWithMatches> leaguesWithMatches;

    private boolean dataIsLoading;
    private boolean dataLoaded;
    private ArrayList<League> leagues;
    private ArrayList<MatchesHelper> matchesHelpers;


    int langId;
    boolean notify;
    boolean autoRefresh;
    int timeOutId;

    final static int REQUEST_CODE_SETTINGS = 0;

    private NavigationView mNavView;
    String[] CATEGORIES;//{"Live", "Today", "Yesterday", "Not Started", "Finished","Tomorrow"};
    final  static int[] TIMEOUTS = {30,45,60,120,180,300,600,1800};


    private boolean readPhoneStatePermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        loadSettings();
        setLanguage();
        setContentView(R.layout.activity_main);

        CATEGORIES = getCategories();

        fragmentAdapters = new ArrayList<>();

        spSource = (Spinner) findViewById(R.id.spSource);
        loadToSpinner();

        matchesHelpers = new ArrayList<>();

        mAppId = myAppId();


        http = new HTTPFunctions();
        parse = new JParse();

        btnCloseTab = (FloatingActionButton) findViewById(R.id.btnCloseTab);

        btnCloseTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = mViewPager.getCurrentItem();
                Log.i("Remove", "mh size = " + matchesHelpers.size() + " pos = " + pos);
                matchesHelpers.remove(pos);
                mPagerAdapter = new MPagerAdapter(getSupportFragmentManager());
                mViewPager.setAdapter(mPagerAdapter);
                mTabLayout.setupWithViewPager(mViewPager);

                mViewPager.setCurrentItem(pos - 1, true);
                mTabLayout.getTabAt(pos - 1).select();
            }
        });



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,R.string.m_open, R.string.m_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mNavView = (NavigationView) findViewById(R.id.navigation_view);
//        mNavView.getMenu().findItem(0).setChecked(true);
        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
//                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                Log.i("nav ---->", "item selected");

                switch (menuItem.getItemId()) {
                    case R.id.nav_item_live_scores: {
                        mViewPager.setCurrentItem(0, true);
                        break;
                    }
                    case R.id.nav_item_finished_scores: {
                        mViewPager.setCurrentItem(4, true);
                        break;
                    }
                    case R.id.nav_item_today_scores: {
                        mViewPager.setCurrentItem(1, true);
                        break;
                    }
                    case R.id.nav_item_not_started_scores: {
                        mViewPager.setCurrentItem(3, true);
                        break;
                    }
                    case R.id.nav_item_yesterday_scores: {
                        mViewPager.setCurrentItem(2, true);
//                        Toast.makeText(getApplicationContext(), "Yesterday", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.nav_item_leagues: {
                        LoadLeaguesTask mTask = new LoadLeaguesTask();
                        if (Build.VERSION.SDK_INT >= 11) {
                            mTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
                        } else {
                            mTask.execute("");
                        }
                        break;
                    }
                    case R.id.nav_item_tables: {
                        LoadTablesTask mTask = new LoadTablesTask();
                        if (Build.VERSION.SDK_INT >= 11) {
                            mTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
                        } else {
                            mTask.execute("");
                        }
                        break;
                    }
                    case R.id.nav_item_tomorrow_scores: {
                        mViewPager.setCurrentItem(5, true);
                        break;
                    }
                    case R.id.nav_item_settings: {
                        Intent i = new Intent(MainActivity.this, Settings.class);
                        MainActivity.this.startActivityForResult(i, REQUEST_CODE_SETTINGS);
                        break;
                    }
                    default:
                        break;
                }
                return true;
            }
        });


        mNavView.setItemTextColor(navigationDrawerItemTextColor());


        mViewPager = (ViewPager)findViewById(R.id.viewpager);


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                if (position < CATEGORIES.length) {
                    mNavView.getMenu().getItem(position).setChecked(true);
                }
                setCloseButtonVisibility(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });



        mTabLayout = (TabLayout)findViewById(R.id.tablayout);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                mNavView.getMenu().getItem(position).setChecked(true);
                mViewPager.setCurrentItem(position);

                setCloseButtonVisibility(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });




        AsyncTask task = new AsyncTask<String,String,String> (){

            ProgressDialog dialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(MainActivity.this);
                dialog.setMessage(getResources().getString(R.string.ms_loading));
                this.dialog.show();
            }
            @Override
            protected String doInBackground(String... urls) {
                loadMatches();
                return "";
            }

            @Override
            protected void onPostExecute(String result){
                mPagerAdapter = new MPagerAdapter(getSupportFragmentManager());
                mViewPager.setAdapter(mPagerAdapter);
                mTabLayout.setupWithViewPager(mViewPager);
                mViewPager.setCurrentItem(0);
                dialog.dismiss();

            }
        };

        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,null);

    }

    public void setLanguage(){
        String[] ls = {"en","ru","az","tr"};


        Locale locale = new Locale(ls[langId]);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_SETTINGS){
            if (resultCode == RESULT_OK) {
                if(data.getBooleanExtra("finish", false)){
                    this.finish();
                }
                notify = data.getBooleanExtra("notify",false);
                autoRefresh = data.getBooleanExtra("autoRefresh", false);
                timeOutId = data.getIntExtra("timeOutId", 0);
                langId = data.getIntExtra("langId",0);


//                setLanguage();
            }
        }
    }

    private void setCloseButtonVisibility(int position){
        if(position >= CATEGORIES.length){
            btnCloseTab.setVisibility(View.VISIBLE);
        } else {
            btnCloseTab.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            Log.i("ERROR", ev.toString());
            return false;
        }
    }

    public void loadMatches(){
        String ss = http.getJson("data.php?app_id="+mAppId);
        try {
            matchesArray = parse.getSeparatedMatches(ss);

            if(matchesHelpers.size()<CATEGORIES.length){
                Log.i("Load matches", "MA size"+matchesArray.size());
                for(int i = 0; i < matchesArray.size(); i++){
                    matchesHelpers.add(new MatchesHelper(matchesArray.get(i),1, "", CATEGORIES[i], false, true));
                }
            } else {
                for(int i = 0; i < matchesArray.size(); i++){
                    matchesHelpers.set(i, new MatchesHelper(matchesArray.get(i), 1, "", CATEGORIES[i], false, true));
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private boolean getMatches(int type, String leagueId, boolean force, int tabPosition){
        boolean ret = false;

        if(type == 1){
            Log.i("getMatches", " 1");

            if(force){
                Log.i("getMatches", " 2");
                if(!matchesHelpers.get(tabPosition).isLoading() && matchesHelpers.get(tabPosition).isLoaded()){
                    new LoadDataTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
                }
            } else {
                if(matchesHelpers.get(tabPosition).isLoading()){
                    Log.i("getMatches", " 3");
                    ret = false;
                } else if(matchesHelpers.get(tabPosition).isLoaded()){
                    Log.i("getMatches", " 4");
                    ret = true;
                }
            }
        } else if(type == 2){
            if(force){
                if(!matchesHelpers.get(tabPosition).isLoading() && matchesHelpers.get(tabPosition).isLoaded()){
                    new LoadLeagueTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,leagueId);
                }
            } else {
                if(matchesHelpers.get(tabPosition).isLoading()){
                    ret = false;
                } else if(matchesHelpers.get(tabPosition).isLoaded()){
                    ret = true;
                } else {
                    matchesHelpers.get(tabPosition).setIsLoading(true);
                    new LoadLeagueTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, leagueId);
                    ret = false;
                }
            }
        } else if(type == 3){

            Log.i("GET MATCHES", "TYPE = 3");
            if(force){
                if(!matchesHelpers.get(tabPosition).isLoading() && matchesHelpers.get(tabPosition).isLoaded()) {
                    new LoadDayTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, leagueId);
                }
            } else {
                if(matchesHelpers.get(tabPosition).isLoading()){
                    ret = false;
                } else if(matchesHelpers.get(tabPosition).isLoaded()){
                    ret = true;
                } else {
                    matchesHelpers.get(tabPosition).setIsLoading(true);
                    new LoadDayTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, leagueId);
                    ret = false;
                }
            }
        }

        return ret;
    }


    //
//    private boolean getMatches(int type, String leagueId, boolean force){
//
//
//        if(type == 1){
//            Log.i("MNH","get matches type = 1");
//            if(dataIsLoading){
//                Log.i("MNH","get matches data is loading return false");
//                return false;
//            }
//            if(!dataLoaded || force){
//                Log.i("MNH","get matches !dataLoaded || force");
//                //load in async task
//                new LoadDataTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"");
//                //set is loading true
//            } else {
//                Log.i("MNH","get matches !dataLoaded || force else return true");
//
//                return true;
//            }
//        } else {
//            for(int i = 0; i < leaguesWithMatches.size(); i++){
//                if(leaguesWithMatches.get(i).getId() == leagueId ){
//                    if(leaguesWithMatches.get(i).isLoading()){
//                        return false;
//                    }
//                    if(!leaguesWithMatches.get(i).isLoaded() || force){
//                        new LoadLeagueTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,leagueId,String.valueOf(i));
//                    } else {
//                        return true;
//                    }
//                }
//            }
//        }
//
//        return true;
//    }
    private class LoadDataTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            loadMatches();
            return "";
        }

        @Override
        protected void onPostExecute(String result){
            for(int i = 0; i < CATEGORIES.length; i++){
                matchesHelpers.get(i).setIsLoading(false);
                matchesHelpers.get(i).setLoaded(true);
            }
        }
    }


    private class LoadLeaguesTask extends AsyncTask<String,Void,String> {

//        ArrayList<League> leags;
        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage(getResources().getString(R.string.ms_loading));
            this.dialog.show();
        }
        @Override
        protected String doInBackground(String... urls) {
            String ss = http.getJson("leagues/index.php");
            leagues = parse.generateLeagueList(ss);
            return "";
        }

        @Override
        protected void onPostExecute(String result){
//            Toast.makeText(getApplicationContext(), "get leagues on post execute", Toast.LENGTH_SHORT).show();
            displayLeaguesDialog();
            dialog.dismiss();
        }
    }
    private class LoadTablesTask extends AsyncTask<String,Void,String> {

        ArrayList<League> countries;
        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage(getResources().getString(R.string.ms_loading));
            this.dialog.show();
        }
        @Override
        protected String doInBackground(String... urls) {
//            Toast.makeText(getApplicationContext(), "get leagues do in background", Toast.LENGTH_SHORT).show();
            String ss = http.getJson("tables/index.php");
            countries = parse.generateLeagueList(ss);
            return "";
        }

        @Override
        protected void onPostExecute(String result){
            displayCountriesDialog(countries);
            dialog.dismiss();
        }
    }


    private class LoadDayTask extends AsyncTask<String,Void,String> {


        String lid = "";
        ArrayList<Match> ret;

        @Override
        protected String doInBackground(String... id) {
            lid = id[0];
            setProgress(0);
            String ss = http.getJson("data.php?day="+id[0]);
            Log.i("LDT","HTTP URL "+ss);
            setProgress(50);
            try {
                ret = parse.getMatches(ss);
                setProgress(60);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setProgress(100);
            return "";
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);
            boolean go = false;
            for(int i = 5; i < matchesHelpers.size(); i++){
                if(matchesHelpers.get(i).getLeagueId() == lid){
                    go = true;
                    break;
                }
            }
            if(!go){
                this.cancel(true);
            }
        }

        @Override
        protected void onPostExecute(String result){
            Log.i("..._______", "DAY " + lid);
            for(int i = 0; i < matchesHelpers.size(); i++){
                Log.i("DDDDDDDD","DAY LOADED "+i+" = "+matchesHelpers.get(i).getLeagueId());
                if(matchesHelpers.get(i).getLeagueId() == lid){
                    matchesHelpers.get(i).setMatches(ret);
                    matchesHelpers.get(i).setLoaded(true);
                    matchesHelpers.get(i).setIsLoading(false);
                }
            }
        }
    }


    private class LoadLeagueTask extends AsyncTask<String,Void,String> {

        //finde
        String lid = "";
        ArrayList<Match> ret;

        ProgressDialog pDialog;


        @Override
        protected void onPreExecute() {
            loadingLeagueTask = false; // TODO
            super.onPreExecute();
            if(loadingLeagueTask ){
                pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setMessage(getApplicationContext().getResources().getString(R.string.ms_loading));
                pDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... id) {
            lid = id[0];
            setProgress(0);
            String ss = http.getJson("leagues/view.php?id="+id[0]);
            loadingLeagueTask = true;
            setProgress(50);
            try {
                ret = parse.getMatches(ss);
                setProgress(60);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setProgress(100);
            return "";
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);
            boolean go = false;
            for(int i = 5; i < matchesHelpers.size(); i++){
                if(matchesHelpers.get(i).getLeagueId() == lid){
                    go = true;
                    break;
                }
            }
            if(!go){
                this.cancel(true);
            }
        }

        @Override
        protected void onPostExecute(String result){
            Log.i("...}}}}","LEAGUE "+lid);
            for(int i = 0; i < matchesHelpers.size(); i++){
                Log.i("OOOOOOOOOOO","LEAGUE LOADED "+i+" = "+matchesHelpers.get(i).getLeagueId());
                if(matchesHelpers.get(i).getLeagueId() == lid){
                    matchesHelpers.get(i).setMatches(ret);
                    matchesHelpers.get(i).setLoaded(true);
                    matchesHelpers.get(i).setIsLoading(false);
                }
            }
            if(pDialog != null){
                pDialog.dismiss();
                loadingLeagueTask = false;
            }
        }
    }

    public ArrayList<String> getTitles(){
        ArrayList<String> ret = new ArrayList<>();
        for(int i = 0; i<matchesHelpers.size(); i++){
            ret.add(matchesHelpers.get(i).getName());
        }
        return ret;
    }

//    public ArrayList<String> getTitles(){
//        ArrayList<String> sl = new ArrayList<>();
//
//        for(int i = 0; i < CATEGORIES.length;i++){
//            sl.add(CATEGORIES[i]);
//        }
//
//        for(int i = 0; i < leaguesWithMatches.size();i++){
//            sl.add(leaguesWithMatches.get(i).getName());
//        }
//
//        return sl;
//    }

    private void loadToSpinner(){
        List<String> items = new ArrayList<String>();

        items.add("Football");
        items.add("Cricket");
        items.add("Hockey");
        items.add("Tennis");
        items.add("Basketball");
        items.add("Handball");
        items.add("Volleyball");
        items.add("American football");
        items.add("Rugby");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, items);
        spSource.setAdapter(dataAdapter);
    }

    private String getDay(int d){
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, d);
        return dateFormat.format(cal.getTime());
    }



    private List<String> getCalendarDays() {
        List<String> ret = new ArrayList<>();

        for(int i=-7;i<8;i++){
            ret.add(getDay(i));
        }


        return ret;
    }

    private void displayCalendarDialog(){

        final List<String> days = getCalendarDays();

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle(R.string.m_select_day);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                MainActivity.this,
                R.layout.dialog_item);

        for(int i = 0; i < days.size(); i++){
            if(i==7){
                arrayAdapter.add(getResources().getString(R.string.m_today));
                continue;
            }
            arrayAdapter.add(days.get(i));

        }

//        String cn = "<font color='black'>"+getResources().getString(R.string.m_cancel)+"</font>";
        builderSingle.setNegativeButton(
                Html.fromHtml(getResources().getString(R.string.m_cancel)),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // load day
//                        addLeagueTab(leagues.get(which).getId(), leagues.get(which).getName());
                        addDayTab(days.get(which), "" + (which - 7));
                    }
                });
        builderSingle.show();
    }


    private void displayLeaguesDialog(){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle(R.string.m_select_a_league);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                MainActivity.this,
                R.layout.dialog_item);

        for(int i = 0; i < leagues.size(); i++){
            arrayAdapter.add(leagues.get(i).getName());
        }

        builderSingle.setNegativeButton(
                getResources().getString(R.string.m_cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // load league
                        addLeagueTab(leagues.get(which).getId(), leagues.get(which).getName());
                    }
                });
        builderSingle.show();
    }

    private void displayCountriesDialog(final ArrayList<League> countries){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle(R.string.m_select_a_league);



        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                MainActivity.this,
                R.layout.dialog_item);

        for(int i = 0; i < countries.size(); i++){
            arrayAdapter.add(countries.get(i).getName());
        }

        String cn = "<font color='black'>"+getResources().getString(R.string.m_cancel)+"</font>";
        builderSingle.setNegativeButton(
                Html.fromHtml(cn),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setSingleChoiceItems(arrayAdapter, 3, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface pDialog, int which) {
                final int w = which;
                AsyncTask task = new AsyncTask<String, String, String>() {
                    ArrayList<League> lgs;
                    ProgressDialog pDialog;
                    boolean success = false;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        pDialog = new ProgressDialog(MainActivity.this);
                        pDialog.setMessage(getApplicationContext().getResources().getString(R.string.ms_loading));
                        pDialog.show();
                    }

                    @Override
                    protected String doInBackground(String... urls) {
                        String js = http.getJson("tables/leagues.php?id=" + countries.get(w).getId());
                        success = (js != null) && (js.length() > 10);
                        if(success){
                            lgs = parse.generateLeagueList(js);
                        }
                        return "";
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        if(success){
                            displayTableViewListDialog(lgs);
                        } else {
                            Snackbar.make(mViewPager,R.string.m_cant_load_data,Snackbar.LENGTH_LONG);
                        }
                        pDialog.dismiss();
                    }
                };

                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
            }
        });
        builderSingle.show();
    }

    private void displayTableViewListDialog(final ArrayList<League> tables){
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle(R.string.m_select_a_league);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                MainActivity.this,
                R.layout.dialog_item);

        for(int i = 0; i < tables.size(); i++){
            arrayAdapter.add(tables.get(i).getName());
        }

        String cn = "<font color='black'>"+getResources().getString(R.string.m_cancel)+"</font>";
        builderSingle.setNegativeButton(
                Html.fromHtml(cn),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


        builderSingle.setSingleChoiceItems(
                arrayAdapter, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int which) {
                        AsyncTask task = new AsyncTask<String, String, String>() {
                            ArrayList<TableTeam> teams;
                            ProgressDialog dialog;

                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                dialog = new ProgressDialog(MainActivity.this);
                                dialog.setMessage(getApplicationContext().getResources().getString(R.string.ms_loading));
                                this.dialog.show();
                            }

                            @Override
                            protected String doInBackground(String... urls) {
                                teams = parse.generateTeams(http.getJson("tables/view.php?id=" + tables.get(which).getId()));
                                return "";
                            }

                            @Override
                            protected void onPostExecute(String result) {
                                TableViewFragment tvf = new TableViewFragment(teams);
                                if (teams.size() > 0) {

                                    tvf.show(MainActivity.this.getFragmentManager(), "");
                                } else {
//                                    Snackbar.make(, "no data", Snackbar.LENGTH_SHORT).show();
                                    Toast.makeText(getApplicationContext(), R.string.m_no_data_to_display, Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            }
                        };

                        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
                    }
                });
        builderSingle.show();
    }



    private void addLeagueTab(String leagueId, String name) {
//        getMatches(2, leagueId, false);
        matchesHelpers.add(new MatchesHelper(2, name, false, false, leagueId));
        mViewPager.getAdapter().notifyDataSetChanged();
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(mTabLayout.getTabCount() - 1);

    }

    private void addDayTab(String name, String day){
        matchesHelpers.add(new MatchesHelper(3, Integer.valueOf(day) == 0 ? getResources().getString(R.string.m_today) : name, false, false, day));
        mViewPager.getAdapter().notifyDataSetChanged();
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(mTabLayout.getTabCount() - 1);
    }



    private ColorStateList navigationDrawerItemTextColor(){
        int[][] state = new int[][] {
                new int[] {-android.R.attr.state_enabled}, // disabled
                new int[] {android.R.attr.state_enabled}, // enabled
                new int[] {-android.R.attr.state_checked}, // unchecked
                new int[] { android.R.attr.state_pressed}  // pressed

        };

        int[] color = new int[] {
                getColorSupport(R.color.m_gray_text),
                getColorSupport(R.color.m_white),
                getColorSupport(R.color.m_white),
                getColorSupport(R.color.m_gray_text)
        };

        return  new ColorStateList(state, color);
    }

    int getColorSupport(int id){
        if(android.os.Build.VERSION.SDK_INT >= 21){
            return ContextCompat.getColor(getApplicationContext(), id);
        } else {
            return getResources().getColor(id);
        }
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) search.getActionView();

        int searchImgId = android.support.v7.appcompat.R.id.search_button;//getResources().getIdentifier("android:id/search_button", null, null);
        ImageView v = (ImageView) searchView.findViewById(searchImgId);
        v.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        v.setImageResource(R.mipmap.ic_search_m);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem calendar = menu.findItem(R.id.calendar);
        calendar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                displayCalendarDialog();
                return false;
            }
        });

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();


            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    Log.i("onQueryTextSubmit","SUBMIT");
                    search(s);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    Log.i("onQueryTextChange",s);
                    search(s);
                    return false;
                }
            });

            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    search("");
                    return false;
                }
            });
        }

        return true;
    }

    private boolean search(String query){

        int c = mViewPager.getCurrentItem();
//        Log.i("SEARCH ", "TAB COUNT = " + mTabLayout.getTabCount() + ", CUR = " + mViewPager.getCurrentItem());
//        fragmentAdapters.get(mViewPager.getCurrentItem()).getFilter().filter(query);
        if(c >= fragmentAdapters.size()){
            return false;
        }
        if(fragmentAdapters.get(c) != null){
            fragmentAdapters.get(c).search(query);
            fragmentAdapters.get(c).notifyDataSetChanged();
            mPagerAdapter.notifyDataSetChanged();
        } else {
            Log.i("Search", "Search did not anything");

        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

//        switch (id) {
//            case android.R.id.home:
//                mDrawerLayout.openDrawer(GravityCompat.START);
//                return true;
//            case R.id.action_settings:
//                return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public class MFragment extends Fragment {
        public SwipeRefreshLayout mSwipeRefreshLayout;
        RecyclerView recyclerView;
        int type;
        boolean needRefresh = false;
        boolean forceRefresh = false;
        private Timer timer;
        private TimerTask timerTask;
        public int tabPosition;
        private String mLigId;


        private boolean exist(){
            if(type == 1){
                return true;
            } else {
                boolean ret = false;
                for(int i = 0; i < matchesHelpers.size(); i++){
                    if(matchesHelpers.get(i).getLeagueId() == mLigId){
                        ret = true;

                        break;
                    }
                }
                return ret;
            }
        }

        public MFragment(){
            super();
        }
        public MFragment( int tabPosition, int type) {
            this.tabPosition = tabPosition;
            this.needRefresh = true;

            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(0);
                }
            };
            timer.scheduleAtFixedRate(timerTask,1000, 1000);
            this.type = type;
            mLigId = matchesHelpers.get(tabPosition).getLeagueId();
        }


        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message message){
                if(message.what == 0){
                    if(!exist()){
                        timer.cancel();
                    }

//                    if(tabPosition > )
                    {
                        if(autoRefresh && (tabPosition < matchesHelpers.size())){
//                            Log.i("AUTOREFRESH","TIMEOUT = "+matchesHelpers.get(tabPosition).getRefreshedSecondsBefore());
                            matchesHelpers.get(tabPosition).increaseRefreshedSecondsBefore(1);
                            if(matchesHelpers.get(tabPosition).getRefreshedSecondsBefore() >= TIMEOUTS[timeOutId]){
                                needRefresh = true;
                                forceRefresh = true;
                                matchesHelpers.get(tabPosition).setRefreshedSecondsBefore(0);
                                mSwipeRefreshLayout.setRefreshing(true);
                            } else {
                                forceRefresh = false;
                            }
                        }
                    }

                    if(needRefresh) {
                        if(type == 1){
                            if(getMatches(type, "", forceRefresh, tabPosition)) {
                                needRefresh = false;
                                forceRefresh = false;
                                if(recyclerView.getAdapter() != null){
                                    recyclerView.getAdapter().notifyDataSetChanged();
                                } else {
                                    recyclerView.setAdapter(new RecyclerAdapter(matchesHelpers.get(tabPosition).getMatches(), MainActivity.this, getSupportFragmentManager()));
                                    mViewPager.getAdapter().notifyDataSetChanged();
                                }
                                mSwipeRefreshLayout.setRefreshing(false);

                            } else {
                                forceRefresh = false;
                            }
                        } else if (type == 2 && (exist())){

                            if(getMatches(type, matchesHelpers.get(tabPosition).getLeagueId(), forceRefresh, tabPosition)) {
                                needRefresh = false;
                                forceRefresh = false;

                                mSwipeRefreshLayout.setRefreshing(false);
                                if (recyclerView.getAdapter() != null) {
                                    recyclerView.getAdapter().notifyDataSetChanged();
                                } else {
                                    recyclerView.setAdapter(new RecyclerAdapter(matchesHelpers.get(tabPosition).getMatches(), MainActivity.this, getSupportFragmentManager()));
                                }

                                mViewPager.getAdapter().notifyDataSetChanged();
                            } else {
                                forceRefresh = false;
                            }
                        } else if(type == 3 && (exist())){
                            if(getMatches(type, matchesHelpers.get(tabPosition).getLeagueId(), forceRefresh, tabPosition)) {
                                needRefresh = false;
                                forceRefresh = false;

                                mSwipeRefreshLayout.setRefreshing(false);
                                if (recyclerView.getAdapter() != null) {
                                    recyclerView.getAdapter().notifyDataSetChanged();
                                } else {
                                    recyclerView.setAdapter(new RecyclerAdapter(matchesHelpers.get(tabPosition).getMatches(), MainActivity.this, getSupportFragmentManager()));
                                }

                                mViewPager.getAdapter().notifyDataSetChanged();
                            } else {
                                forceRefresh = false;
                            }
                        }

                        Log.i("NEED REFRESH", "TAB POSITION "+tabPosition);
                        if(tabPosition <= (fragmentAdapters.size() - 1)){
                            fragmentAdapters.set(tabPosition, (RecyclerAdapter) recyclerView.getAdapter());
                            Log.i(">>>>"," *** *** ADAPTER CHANGED");
                        } else {
                            Log.i(">>>>"," *** ADAPTER ADDED");
                            fragmentAdapters.add((RecyclerAdapter) recyclerView.getAdapter());
                        }

                    }
                }
            }
        };

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_list_view2, container, false);
            recyclerView = (RecyclerView)v.findViewById(R.id.recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            if(type == 1){
                recyclerView.setAdapter(new RecyclerAdapter(matchesHelpers.get(tabPosition).getMatches(), MainActivity.this, getSupportFragmentManager()));
            } else {
                if(matchesHelpers.get(tabPosition).getMatches() != null){
                    recyclerView.setAdapter(new RecyclerAdapter(matchesHelpers.get(tabPosition).getMatches(), MainActivity.this, getSupportFragmentManager()));
                }
            }




            if(android.os.Build.VERSION.SDK_INT < 23){
                recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);                    int topRowVerticalPosition =
                                (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                        mSwipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        int topRowVerticalPosition =
                                (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                        mSwipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
                    }
                });
            } else {
                recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                        int topRowVerticalPosition =
                                (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                        mSwipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);


                    }
                });
            }



            mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);

            mSwipeRefreshLayout.setColorSchemeResources(R.color.m_primary, R.color.m_red_card, R.color.m_pred_blue);

            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    needRefresh = true;
                    forceRefresh = true;
                }
            });
            return v;
        }
    }


    @Override
    protected void onResume() {
        Log.i("On resume ((((","∂∂∂∂∂∂∂∂∂∂∂km2ergkmwergkm˚˚˚∂¬¬¬∂¬µ´ƒ∆∫¬∫∆ø®∫¬ˆ∆");
        loadSettings();
        super.onResume();
    }

    class MPagerAdapter extends FragmentStatePagerAdapter {

        public MPagerAdapter(FragmentManager fm) {
            super(fm);
        }



        @Override
        public Fragment getItem(int position) {
            if(position < CATEGORIES.length ){
                return  new MFragment(position, 1);
            } else {
                int type = matchesHelpers.get(position).getType();
                return new MFragment(position, type);
            }
        }
         @Override
        public int getCount() {
            return matchesHelpers.size();
        }

         @Override
        public CharSequence getPageTitle(int position) {
            return matchesHelpers.get(position).getName();
        }
    }


    private String myAppId(){
        Log.i("CLASSSS","Class name "+getClass().getName());
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        Random r = new Random();

        return telephonyManager.getDeviceId()+"_"+Math.abs(r.nextInt()%100);
    }

    private String[] getCategories(){
//        CATEGORIES = {"Live", "Today", "Yesterday", "Not Started", "Finished","Tomorrow"};
        String[] ret = {"","","","","",""};
        ret[0] = getStringResourceByName("live");
        ret[1] = getStringResourceByName("today");
        ret[2] = getStringResourceByName("yesterday");
        ret[3] = getStringResourceByName("not_started");
        ret[4] = getStringResourceByName("finished");
        ret[5] = getStringResourceByName("tomorrow");
        return ret;
    }


    private String getStringResourceByName(String aString) {
        String packageName = getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getString(resId);
    }
    private void loadSettings() {
        SharedPreferences pref = getSharedPreferences("lscores", MODE_PRIVATE);
        langId      = pref.getInt("langId", -1);
        String[] ls = {"en","ru","az","tr"};
        if (langId == -1){

            String dl = Locale.getDefault().getLanguage();

            for (int i = 0; i < ls.length; i++){
                if(ls[i].equals(dl)){
                    langId = i;
                    saveDefaultLanguage();
                    break;
                }
            }
        }
        notify      = pref.getBoolean("notify", false);
        timeOutId   = pref.getInt("timeOutId", 0);
        autoRefresh = pref.getBoolean("autoRefresh", true);
    }

    private void saveDefaultLanguage(){
        SharedPreferences pref = getSharedPreferences("lscores", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("langId",langId);
        editor.commit();
    }


}
