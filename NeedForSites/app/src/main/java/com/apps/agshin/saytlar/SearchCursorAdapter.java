package com.apps.agshin.saytlar;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.apps.agshin.saytlar.R;

public class SearchCursorAdapter extends CursorAdapter {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    public SearchCursorAdapter(Context context, Cursor c, boolean autoQuery) {
        super(context, c, autoQuery);

        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = mLayoutInflater.inflate(R.layout.lay_custom_search_view, parent, false);
        return v;
    }


    @Override
    public void bindView(View v, Context context, Cursor c) {
        String title = TouchAccessSitesView.humanizeName(c.getString(c.getColumnIndexOrThrow("nm")));

        /**
         * Next set the title of the entry.
         */

        TextView title_text = (TextView) v.findViewById(R.id.tv_domain_name);
        if (title_text != null) {
            title_text.setText(title);
        }
    }
}
