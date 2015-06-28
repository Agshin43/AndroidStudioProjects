package com.apps.idrak.positiontracker;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class HttpFunctions {

    public HttpFunctions ()
    {

    }

    public static void registerMe(String deviceId, String serverUrl)
    {
        JSONObject jsonObject= new JSONObject();
        String jString = "";
        try {
            jsonObject.put("deviceId", deviceId);
//            jsonObject.put("displayName", myName);
            jString = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                HttpClient httpClient = new DefaultHttpClient();
                String ret = null;
                try {
                    HttpPost request = new HttpPost(params[0]+"/registerMe");
                    StringEntity param =new StringEntity(params[1]);
                    request.addHeader("Content-type", "application/json");
                    request.setEntity(param);
                    HttpResponse response = httpClient.execute(request);
                    ret = response.toString();
                }catch (Exception ex) {
                    Log.e("res", ex.getMessage());
                } finally {
                    httpClient.getConnectionManager().shutdown();
                }
                return ret;
            }

            @Override
            protected void onPostExecute(String s) {
//                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serverUrl, jString);
    }
    public static void setMyConnection(String deviceInfo, String serverUrl)
    {
        JSONObject jsonObject= new JSONObject();
        String jString = "";
        try {
            jsonObject.put("deviceId", deviceInfo);
//            jsonObject.put("displayName", myName);
            jString = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("","set my connection "+deviceInfo.split(",")[1]);
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                HttpClient httpClient = new DefaultHttpClient();
                String ret = null;
                try {
                    HttpPost request = new HttpPost(params[0]+"/setConnection");
                    StringEntity param =new StringEntity(params[1]);
                    request.addHeader("Content-type", "application/json");
                    request.setEntity(param);
                    HttpResponse response = httpClient.execute(request);
                    ret = response.toString();
                }catch (Exception ex) {
                    Log.e("res", ex.getMessage());
                } finally {
                    httpClient.getConnectionManager().shutdown();
                }
                return ret;
            }

            @Override
            protected void onPostExecute(String s) {
//                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serverUrl, jString);
    }

    public static int getLastCommand(String devId, String serverUrl)
    {
        int id = -1;
        RequestTask mTask = new RequestTask();
        String ss = serverUrl+"/getLastCommand/"+devId;

        mTask.execute(ss);
        String jsonStr = "";
        try {
            jsonStr = mTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(jsonStr != null)
        {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                id = jsonObj.getInt("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return id;
    }
    public static int getRegistrationStatus(String devId, String serverUrl)
    {
        int id = -1;
        RequestTask mTask = new RequestTask();
        String ss = serverUrl+"/getRegistrationStatus/"+devId;

        mTask.execute(ss);
        String jsonStr = "";
        try {
            jsonStr = mTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(jsonStr != null)
        {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                id = jsonObj.getInt("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return id;
    }

    public static boolean isTracking(String devId, String serverUrl)
    {
        int id = -1;
        RequestTask mTask = new RequestTask();
        String ss = serverUrl+"/isTracking/"+devId;

        mTask.execute(ss);
        String jsonStr = "";
        try {
            jsonStr = mTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Log.i("","tracking json:"+jsonStr);

        if(jsonStr != null)
        {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                id = jsonObj.getInt("id");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
        {
            return false;
        }

        if(id == 0)
        {
            return  false;
        }
        else
        {
            return true;
        }
    }

    public static void postPosition(String devId, double latitude, double longitude, String date, String serverUrl)
    {
        JSONObject jsonObject= new JSONObject();
        String jString = "";
        try {
            jsonObject.put("deviceId", devId);
            jsonObject.put("latitude", latitude);
            jsonObject.put("longitude", longitude);
            jsonObject.put("date", date);

            jString = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                HttpClient httpClient = new DefaultHttpClient();
                String ret = null;
                try {
                    HttpPost request = new HttpPost(params[0]+"/postPosition");
                    StringEntity param =new StringEntity(params[1]);
                    request.addHeader("Content-type", "application/json");
                    request.setEntity(param);
                    HttpResponse response = httpClient.execute(request);
                    ret = response.toString();
                }catch (Exception ex) {
                    Log.e("res", ex.getMessage());
                } finally {
                    httpClient.getConnectionManager().shutdown();
                }
                return ret;
            }

            @Override
            protected void onPostExecute(String s) {
//                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serverUrl, jString);
    }
}

class RequestTask extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
            response = httpclient.execute(new HttpGet(uri[0]));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out.toString();
                out.close();
            } else {
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            //TODO Handle problems..
        } catch (IOException e) {
            //TODO Handle problems..
        }
        return responseString;
    }
}