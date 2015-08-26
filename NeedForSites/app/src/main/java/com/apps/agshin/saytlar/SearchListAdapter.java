package com.apps.agshin.saytlar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.apps.agshin.saytlar.R;

import java.util.List;

/**
 * Created by agshin on 4/6/15.
 */
public class SearchListAdapter extends ArrayAdapter<Domain> {

    public SearchListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public SearchListAdapter(Context context, int resource, List<Domain> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.lay_custom_search_view, null);

        }

        Domain d = getItem(position);

        if (d != null) {

            final TextView tv_name = (TextView) v.findViewById(R.id.tv_domain_name);
            ImageButton btn_share = (ImageButton) v.findViewById(R.id.btn_share);
            ImageButton btn_go = (ImageButton) v.findViewById(R.id.btn_go);
            ImageButton btn_fav = (ImageButton) v.findViewById(R.id.btn_add_tofavorites);

            btn_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MainActivity.shareDomain(tv_name.getText().toString(), getContext());
                }
            });
            btn_go.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MainActivity.goThisSite(tv_name.getText().toString(), getContext());
                }
            });

            btn_fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MainActivity.addToFavorites(tv_name.getText().toString(), getContext());
                }
            });

            if (tv_name != null) {
                tv_name.setText(d.name);
            }
        }

        return v;

    }

    public void startShareIntent(String toShare)
    {

    }
}

