package com.akaya.apps.bethclip;

/**
 * Created by agshin on 10/22/15.
 */
import java.util.ArrayList;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<ClipboardItem> {

    private ArrayList<ClipboardItem> objects;
    private Context context;

    public CustomAdapter(Context context, int textViewResourceId, ArrayList<ClipboardItem> objects) {
        super(context, textViewResourceId, objects);
        this.objects = objects;
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.custom_list_item, parent,false);
        }

        ClipboardItem cItem = objects.get(position);

        if (cItem != null) {
            ImageView iv_type = (ImageView) v.findViewById(R.id.iv_itemType);
            TextView tv_type = (TextView) v.findViewById(R.id.tv_type);
            TextView tv_date = (TextView) v.findViewById(R.id.tv_date);
            TextView tv_text = (TextView) v.findViewById(R.id.tv_text);
            TextView tv_dev = (TextView) v.findViewById(R.id.tv_dev_name);
//            TextView btd = (TextView) v.findViewById(R.id.desctext);

            int id = 0;
            String ts = "";
            switch (cItem.getType()){
                case 0:{
                    ts = "Text";
                    id = R.drawable.ic_text;
                    break;
                }
                case 1:{
                    ts = "Link";
                    id = R.drawable.ic_link;
                    break;
                }
                case 2:{
                    ts = "Phone number";
                    id = R.drawable.ic_phone;
                    break;
                }
                case 3:{
                    ts = "Email";
                    id = R.drawable.ic_email;
                    break;
                }
                default:break;
            }

            if (iv_type != null){
                iv_type.setBackgroundResource(id);
            }
            if (tv_type != null){
                tv_type.setText(ts);
            }
            if (tv_date != null){
                tv_date.setText(cItem.getDate());
            }
            if (tv_dev != null){
                tv_dev.setText(cItem.getDeviceName());
            }
            if (tv_text != null){
                tv_text.setText(cItem.getText());
            }
        }

        return v;

    }

}
