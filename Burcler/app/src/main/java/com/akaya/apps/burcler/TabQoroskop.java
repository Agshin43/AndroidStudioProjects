package com.akaya.apps.burcler;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class TabQoroskop extends Fragment {

    String html;
    TextView tvQoro;
    PatternMatcherGroupHtml regExp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        html = "";
        regExp = new PatternMatcherGroupHtml();
        readWebpage();

//        DownloadPage dp = new DownloadPage();
//        dp.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://qadinlar.biz/burcler-ve-qoroskop/13443-gundelik-burcler-gunun-qoroskopu-bu-gunun-burcleri.html");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.qoroskop_layout, container, false);
        tvQoro = (TextView) v.findViewById(R.id.tv_qoro);
        return v;
    }


    protected class DownloadPage extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {

            String responseStr = null;

            try {
                for (String url : urls) {
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    HttpGet get = new HttpGet(url);
                    HttpResponse httpResponse = httpClient.execute(get);
                    HttpEntity httpEntity = httpResponse.getEntity();
                    responseStr = EntityUtils.toString(httpEntity);
                }
            } catch (UnsupportedEncodingException e) {

            } catch (ClientProtocolException e) {

            } catch (IOException e) {

            }
            return responseStr;
        }

        protected void onPostExecute(String result) {
            html = result;
            Log.i("", "zibil " + html);
            ArrayList<String> ql = getStringsBetweenTags(result, "<td", "</td>");//regExp.parse((html));

            String tx = ""+ql.size();

            for(int i = 0; i < ql.size(); i++) {
                tx += ql.get(i) + "\n";
            }


            tvQoro.setText(tx);
//            if(ql.size() > 0){
//
//                tvQoro.setText(Html.fromHtml(ql.get(3)));
//            } else {
//                tvQoro.setText(Html.fromHtml("Exp not found"));
//            }
        }
    }

    public void readWebpage() {
        DownloadPage task = new DownloadPage();
        task.execute(new String[] { "http://qadinlar.biz/burcler-ve-qoroskop/13443-gundelik-burcler-gunun-qoroskopu-bu-gunun-burcleri.html" });
    }

    private String adamizeString(String hstr){
        String ret = "";
        try {
            ret = new String(hstr.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return ret;
    }


    public  ArrayList<String> getStringsBetweenTags(String text, String opening, String closing)
    {
        ArrayList<String> result = new ArrayList<String>();
        try
        {
            List<Integer> openings = GetListOfOccurrences(text, opening);
            List<Integer> closings = GetListOfOccurrences(text, closing);
            if(openings.size() != closings.size())
                throw new Exception("There is " + (openings.size() > closings.size() ? "more " : "less ") + opening + " tags than " + closing + "!");
            for(int i = 0; i < openings.size(); i++)
                result.add(text.substring(openings.get(i) + opening.length(), closings.get(i) - openings.get(i) - opening.length()));
        }
        catch(Exception exp) {  }
        return result;
    }


    private static List<Integer> GetListOfOccurrences(String text, String substring)
    {
        List<Integer> pos = null;//new List<Integer>();
        try
        {
            int ind = 0;
            while((ind = text.indexOf(substring, ind)) != -1)
                pos.add(ind++);
        }
        catch(Exception exp) {  }
        return pos;
    }
}