package livescores.biz.livescores;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class HTTPFunctions {

    final static int READ_TIMEOUT = 20000;
    final static int CONNECT_TIMEOUT = 20000;
    final static String SITE_URL = "http://livescores.biz/api/";
//    private String getScores() throws IOException {
//        String s = "http://livescores.biz/api/data.php";
//        URL url = null;
//        try {
//            url = new URL(s);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        HttpURLConnection conn;
//        conn = (HttpURLConnection) url.openConnection();
//        conn.connect();
//
//        InputStream is = conn.getInputStream();
//        BufferedReader reader =new BufferedReader(new InputStreamReader(is, "UTF-8"));
//        String webPage = "",data="";
//
//        while ((data = reader.readLine()) != null){
//            webPage += data + "\n";
//        }
//
//        return webPage;
//    }


    public String getJson(String url )
    {
        int id = -1;
        DownloadTask mTask = new DownloadTask();
        String ss = SITE_URL+url;

        Log.i("JSON",ss);

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

    private class DownloadTask extends AsyncTask<String,String,String>{

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


