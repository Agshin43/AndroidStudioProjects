package com.akaya.apps.bethclip;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener{

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CustomAdapter clipAdapter;
    private ListView lvClipItems;

    private boolean circleImage = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setBackgroundResource(R.color.ColorDarkBlue);
        navigationView.setItemIconTintList(null);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
//        Snackbar.make(get, "wrty", Snackbar.LENGTH_SHORT).show();

        final ImageView imageView1 = (ImageView) findViewById(R.id.iv_user);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_user);
        MCircleDrawable drw = new MCircleDrawable(bm, getColor(getApplicationContext(), R.color.ColorWhite));
        imageView1.setImageDrawable(drw);
        imageView1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        if (circleImage) {
                            imageView1.setImageDrawable(getResources().getDrawable(R.drawable.ic_user));
                        } else {
                            imageView1.setImageDrawable(new MCircleDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.ic_user), getColor(getApplicationContext(), R.color.ColorWhite)));
                        }
                        break;
                    }
                    default:
                        break;
                }
                circleImage = !circleImage;
                return false;
            }
        });

        addDevicesToNav();


        clipAdapter = new CustomAdapter(getApplicationContext(), R.layout.custom_list_item, sampleClipboards());

        lvClipItems = (ListView) findViewById(R.id.lv_clips);
        lvClipItems.setAdapter(clipAdapter);
        clipAdapter.notifyDataSetChanged();


    }

    private ArrayList sampleClipboards(){
        ArrayList res = new ArrayList<ClipboardItem>();
        //public ClipboardItem(String text, int type, String deviceName, String date) { text, link, phone, email
        res.add(new ClipboardItem("050 123 54 32", 2, "Samsung Note", "23 02 2015 12:45"));
        res.add(new ClipboardItem("agshin.h@gmail.com", 3, "Windows PC", "30 02 2015 11:45"));
        res.add(new ClipboardItem("055 123 54 32", 2, "Windows PC", "10 02 2015 12:45"));
        res.add(new ClipboardItem("\n" +
                "Hi Agshin, Mehrab Huseynzali joined your shared folder \"guitar\"!You can also share \"guitar\" with others.\n" +
                "\n" +
                "Happy sharing!\n" +
                "- The Dropbox Team", 0, "Nexus 5", "03 02 2015 12:45"));
        res.add(new ClipboardItem("http://www.kurikulum.az/index.php/az/interaktiv/suallar", 1, "Nexus 5", "19 02 2015 12:45"));
        res.add(new ClipboardItem("051 123 54 32", 2, "Nexus 5", "11 11 2015 12:45"));
        res.add(new ClipboardItem("050 123 54 32", 2, "Mac Pro", "11 06 2015 12:45"));
        res.add(new ClipboardItem("050 123 54 32", 2, "Nexus 5", "09 02 2015 12:45"));
        res.add(new ClipboardItem("http://tarix.info/kurikkulum/2068-muellimlere-destek-kurikulum-suallari.html",1, "Windows PC", "11 02 2013 12:45"));
        res.add(new ClipboardItem("077 123 54 32", 2, "HTC one", "04 03 2015 12:45"));
        res.add(new ClipboardItem("050 123 54 32", 2, "Nexus 5", "11 04 2015 12:45"));
        res.add(new ClipboardItem("It looks like you have reached a 2 models limit in your account. ", 0, "Nexus 5", "11 02 2015 09:45"));

        return res;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addDevicesToNav(){
        NavigationView navView = (NavigationView) findViewById(R.id.navigation_view);
        Menu m = navView.getMenu();
        SubMenu topChannelMenu = m.addSubMenu(Html.fromHtml("<p><font size=\"3\" color=\"#ffffff\">All devices </font></p>"));
        topChannelMenu.add(1, 0, 0, Html.fromHtml("<p><font size=\"3\" color=\"#ffffff\">Nexus 5 : </font><font size=\"2\" color=\"grey\">Android</font></p>"));
        topChannelMenu.getItem(topChannelMenu.size() - 1).setIcon(R.drawable.ic_cellphone);
        topChannelMenu.add(1, 1, 1, Html.fromHtml("<p><font size=\"3\" color=\"#ffffff\">iPhone 5S : </font><font size=\"2\" color=\"grey\">IOS</font></p>"));
        topChannelMenu.getItem(topChannelMenu.size() - 1).setIcon(R.drawable.ic_cellphone);
        topChannelMenu.add(1,2,2,Html.fromHtml("<p><font size=\"3\" color=\"#ffffff\">My Pc : </font><font size=\"2\" color=\"grey\">Windows 10</font></p>"));
        topChannelMenu.getItem(topChannelMenu.size() - 1).setIcon(R.drawable.ic_desktop);

//        navView.getMenu().removeItem(navView.getMenu().add("").getItemId());

        topChannelMenu.add(2,3,3,Html.fromHtml("<p><font size=\"3\" color=\"#ffffff\">Add device </font></p>"));
        topChannelMenu.getItem(topChannelMenu.size() - 1).setIcon(R.drawable.ic_plus);

//        navView.getMenu().removeItem(navView.getMenu().add("").getItemId());

        topChannelMenu.add(3,4,4,Html.fromHtml("<p><font size=\"3\" color=\"#ffffff\">Netbook : </font><font size=\"2\" color=\"grey\">Windows</font></p>"));
        topChannelMenu.getItem(topChannelMenu.size() - 1).setIcon(R.drawable.ic_desktop);
        topChannelMenu.add(3,5,5,Html.fromHtml("<p><font size=\"3\" color=\"#ffffff\">iPhone 6 : </font><font size=\"2\" color=\"grey\">IOS</font></p>"));
        topChannelMenu.getItem(topChannelMenu.size() - 1).setIcon(R.drawable.ic_cellphone);
        topChannelMenu.add(3,6,6,Html.fromHtml("<p><font size=\"3\" color=\"#ffffff\">Home PC : </font><font size=\"2\" color=\"grey\">Windows 10</font></p>"));
        topChannelMenu.getItem(topChannelMenu.size() - 1).setIcon(R.drawable.ic_desktop);
        topChannelMenu.add(3,7,7,Html.fromHtml("<p><font size=\"3\" color=\"#ffffff\">SONY Xperia Z : </font><font size=\"2\" color=\"grey\">Android</font></p>"));
        topChannelMenu.getItem(topChannelMenu.size() - 1).setIcon(R.drawable.ic_cellphone);
        topChannelMenu.add(3,8,8,Html.fromHtml("<p><font size=\"3\" color=\"#ffffff\">iPhone 4: </font><font size=\"2\" color=\"grey\">IOS</font></p>"));
        topChannelMenu.getItem(topChannelMenu.size() - 1).setIcon(R.drawable.ic_cellphone);

        MenuItem mi = m.getItem(m.size()-1);
        mi.setTitle(mi.getTitle());
    }

    public static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;

        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
