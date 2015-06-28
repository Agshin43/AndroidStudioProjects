package com.apps.akaya.picnest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MakePicnest extends ActionBarActivity{

    Toolbar toolbar;
    PicsView picsView;
    RelativeLayout layPicsView;

    Spinner picSpinner;
    ArrayList<PicData> myPics;


    private LinearLayout layTools;
    Animation vanish;
    Button btn_copy;
    Button btn_paste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_picnest);

        vanish = AnimationUtils.loadAnimation(MakePicnest.this, R.anim.vanish);
        layTools = (LinearLayout) findViewById(R.id.lay_tools);
        layTools.setVisibility(View.INVISIBLE);

        btn_copy = (Button) findViewById(R.id.btn_copy);
        btn_paste = (Button) findViewById(R.id.btn_paste);

        toolbar = (Toolbar) findViewById(R.id.toolbarv7main);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            // configuring toolbar options
            toolbar.setLogo(R.drawable.ic_image_launcher);
            toolbar.setTitle(R.string.app_name);
            toolbar.setTitleTextColor(Color.rgb(255, 255, 255));
            //------------------------------------------------
        }else {
            Toast.makeText(this, "null toolbar", Toast.LENGTH_SHORT).show();
        }

        layPicsView = ( RelativeLayout ) findViewById(R.id.lay_picsView);

        picsView = new PicsView(this, layPicsView,3, 3, R.drawable.pic_animal10);
        picsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MakePicnest.this, " Pics width = "+picsView.width, Toast.LENGTH_SHORT).show();
            }
        });

        picsView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        layPicsView.addView(picsView);
        picsView.invalidate();
        picsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!picsView.started) {
                    picsView.started = true;

                    LinearLayout layOptions = (LinearLayout) findViewById(R.id.lay_start_options);
//                    layOptions.startAnimation(vanish);

                    picsView.invalidate();
                    layOptions.setVisibility(View.INVISIBLE);
//                    layTools.startAnimation(vanish);
                    layTools.setVisibility(View.INVISIBLE);
                    return;

                }
                if (picsView.selectedPicId != -1) {
                    if(layTools.getVisibility() != View.VISIBLE)
                    {
                        layTools.setVisibility(View.VISIBLE);
//                        layTools.startAnimation(vanish);
                    }
                }
                else
                {
//                    layTools.startAnimation(vanish);
                    layTools.setVisibility(View.INVISIBLE);
                }
            }
        });

        ////////////////////////////////////////////////////////////////////
        final ImageButton btn_choose = (ImageButton) findViewById(R.id.btn_choose);

        // add button listener
        btn_choose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                picSpinner.performClick();
                btn_choose.startAnimation(vanish);
            }
        });


        final ImageButton btnTileSize = (ImageButton) findViewById(R.id.btn_tile_size);


        // add button listener
        btnTileSize.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                btn_choose.startAnimation(vanish);

                LayoutInflater inflater = LayoutInflater.from(MakePicnest.this);
                View layout = inflater.inflate(R.layout.layout_tile_size_dialog, null);

                RelativeLayout mLayout = (RelativeLayout) layout.findViewById(R.id.lay_tile_size);


                final TileSizeMenu tileSizeMenu = new TileSizeMenu(MakePicnest.this, picsView.horizontalCount, picsView.verticalCount,
                        mLayout,picsView.picsBitmap);

                mLayout.addView(tileSizeMenu);

                AlertDialog MyDialog;
                AlertDialog.Builder MyBuilder = new AlertDialog.Builder(MakePicnest.this);
                MyBuilder.setTitle(R.string.resize);
                MyBuilder.setView(layout);
                MyBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        picsView.changeTiles(tileSizeMenu.columnCount, tileSizeMenu.rowCount);
                        picsView.selectedPicId = -1;
                    }
                });
                MyDialog = MyBuilder.create();
                MyDialog.show();
            }
        });

        btn_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_copy.startAnimation(vanish);
            }
        });

        btn_paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_paste.startAnimation(vanish);
            }
        });
        ////////////////////////////////////////////////////////////////////

        myPics = populateList();
        PicAdapter myAdapter = new PicAdapter(this, android.R.layout.simple_spinner_item, myPics);
        picSpinner = (Spinner) findViewById(R.id.sp_pic_select);
        picSpinner.setAdapter(myAdapter);
//        picSpinner.setPopupBackgroundDrawable(getResources().getDrawable(R.drawable.ic_choose));
        picSpinner.setVisibility(RelativeLayout.INVISIBLE);
        picSpinner.setBackground(getResources().getDrawable(R.drawable.ic_choose));
        picSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                PicData pc = (PicData) picSpinner.getSelectedItem();
                try
                {
                    picsView.setPicsBitmap(pc.drawable, false);
                }
                catch(Exception e){
                    System.out.println("An Unknown Error has Occured");
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

    }


    @SuppressWarnings("unused")
    public void onBtnChoosePicClicked(View view)
    {
//        Toast.makeText(this, " Choose clicked ", Toast.LENGTH_LONG).show();
//        customQuickAction.show(view,);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_make_picnest, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private int getDrawableIdByName(String name)
    {
        Resources resources = this.getResources();
        return  resources.getIdentifier(name, "drawable",
                this.getPackageName());

    }
    public ArrayList<PicData> populateList()
    {
        System.gc();
        ArrayList<PicData> pics = new ArrayList<PicData>();

        ArrayList<String> hearts = new ArrayList<String>();

        String[] itemNames = getResources().getStringArray(R.array.pics_array);
        for (int i = 0;i < itemNames.length; i++) {
            String[] ss = itemNames[i].split(",");
            int id = getResources().getIdentifier(ss[0], "drawable", getPackageName());
            pics.add(new PicData(id , ss[1]));
        }

        return pics;
    }
    public class PicAdapter extends ArrayAdapter<PicData>
    {
        private Activity context;
        ArrayList<PicData> data = null;

        public PicAdapter(Activity context, int resource, ArrayList<PicData> data)
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

            PicData item = data.get(position);

            if(item != null)
            {
                ImageView iv_pic = (ImageView) row.findViewById(R.id.imageIc);
                TextView tv_name = (TextView) row.findViewById(R.id.tv_pic_name);
                if(iv_pic != null)
                {
                    iv_pic.setBackground(getResources().getDrawable(item.drawable));
                }
                if(tv_name != null)
                    tv_name.setText(item.name);

            }

            return row;
        }
    }


}
