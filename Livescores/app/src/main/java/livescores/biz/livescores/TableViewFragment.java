package livescores.biz.livescores;

import android.app.ActionBar;
import android.app.DialogFragment;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class TableViewFragment extends DialogFragment{
    private ArrayList<TableTeam> teams;
    public TableViewFragment(ArrayList<TableTeam> teams) {
        this.teams = teams;
    }

    @Override
    public void onStart() {
        super.onStart();


        String ssh = "<font color='blue' size ='2'>" + teams.get(0).getLeague() + "</font>";
        getDialog().setTitle(Html.fromHtml(ssh));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.table_view, container, false);

        TableLayout tl = (TableLayout) v.findViewById(R.id.table);
        final FloatingActionButton close = (FloatingActionButton) v.findViewById(R.id.btnCloseTable);

        tl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(close.getVisibility() == View.VISIBLE){
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

        Resources res = getResources();
        String[] header = res.getStringArray(R.array.table_header);
        TableRow trh = new TableRow(getActivity());

        TextView tv = new TextView(getActivity());
        tv.setId(0);
        tv.setText("#");
        tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.m_white));
        tv.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        tv.setGravity(Gravity.CENTER | Gravity.LEFT);
        trh.addView(tv);

        for(int i = 1; i <= header.length; i++){
            TextView labelTV = new TextView(getActivity());
            labelTV.setId(0);
            labelTV.setText(header[i - 1]);

            labelTV.setTextColor(ContextCompat.getColor(getActivity(), R.color.m_white));
            labelTV.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            if(i == 1){
                labelTV.setGravity(Gravity.CENTER|Gravity.LEFT);
            } else {
                labelTV.setGravity(Gravity.CENTER);
            }
            trh.addView(labelTV);
        }

        trh.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.m_primary));

        trh.setPadding(0, 0, 0, 10);
        tl.addView(trh, new TableLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        for (int i = 0; i < teams.size(); i++)
        {
            
            int bcolor = i%2 == 1? ContextCompat.getColor(getActivity(), R.color.m_primary_light) : Color.WHITE;


            TableRow tr = new TableRow(getActivity());


            TextView number = new TextView(getActivity());
            number.setId(i);
            number.setText((i + 1) + ".");
            number.setTextColor(Color.BLACK);
            number.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tr.addView(number);

            TextView team = new TextView(getActivity());
            team.setId(i);
            team.setText(teams.get(i).getTeam());
            team.setTextColor(Color.BLACK);
            team.setGravity(Gravity.CENTER|Gravity.LEFT);
            team.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tr.addView(team);

            TextView  game = new TextView(getActivity());
            game.setId(i);
            game.setGravity(Gravity.CENTER);
            game.setText(teams.get(i).getGames());
            game.setTextColor(Color.BLACK);
            game.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tr.addView(game);


            TextView win = new TextView(getActivity());
            win.setId(i);
            win.setGravity(Gravity.CENTER);
            win.setText(teams.get(i).getWin());
            win.setTextColor(Color.BLACK);
            win.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tr.addView(win);

            TextView draw = new TextView(getActivity());
            draw.setId(i);
            draw.setGravity(Gravity.CENTER);
            draw.setText(teams.get(i).getDraw());
            draw.setTextColor(Color.BLACK);
            draw.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tr.addView(draw);

            TextView lost = new TextView(getActivity());
            lost.setId(i);
            lost.setGravity(Gravity.CENTER);
            lost.setText(teams.get(i).getLost());
            lost.setTextColor(Color.BLACK);
            lost.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tr.addView(lost);

            TextView point = new TextView(getActivity());
            point.setId(i);
            point.setGravity(Gravity.CENTER);
            point.setText(teams.get(i).getPoints());
            point.setTextColor(Color.BLACK);
            point.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tr.addView(point);

            tr.setBackgroundColor(bcolor);

            tr.setPadding(0,5,0,5);

            tl.addView(tr, new TableLayout.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
        }


        return v;
    }

}
