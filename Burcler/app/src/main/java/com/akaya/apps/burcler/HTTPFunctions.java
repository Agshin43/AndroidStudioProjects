package com.akaya.apps.burcler;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class HTTPFunctions {

    private Context context;
    final static int READ_TIMEOUT = 20000;
    final static int CONNECT_TIMEOUT = 20000;
    final static String SITE_URL_DAILY = "http://burcler.net/gundelik/";
    final static String SITE_URL_WEEKLY = "http://burcler.net/ayliq/";


    public HTTPFunctions(Context context) {
        this.context = context;
    }

    public String goroscopeByConstellationId(int id, int type){
        Log.i("gbci ****","1");
        Resources res = context.getResources();
        String[] gids = res.getStringArray(R.array.goroscope_ids);

        Log.i("gbci ****","2");
        String ret = "";

        String html = getHTML(gids[id] + "/", type);
        if(html == null){
            return "yüklənmədi. İnternet bağlantınızı yoxlayın";
        }
        Log.i("gbci ****","3 "+ "LENGT = "+html.length());
        String[] l1 = html.split("<div class=\"horoscope\">");

        if(l1.length > 1){
            Log.i("gbci ****","4");
            ret = l1[1].split("</p>")[0].replace("<p>","");
        }

        return ret;
    }
    public String getHTML(String url , int type)
    {
        String p = type == 0 ? SITE_URL_DAILY : SITE_URL_WEEKLY;
        DownloadTask mTask = new DownloadTask();
        String ss = p+url;


        mTask.execute(ss);
        String jsonStr = "";
        try {
            jsonStr = mTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        return jsonStr;
    }

    private class DownloadTask extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection c = null;
            try {
                URL u = new URL(urls[0]);
                c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod("GET");
                c.setRequestProperty("Content-length", "0");
                c.setUseCaches(false);
                c.setAllowUserInteraction(false);
                c.setConnectTimeout(CONNECT_TIMEOUT);
                c.setReadTimeout(READ_TIMEOUT);
                c.connect();
                int status = c.getResponseCode();

                switch (status) {
                    case HttpURLConnection.HTTP_OK:
                    case HttpURLConnection.HTTP_CREATED:
                        BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line+"\n");
                        }
                        br.close();
                        return sb.toString();
                }

            } catch (MalformedURLException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            } catch (UnknownHostException ex){
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (c != null) {
                    try {
                        c.disconnect();
                    } catch (Exception ex) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result){

        }
    }



}


