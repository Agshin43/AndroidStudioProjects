package com.agshin.brightpolicelights;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Settings extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter patternAdapter;

    private static ArrayList<Pattern> patterns;
    private Spinner sp23;
    private int api23resolved;
    private Button btnSirens;
    private Button btnPatterns;
    private ImageView btnSirenIcon;

    private int lastUsedPatternId;

    final static String SP_1  = "<NAME-ELEMENTS>";
    final static String SP_2  = "<FEATURES>";
    final static String SP_EL = "<ELEMENT>";
    CollapsingToolbarLayout ctbl;
    AppBarLayout abl;
    CoordinatorLayout rootLayout;

    private ArrayList<String> sirenList;

//    private Pattern currentPattern;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        btnPatterns = (Button) findViewById(R.id.btnPatterns);
        btnSirens = (Button) findViewById(R.id.btnSirens);
        btnSirenIcon = (ImageView) findViewById(R.id.btnSirenIcon);
        sp23 = (Spinner) findViewById(R.id.sp23);
        api23resolved = 0;

        btnSirens.setTransformationMethod(null);
        btnPatterns.setTransformationMethod(null);

        btnPatterns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPatternsDialog();
            }
        });


        btnSirenIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySirensDialog();
            }
        });

        btnSirens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySirensDialog();
            }
        });

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        ctbl = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

//        ctbl.setTitle(getResources().getString(R.string.app_name));
        rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);

        abl = (AppBarLayout) findViewById(R.id.appBarLayout);
//        abl.setExpanded(true, true);

        loadSirens();
        readPatterns();
        loadToSpinner();

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this, LinearLayout.VERTICAL, false);


        mRecyclerView.setLayoutManager(mLayoutManager);


        if(lastUsedPatternId != -777) {
            patternAdapter = new CardAdapter(patterns.get(lastUsedPatternId).getElements(), Settings.this, mRecyclerView);
            mRecyclerView.setAdapter(patternAdapter);
        }

        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        api23resolved = 0;
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        if ((api23resolved % 3 == 0) && (api23resolved < 30)) {
                            sp23.setSelection(sp23.getSelectedItemPosition() == 0 ? 1 : 0);
                        }
                        api23resolved++;
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        sp23.setSelection(sp23.getSelectedItemPosition() == 0 ? 1 : 0);
                    }
                    default:
                        break;
                }
                return false;
            }
        });


        final FloatingActionButton btnAddNewPattern = (FloatingActionButton) findViewById(R.id.fabNew);
        btnAddNewPattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPattern();
            }
        });

        final FloatingActionButton btnDeletePattern = (FloatingActionButton) findViewById(R.id.fabDelete);
        btnDeletePattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (patterns.size() == 0) {
                    Snackbar.make(btnDeletePattern, getResources().getString(R.string.nothing_to_delete), Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (patterns.size() == 1) {
                    patterns.clear();

                    mRecyclerView.setAdapter(new CardAdapter(new ArrayList<PatternElement>(), Settings.this, mRecyclerView));
                    loadToSpinner();
                    if(patterns.size() == 0){
                        btnPatterns.setText("- - -");
                        btnSirens.setText("- - -");
                    }
                    Snackbar.make(btnDeletePattern, "Pattern deleted", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                patterns.remove(lastUsedPatternId);
                loadToSpinner();

                if(lastUsedPatternId > 0){
                    lastUsedPatternId -= 1;
                }


                patternAdapter = new CardAdapter(patterns.get(lastUsedPatternId).getElements(), Settings.this, mRecyclerView);
                mRecyclerView.setAdapter(patternAdapter);
                btnPatterns.setText(patterns.get(lastUsedPatternId).getName());

                btnSirens.setText(sirenList.get(patterns.get(lastUsedPatternId).getSirenId()));

                Snackbar.make(btnDeletePattern, "Pattern deleted", Snackbar.LENGTH_SHORT).show();

                writePatterns();



            }
        });


//        spPatterns.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                currentPattern = patterns.get(position);
//                patternAdapter = new CardAdapter(currentPattern.getElements(), Settings.this, mRecyclerView);
//                mRecyclerView.setAdapter(patternAdapter);
//                lastUsedPatternId = position;
//                int sid = patterns.get(lastUsedPatternId).getSirenId();
//
//                btnSirens.setText(sirenList.get(patterns.get(lastUsedPatternId).getSirenId()));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        btnPatterns.setText(patterns.get(lastUsedPatternId).getName());

        final FloatingActionButton btnStart = (FloatingActionButton) findViewById(R.id.fabPlay);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writePatterns();
                if (patterns.size() > 0) {
                    startActivity(new Intent(Settings.this, LightsAndSirens.class));
                } else {
                    Snackbar.make(btnStart, getResources().getString(R.string.there_is_no_any_pattern), Snackbar.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onStop(){
        super.onStop();
        writePatterns();
    }

    private void addPattern(){
        String ss = "<font color='blue'>" + getResources().getString(R.string.enter_a_title) + "</font>";


        View view = LayoutInflater.from(Settings.this).inflate(R.layout.dialog_view, null);
        final AlertDialog d = new AlertDialog.Builder(Settings.this)
                .setView(view)
                .setTitle(Html.fromHtml(ss))
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton( getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();

        final EditText et = (EditText) view.findViewById(R.id.inputTitle);
        et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
//        et.setFocusable(true);
        et.requestFocus();
        d.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (et.getText().length() == 0) {
                            et.setError(getResources().getString(R.string.enter_a_title));
                            return;
                        }
                        patterns.add(new Pattern(singleElementPattern(), et.getText().toString()));
                        loadToSpinner();
                        lastUsedPatternId = patterns.size() - 1;

                        btnPatterns.setText(patterns.get(lastUsedPatternId).getName());

                        patternAdapter = new CardAdapter(patterns.get(lastUsedPatternId).getElements(), Settings.this, mRecyclerView);
                        mRecyclerView.setAdapter(patternAdapter);
                        btnSirens.setText(sirenList.get(patterns.get(lastUsedPatternId).getSirenId()));
                        d.dismiss();
                        Snackbar.make(ctbl, getResources().getString(R.string.pattern_added), Snackbar.LENGTH_SHORT).show();
                        writePatterns();
                    }
                });
            }
        });


        d.show();
    }


    private void displayPatternsDialog(){

        if(patterns.size() == 0){
            return;
        }
        final int[] mid = {-1};

        String ss = "<font color='blue'>" + getResources().getString(R.string.m_select_a_pattern) + "</font>";

        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(Settings.this);
        builderSingle.setTitle(Html.fromHtml(ss));



        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                Settings.this,
                android.R.layout.select_dialog_singlechoice);
        for(int i = 0; i < patterns.size(); i++){
            arrayAdapter.add(patterns.get(i).getName());
        }

        builderSingle.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builderSingle.setPositiveButton(
                "ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mid[0] == -1){
                            dialog.dismiss();
                            return;
                        }
                        lastUsedPatternId = mid[0];
                        btnPatterns.setText(patterns.get(lastUsedPatternId).getName());
                        patternAdapter = new CardAdapter(patterns.get(lastUsedPatternId).getElements(), Settings.this, mRecyclerView);
                        mRecyclerView.setAdapter(patternAdapter);

                        btnSirens.setText(sirenList.get(patterns.get(lastUsedPatternId).getSirenId()));

                        dialog.dismiss();
                    }
                });
        builderSingle.setSingleChoiceItems(arrayAdapter, lastUsedPatternId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mid[0] = which;
            }
        });
        builderSingle.show();
    }

    private void displaySirensDialog(){
        if(patterns.size() == 0){
            return;
        }

        String ss = "<font color='blue'>" + getResources().getString(R.string.m_select_a_siren) + "</font>";

        final int[] mid = {-1};
        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(Settings.this);
        builderSingle.setTitle(Html.fromHtml(ss));



        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                Settings.this,
                android.R.layout.select_dialog_singlechoice);
        for(int i = 0; i < sirenList.size(); i++){
            arrayAdapter.add(sirenList.get(i));
        }

        builderSingle.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builderSingle.setPositiveButton(
                "ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mid[0] == -1){
                            dialog.dismiss();
                            return;
                        }
                        patterns.get(lastUsedPatternId).setSirenId(mid[0]);
                        btnSirens.setText(sirenList.get(mid[0]));
                        dialog.dismiss();



                    }
                });

//        builderSingle.setAdapter(
//                arrayAdapter,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        Toast.makeText(getApplicationContext(), "CLICKed " + which, Toast.LENGTH_SHORT).show();
//
////                        dialog.dismiss();
//                    }
//                });
        builderSingle.setSingleChoiceItems(arrayAdapter, patterns.get(lastUsedPatternId).getSirenId(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(getApplicationContext(), "CLICKed " + which, Toast.LENGTH_SHORT).show();
                    mid[0] = which;
            }
        });
        builderSingle.show();
    }

    private static ArrayList singleElementPattern(){
        ArrayList ret = new ArrayList<>();

        ret.add(new PatternElement(100, PatternElementType.Light, Color.argb(255, 255, 0, 0)));

        return ret;
    }

    private static ArrayList fmPattern(){
        ArrayList ret = new ArrayList<>();

        ret.add(new PatternElement(100, PatternElementType.Light, Color.argb(255, 255, 0, 0)));
        ret.add(new PatternElement(100, PatternElementType.Pause, Color.argb(255, 100, 100, 150)));
        ret.add(new PatternElement(100, PatternElementType.Light, Color.argb(255, 255, 0, 0)));
        ret.add(new PatternElement(100, PatternElementType.Pause, Color.argb(255, 100, 100, 150)));
        ret.add(new PatternElement(100, PatternElementType.Light, Color.argb(255, 255, 0, 0)));
        ret.add(new PatternElement(100, PatternElementType.Pause, Color.argb(255, 100, 100, 150)));
        ret.add(new PatternElement(100, PatternElementType.Light, Color.argb(255, 255, 0, 0)));
        ret.add(new PatternElement(200, PatternElementType.Pause, Color.argb(255, 100, 100, 150)));

        ret.add(new PatternElement(100, PatternElementType.Light, Color.argb(255, 255, 200, 0)));
        ret.add(new PatternElement(100, PatternElementType.Pause, Color.argb(255, 100, 100, 150)));
        ret.add(new PatternElement(100, PatternElementType.Light, Color.argb(255, 255, 200, 0)));
        ret.add(new PatternElement(100, PatternElementType.Pause, Color.argb(255, 100, 100, 150)));
        ret.add(new PatternElement(100, PatternElementType.Light, Color.argb(255, 255, 200, 0)));
        ret.add(new PatternElement(100, PatternElementType.Pause, Color.argb(255, 100, 100, 150)));
        ret.add(new PatternElement(100, PatternElementType.Light, Color.argb(255, 255, 200, 0)));
        ret.add(new PatternElement(200, PatternElementType.Pause, Color.argb(255, 100, 100, 150)));

        return ret;
    }


    private static ArrayList samplePattern(){
        ArrayList ret = new ArrayList<>();

        ret.add(new PatternElement(100, PatternElementType.Light, Color.argb(255, 255, 0, 0)));
        ret.add(new PatternElement(100, PatternElementType.Pause, Color.argb(255, 100, 100, 150)));
        ret.add(new PatternElement(100, PatternElementType.Light, Color.argb(255, 255, 0, 0)));
        ret.add(new PatternElement(100, PatternElementType.Pause, Color.argb(255, 100, 100, 150)));
        ret.add(new PatternElement(100, PatternElementType.Light, Color.argb(255, 255, 0, 0)));
        ret.add(new PatternElement(100, PatternElementType.Pause, Color.argb(255, 100, 100, 150)));
        ret.add(new PatternElement(100, PatternElementType.Light, Color.argb(255, 255, 0, 0)));
        ret.add(new PatternElement(200, PatternElementType.Pause, Color.argb(255, 100, 100, 150)));

        ret.add(new PatternElement(100, PatternElementType.Light, Color.argb(255, 0, 0, 255)));
        ret.add(new PatternElement(100, PatternElementType.Pause, Color.argb(255, 100, 100, 150)));
        ret.add(new PatternElement(100, PatternElementType.Light, Color.argb(255, 0, 0, 255)));
        ret.add(new PatternElement(100, PatternElementType.Pause, Color.argb(255, 100, 100, 150)));
        ret.add(new PatternElement(100, PatternElementType.Light, Color.argb(255, 0, 0, 255)));
        ret.add(new PatternElement(100, PatternElementType.Pause, Color.argb(255, 100, 100, 150)));
        ret.add(new PatternElement(100, PatternElementType.Light, Color.argb(255, 0, 0, 255)));
        ret.add(new PatternElement(200, PatternElementType.Pause, Color.argb(255, 100, 100, 150)));

        return ret;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);



        return true;
    }

//    public static void requestWakePermission(final Activity activity, View view){
//        if (ContextCompat.checkSelfPermission(activity,
//                Manifest.permission.READ_SMS)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
//                    Manifest.permission.READ_SMS)) {
//
//                Snackbar.make(view, R.string.permission_sms_explanation,
//                        Snackbar.LENGTH_INDEFINITE).setAction(R.string.ok, new View.OnClickListener() {
//                    final Activity act = activity;
//                    @Override
//                    public void onClick(View view) {
//                        // Request the permission
//                        ActivityCompat.requestPermissions(act,
//                                new String[]{Manifest.permission.READ_SMS},
//                                PERMISSION_REQUEST_SMS);
//                    }
//                }).show();
//
//            } else {
//
//                Snackbar.make(view,
//                        R.string.requesting_permission,
//                        Snackbar.LENGTH_SHORT).show();
//                ActivityCompat.requestPermissions(activity,
//                        new String[]{Manifest.permission.READ_SMS},
//                        PERMISSION_REQUEST_SMS);
//            }
//        }
//    }

    private void loadToSpinner(){
//        List<String> ptrns = new ArrayList<String>();
//        for (int i = 0; i < patterns.size(); i++){
//            ptrns.add(patterns.get(i).getName());
//        }
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, ptrns);
//        spPatterns.setAdapter(dataAdapter);

        List<String> p23 = new ArrayList<String>();
        for (int i = 0; i < 2; i++){
            p23.add("");
        }
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(this, R.layout.spinner_item, p23);
        sp23.setAdapter(dataAdapter2);
    }

    private void loadSirens(){
        Resources res = getResources();
        String[] sirens = res.getStringArray(R.array.sirens_array);

        this.sirenList = new ArrayList<>();
        for(int i = 0; i < sirens.length; i++){
            this.sirenList.add(sirens[i]);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_add_spl) {
            patterns.add(new Pattern(samplePattern(), getResources().getString(R.string.police_lights)));
            loadToSpinner();
            lastUsedPatternId = patterns.size() - 1;

            btnPatterns.setText(patterns.get(lastUsedPatternId).getName());

            patternAdapter = new CardAdapter(patterns.get(lastUsedPatternId).getElements(), Settings.this, mRecyclerView);
            mRecyclerView.setAdapter(patternAdapter);

            btnSirens.setText(sirenList.get(patterns.get(lastUsedPatternId).getSirenId()));

            writePatterns();
            Snackbar.make(ctbl, getResources().getString(R.string.pattern_added), Snackbar.LENGTH_SHORT).show();
            return true;
        }

        if (id == R.id.action_add_sfl) {
            patterns.add(new Pattern(fmPattern(), getResources().getString(R.string.fire_car_lights)));
            loadToSpinner();
            lastUsedPatternId = patterns.size() - 1;

            btnPatterns.setText(patterns.get(lastUsedPatternId).getName());

            patternAdapter = new CardAdapter(patterns.get(lastUsedPatternId).getElements(), Settings.this, mRecyclerView);
            mRecyclerView.setAdapter(patternAdapter);

            btnSirens.setText(sirenList.get(patterns.get(lastUsedPatternId).getSirenId()));

            Snackbar.make(ctbl, getResources().getString(R.string.pattern_added), Snackbar.LENGTH_SHORT).show();
            writePatterns();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void readPatterns(){
        if(patterns == null){
            patterns = new ArrayList<>();
        } else {
            patterns.clear();
        }

        SharedPreferences sp = this.getSharedPreferences("bpl_", MODE_PRIVATE);

        int ps = sp.getInt("pattern count",0);

        lastUsedPatternId = sp.getInt("last pattern", -777);




        if(ps == 0){
            patterns.add(new Pattern(samplePattern(), "Police Lights"));
        } else {
            for (int i = 0; i < ps; i++){
                patterns.add(new Pattern(sp.getString("pattern "+i,""), SP_1, SP_2, SP_EL));
            }
        }

        if(lastUsedPatternId == -777){
            if(patterns.size() > 0){
                lastUsedPatternId = 0;
            }
        }

        btnSirens.setText(sirenList.get(patterns.get(lastUsedPatternId).getSirenId()));

    }

    private void writePatterns(){
//        patterns.get(lastUsedPatternId).setSirenId(sirenList.get(pa));

        SharedPreferences sp = this.getSharedPreferences("bpl_", MODE_PRIVATE);
        SharedPreferences.Editor editor  = sp.edit();
        editor.putInt("pattern count",patterns.size());
        for(int i = 0; i < patterns.size(); i++){
            String pts = patterns.get(i).toString(SP_1, SP_2, SP_EL);
            Log.i("Pattern",pts );
            editor.putString("pattern "+i, pts);
        }

        editor.putInt("last pattern", lastUsedPatternId);
        editor.commit();
    }
}
