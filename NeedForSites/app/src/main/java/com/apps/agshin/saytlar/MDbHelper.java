package com.apps.agshin.saytlar;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;

public class MDbHelper extends SQLiteOpenHelper{

    //The Android's default system path of your application database.
    private static String DB_PATH;

    private static String DB_NAME;

    static {
        DB_NAME = "oneminutes";

//        DB_PATH; = "/data/data/com.apps.agshin.needforsites/databases/";
    }

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public MDbHelper(Context context) {

        super(context, DB_NAME, null, 2);
        this.myContext = context;
        if(android.os.Build.VERSION.SDK_INT >= 17) {
            DB_PATH = context.getApplicationInfo().dataDir+"/databases/" ;
        } else {
            DB_PATH = "/data/data/" + context.getPackageName()+"/databases/";
        }
//        DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        Log.i("", ">>>>>>>> Db path:"+DB_PATH);

        try {
            createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {
                copyDataBase();

            } catch (IOException e) {

                Log.e("","copy database error -------"+e.getMessage());
                throw new Error("Error copying database");

            }
        }

    }

    public LinkedList<String> isDbAvailable()
    {
        String lineCount  = Character.toString((char) 35);
        String sourceURL  = Character.toString((char) 64);
        String selectedRowId  = Character.toString((char) 36);
        String lastGotValue  = Character.toString((char) 33);
        String mSource  = Character.toString((char) 38);
        String mDirName  = Character.toString((char) 42);
        String S  = Character.toString((char) 63);
        String sourceIndex  = Character.toString((char) 62);
        String port  = Character.toString((char) 60);
        String tableSize = Character.toString((char) 61);
        String removedLineCount = Character.toString((char) 123);
        String savedDataSize = Character.toString((char) 125);

        return new LinkedList<String> (Arrays.asList(lineCount, sourceURL, selectedRowId, lastGotValue, mSource, mDirName, S, sourceIndex, port, tableSize, removedLineCount, savedDataSize));
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){


        File dbFile = myContext.getDatabasePath(DB_NAME);
        return dbFile.exists();
//        SQLiteDatabase checkDB = null;
//
//        try{
//            String myPath = DB_PATH + DB_NAME;
//            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
//
//        }catch(SQLiteException e){
//
//            //database does't exist yet.
//
//        }
//
//        if(checkDB != null){
//
//            checkDB.close();
//
//        }
//
//        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput;
        try {

            myInput= myContext.getAssets().open(DB_NAME);
        } catch (IOException e)
        {
            Log.e("","> > > > > > >  "+e.getMessage());
            throw new IOException(e);
        }

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException{

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY|SQLiteDatabase.NO_LOCALIZED_COLLATORS);

    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public LinkedList<String> readNames(String whereClaus)
    {
        LinkedList<String> ret = new LinkedList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select nm from web_pages "+whereClaus, null);
        try {
            if (c.moveToFirst()) {

                do {
                    ret.add(c.getString(c.getColumnIndex("nm")));

                } while (c.moveToNext());

            }
        } finally {
            if (c != null && !c.isClosed())
                c.close();
        }

        db.close();
        return ret;
    }

    public LinkedList<String> readNamesInRange(int begin, int range)
    {
        LinkedList<String> ret = new LinkedList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select nm from web_pages LIMIT " + range + " OFFSET " + begin, null);
        try {
            if (c.moveToFirst()) {

                do {
                    ret.add(c.getString(c.getColumnIndex("nm")));

                } while (c.moveToNext());

            }
        } finally {
            if (c != null && !c.isClosed())
                c.close();
        }

        db.close();
        return ret;
    }

    public int nameCount()
    {
        int ret = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select count(*) cl from web_pages ", null);
        try {
            if (c.moveToFirst()) {

                do {
                    ret = c.getInt(c.getColumnIndex("cl"));

                } while (c.moveToNext());

            }
        } finally {
            if (c != null && !c.isClosed())
                c.close();
        }

        db.close();
        Log.i("", " <<<<<<<<<<<<<<<<<   <<<<< name count "+ ret);
        return ret;
    }

    public void logTableNames()
    {
        Cursor cursor = getReadableDatabase().
                rawQuery("SELECT nm FROM sqlite_master WHERE type='table'", null);
        if(cursor == null)
        {
            Log.i("", "CURSOR IS NULL");
        }
        while( !cursor.isAfterLast() )
        {
            Log.i("", ">>>>>>>>>>>>>>>>> TABLE: "+cursor.getString(1));
        }
        cursor.close();
    }

    public String readName(int id)
    {
        String ret = "*";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("select nm from web_pages LIMIT 1 OFFSET " + id, null);
        try {
            if (c.moveToFirst()) {

                do {
                    ret = c.getString(c.getColumnIndex("nm"));

                } while (c.moveToNext());

            }
        } finally {
            if (c != null && !c.isClosed())
                c.close();
        }

        db.close();
        return ret;
    }

    public LinkedList<String> searchNames(String whereClaus)
    {
        LinkedList<String> ret = new LinkedList<String>();

        Cursor cursor = getReadableDatabase().
                rawQuery("select nm from web_pages "+whereClaus, null);
        while( !cursor.isAfterLast() )
        {
            ret.add( cursor.getString(cursor.getColumnIndex("nm")) );
        }
        cursor.close();
        return ret;
    }
    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.

}
