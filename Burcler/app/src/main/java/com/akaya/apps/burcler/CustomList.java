package com.akaya.apps.burcler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CustomList extends ArrayAdapter<String>{

    private ArrayList<String> informations;
    private final Activity context;
    private final String[] web;
    private String shareString;
    private final ArrayList<Integer> imageId;
    private TextView txtShare;
    public CustomList(Activity context,
                      String[] web, ArrayList<Integer> imageId) {
        super(context, R.layout.custom_view, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;

        initInformations();

    }
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.custom_view, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);


        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText(web[position]);

        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtShare = (TextView) rowView.findViewById(R.id.tv_tv);
                txtShare.setText(Html.fromHtml(informations.get(position)));
                shareString = informations.get(position);
                showInfo();
            }
        });

        imageView.setImageResource(imageId.get(position));
        return rowView;
    }

    private void initInformations(){
        informations = new ArrayList<String>();

        for( int i = 0; i < 12; i++ ){
            informations.add(readFile("con_"+i));
        }
    }

    private String readFile(String fileName)
    {
        String ret = "";
        try {
            int rID = getContext().getResources().getIdentifier(getContext().getPackageName() + ":raw/" + fileName, null, null);
            InputStream inputStream = getContext().getResources().openRawResource(rID);

            if (inputStream != null) {
                InputStreamReader streamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(streamReader);

                String l;
                while (( l = bufferedReader.readLine()) != null) {
                    ret += l;
                }

            }

            inputStream.close(); //close the file
        } catch (java.io.FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    private void showInfo(){

        new AlertDialog.Builder(context)
                .setTitle("")
                .setMessage(Html.fromHtml(shareString))
                        .setPositiveButton(R.string.str_close, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setNeutralButton(R.string.str_share, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                share();
                            }
                        })
                        .setIcon(R.drawable.ic_accordance)
                        .show();
    }

    private void share(){

        String sh = txtShare.getText().toString();
        String ad = "\n Google Play: https://play.google.com/store/apps/details?id=com.akaya.apps.burcler";
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, sh + ad);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }
}
