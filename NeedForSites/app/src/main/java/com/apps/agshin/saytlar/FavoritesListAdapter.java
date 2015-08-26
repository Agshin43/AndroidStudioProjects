package com.apps.agshin.saytlar;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.agshin.saytlar.R;

import java.util.List;

/**
 * Created by agshin on 4/6/15.
 */
public class FavoritesListAdapter extends ArrayAdapter<Domain> {

    public FavoritesListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public FavoritesListAdapter(Context context, int resource, List<Domain> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.lay_custom_favorite_view, null);

        }

        Domain d = getItem(position);

        if (d != null) {

            final TextView tv_name = (TextView) v.findViewById(R.id.tv_domain_name);
            ImageButton btn_share = (ImageButton) v.findViewById(R.id.btn_share);
            ImageButton btn_remove = (ImageButton) v.findViewById(R.id.btn_remove);
            ImageButton btn_go = (ImageButton) v.findViewById(R.id.btn_go);

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

            btn_remove.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    deleteFavorite(tv_name.getText().toString());
                    MainActivity.loadFavoriteList(getContext());
                }
            });

            if (tv_name != null) {
                tv_name.setText(d.name);
            }
        }

        return v;

    }


    private void saveFavorites()
    {
        SharedPreferences.Editor editor = getContext().getSharedPreferences(MainActivity.MY_PREFS_NAME, getContext().MODE_PRIVATE).edit();

        String putS = "";
        for(int i = 0; i < MainActivity.allFavorites.size(); i++)
        {
            putS += MainActivity.allFavorites.get(i);
            if( i < (MainActivity.allFavorites.size() - 1))
            {
                putS += ",";
            }

        }
        editor.putString("favorites", putS);

        editor.commit();
    }

    private void deleteFavorite(String fav)
    {
        for(int i = 0; i < MainActivity.allFavorites.size(); i++)
        {
            if(fav.equals(MainActivity.allFavorites.get(i)))
            {
                MainActivity.allFavorites.remove(i);
                break;
            }
        }
        if(MainActivity.allFavorites.size() == 0)
        {
            MainActivity.showLayout(MainActivity.LAY_TOUCH_VIEW);
        }
        saveFavorites();

        Toast.makeText(getContext(), getContext().getString(R.string.delete_from_favorites), Toast.LENGTH_SHORT).show();

    }
}

