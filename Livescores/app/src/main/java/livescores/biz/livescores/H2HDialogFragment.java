package livescores.biz.livescores;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by agshin on 12/19/15.
 */
public class H2HDialogFragment extends DialogFragment {
    private RecyclerView mRecyclerView;
//    private MyRecyclerAdapter adapter;
    private JParse parser;
    private HTTPFunctions httpFunctions;
    private String baseMatchId;
    private ArrayList<MatchH2H> matches;
    private Activity activity;
    private FragmentManager manager;


    public H2HDialogFragment(ArrayList<MatchH2H> matches, String baseMatchId, Activity activity, FragmentManager manager) {
        this.activity = activity;
        this.manager = manager;
        this.matches = matches;
        this.baseMatchId = baseMatchId;
        this.parser = new JParse();
        this.httpFunctions = new HTTPFunctions();
    }


//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        AlertDialog.Builder b=  new  AlertDialog.Builder(getActivity())
//                .setTitle("Enter Players")
//                .setPositiveButton("OK",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                // do something...
//                            }
//                        }
//                )
//                .setNegativeButton("Cancel",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                dialog.dismiss();
//                            }
//                        }
//                );
//        return b.create();
//    }

    @Override
    public void onStart() {
        super.onStart();

        if(getDialog() == null){
            return;
        }

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        Log.i("XXXXXXXXXX", "SIZE " + matches.size());

        float px1 = convertDpToPixel(105, getActivity());
        float px2 = convertDpToPixel(105, getActivity());

        getDialog().getWindow().setLayout((int) ((float) size.x * 0.98f), (int)(px1 + Math.min(matches.size() * px2, (int) (size.y * 0.8))));

        if(matches.size() > 0){
            String ssh = "<font color='blue' size ='2'>" + getActivity().getResources().getString(R.string.ms_h2h) + "</font>";
            getDialog().setTitle(Html.fromHtml(ssh));
        }

        getDialog().setCancelable(true);
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//
//        return new AlertDialog.Builder(getActivity())
//                .setTitle("Title")
//                .setPositiveButton("OK",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                // DO SOMETHING
//                            }
//                        }
//                )
//                .setNegativeButton("Cancel",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                // DO SOMETHING
//                            }
//                        }
//                )
//                .create();
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflate layout with recycler view
        View v = inflater.inflate(R.layout.h2h_fragment_dialog, container, false);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);



        v.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(new CustomAdapter());


        return v;
    }

    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{

        public CustomAdapter() {

        }

        private String getDate(long milliSeconds)
        {
            SimpleDateFormat sf = new SimpleDateFormat("dd MMM yyyy");
            return sf.format(new Date(milliSeconds * 1000));
        }

        @Override
        public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_h2h, parent, false);
            return new ViewHolder(v);
        }



        @Override
        public void onBindViewHolder(CustomAdapter.ViewHolder holder, int position) {
            final MatchH2H mhh = matches.get(position);

            int sc1 = Integer.valueOf(mhh.getScore1());
            int sc2 = Integer.valueOf(mhh.getScore2());


            holder.tvDate.setText(getDate(Long.valueOf(mhh.getDate())));
            holder.tvTeam1.setText(mhh.getTeam1());
            holder.tvTeam2.setText(mhh.getTeam2());
            holder.tvScore.setText(mhh.getScore1() + ":" + mhh.getScore2());
            holder.tvLeague.setText(mhh.getLeague());

            holder.tvScore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AsyncTask task = new AsyncTask<String, String, String>() {
                        MatchDetails details;
                        ProgressDialog dialog;

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            dialog = new ProgressDialog(activity);
                            dialog.setMessage(activity.getResources().getString(R.string.ms_loading));
                            this.dialog.show();
                        }

                        @Override
                        protected String doInBackground(String... urls) {
//                adapter = new CustomAdapter(parser.H2HMatches(httpFunctions.getJson("h2h.php?id="+baseMatchId)));
                            details = parser.generateMatchDetails(httpFunctions.getJson("details.php?id=" + mhh.getMatchId()), mhh);
                            return "";
                        }

                        @Override
                        protected void onPostExecute(String result) {
                            MatchDetailsFragment mdf = new MatchDetailsFragment(details);
                            mdf.show(manager,"");
                            dialog.dismiss();
                        }
                    };

                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
                }
            });

            String res = mhh.getResult().replace("win","W").replace("lost","L").replace("draw","D").replace("0:0","-");
            holder.tvResult.setText(res);

            if(sc1 > sc2){
//                holder.tvResult.setText("W");
                holder.tvResult.setBackgroundResource(R.drawable.bg_won);
            } else if(sc1 == sc2){
//                holder.tvResult.setText("D");
                holder.tvResult.setBackgroundResource(R.drawable.bg_draw);
            } else {
//                holder.tvResult.setText("L");
                holder.tvResult.setBackgroundResource(R.drawable.bg_lost);
            }

            holder.tvTeam1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


        }

        private void showDetails(){

        }

        @Override
        public int getItemCount() {
            return matches.size();
        }
        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView tvTeam1;
            TextView tvTeam2;
            TextView tvScore;
            TextView tvResult;
            TextView tvLeague;
            TextView tvDate;
            public ViewHolder(View v) {
                super(v);
                tvTeam1  = (TextView) v.findViewById(R.id.tvTeam1);
                tvTeam2  = (TextView) v.findViewById(R.id.tvTeam2);
                tvDate   = (TextView) v.findViewById(R.id.tvDate);
                tvScore  = (TextView) v.findViewById(R.id.tvScore);
                tvLeague = (TextView) v.findViewById(R.id.tvLeague);
                tvResult = (TextView) v.findViewById(R.id.tvResult);
            }
        }

    }

}