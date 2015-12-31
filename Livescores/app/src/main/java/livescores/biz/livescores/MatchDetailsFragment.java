package livescores.biz.livescores;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.lang.reflect.GenericArrayType;

/**
 * Created by agshin on 12/21/15.
 */
public class MatchDetailsFragment extends DialogFragment {
    MatchDetails details;
    public MatchDetailsFragment(MatchDetails details) {
        this.details = details;
    }

    @Override
    public void onStart() {
        super.onStart();

        String title = "<font color='blue'>"+getResources().getString(R.string.m_match_details)+"</font>";
        getDialog().setTitle(Html.fromHtml(title));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate layout with recycler view
        View v = inflater.inflate(R.layout.match_datails, container, false);


        String minutes = "'";
        try{
            int t = Integer.valueOf(details.getMinutes());
        } catch (Exception e){
            minutes = "";
        }

        ((TextView) v.findViewById(R.id.tvTeam1)).setText(details.getTeam1());
        ((TextView) v.findViewById(R.id.tvTeam2)).setText(details.getTeam2());
        ((TextView) v.findViewById(R.id.tvMatchDate)).setText(details.getDate());
        ((TextView) v.findViewById(R.id.tvMinutes)).setText(details.getMinutes()+minutes);
        ((TextView) v.findViewById(R.id.tvScore)).setText(details.getScore1() + "-"+details.getScore2());





        TableLayout tl = (TableLayout) v.findViewById(R.id.table);
        final FloatingActionButton close = (FloatingActionButton) v.findViewById(R.id.btnCloseDetails);

        tl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (close.getVisibility() == View.VISIBLE) {
                    close.setVisibility(View.INVISIBLE);
                } else {
                    close.setVisibility(View.VISIBLE);
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        for (int i = 0; i < details.getActions().size(); i++)
        {

            int bcolor = i%2 == 1? ContextCompat.getColor(getActivity(), R.color.m_primary_light) : Color.WHITE;


            TableRow tr = new TableRow(getActivity());


            TextView v1 = new TextView(getActivity());
            v1.setId(i);
            v1.setText(details.getActions().get(i).getValue1());
            v1.setTextColor(Color.BLACK);
            v1.setGravity(Gravity.CENTER);
            v1.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tr.addView(v1);

            TextView a = new TextView(getActivity());
            a.setId(i);
            a.setText(details.getActions().get(i).getName());
            a.setTextColor(Color.BLACK);
            a.setGravity(Gravity.CENTER);
            a.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tr.addView(a);

            TextView  v2 = new TextView(getActivity());
            v2.setId(i);
            v2.setGravity(Gravity.CENTER);
            v2.setText(details.getActions().get(i).getValue2());
            v2.setTextColor(Color.BLACK);
            v2.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tr.addView(v2);

            tl.addView(tr, new TableLayout.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));


            tr.setBackgroundColor(bcolor);

            tr.setPadding(0, 5, 0, 5);

            if(tr.getParent()!=null)
                ((ViewGroup)tr.getParent()).removeView(tr);

            tl.addView(tr, new TableLayout.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
        }




        return v;
    }
}
