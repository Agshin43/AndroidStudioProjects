package livescores.biz.livescores;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.support.v4.app.FragmentManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>  {

    private List<Match> mItems;
    private List<Match> filteredList;
    private Activity context;
    private JParse parse;
    private HTTPFunctions httpFunctions;
    private FragmentManager manager;


//    public MatchFilter filter;

    private String getDate(long milliSeconds)
    {
        SimpleDateFormat sf = new SimpleDateFormat("dd MMM yyyy");
        return sf.format(new Date(milliSeconds * 1000));
    }

    private String getTime(long milliSeconds)
    {
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
        return sf.format(new Date(milliSeconds * 1000));
    }

    RecyclerAdapter(List<Match> items, Activity context, FragmentManager manager) {
        httpFunctions = new HTTPFunctions();
        parse = new JParse();

        this.manager = manager;
        mItems = items;
        filteredList = new ArrayList<>();
        for(int i = 0; i < items.size();i++){
            filteredList.add(items.get(i));
        }
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, viewGroup, false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final Match item = filteredList.get(i);



        viewHolder.tvTeam1.setText(item.getTeam1());
        viewHolder.tvTeam2.setText(item.getTeam2());

        int part = Integer.valueOf(item.getPart());



        if(part == 1 || part == 3){
//            Log.i("part","PART ---- -- -- - --- -- -- ----- -- ----- "+part+"  "+item.getTeam1() + " minutes -- "+item.getMinutes_text());
            boolean bl = true;
            try {
                int m = Integer.valueOf(item.getMinutes_text());
            } catch (Exception e){
                bl = false;

            }
            if(bl || item.getMinutes_text().contains("+")){
                viewHolder.tvMinute.setText(item.getMinutes_text() + "'");
            }
        } else if(part == 2){
            viewHolder.tvMinute.setText("HT");
        } else if(part == 4){
            viewHolder.tvMinute.setText("FT");
        }


        if(part > 0 && part < 5){
            viewHolder.tvScore.setText(item.getScore1()+"-"+item.getScore2());
        } else {
            viewHolder.tvScore.setText("-:-");
        }

        final int yc1 = Integer.valueOf(item.getYellow1());
        final int yc2 = Integer.valueOf(item.getYellow2());

        final int rc1 = Integer.valueOf(item.getRed1());
        final int rc2 = Integer.valueOf(item.getRed2());

//        Log.i("CARDS", " Y1 "+yc1+", R1 "+rc1+" | Y2 "+yc2+", R2 "+rc2+" >>> "+item.getTeam1());

        if(yc1 == 0){
            viewHolder.tvYellow1.setVisibility(View.GONE);
        } else {
            viewHolder.tvYellow1.setText(item.getYellow1());
            viewHolder.tvYellow1.setVisibility(View.VISIBLE);
        }

        if(yc2 == 0){
            viewHolder.tvYellow2.setVisibility(View.GONE);
        } else {
            viewHolder.tvYellow2.setText(item.getYellow2());
            viewHolder.tvYellow2.setVisibility(View.VISIBLE);
        }

        if(rc1 == 0){
            viewHolder.tvRed1.setVisibility(View.GONE);
        } else {
            viewHolder.tvRed1.setText(item.getRed1());
            viewHolder.tvRed1.setVisibility(View.VISIBLE);
        }

        if(rc2 == 0){
            viewHolder.tvRed2.setVisibility(View.GONE);
        } else {
            viewHolder.tvRed2.setText(item.getRed2());
            viewHolder.tvRed2.setVisibility(View.VISIBLE);
        }

        viewHolder.tvDate.setText(getDate(Long.valueOf(item.getTime())));

        viewHolder.tvTime.setText(getTime(Long.valueOf(item.getTime())));

        viewHolder.tvH2H.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask task = new AsyncTask<String, String, String>() {
                    ArrayList<MatchH2H> matchs;
                    ProgressDialog dialog;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        dialog = new ProgressDialog(context);
                        dialog.setMessage(context.getResources().getString(R.string.ms_loading));
                        this.dialog.show();
                    }

                    @Override
                    protected String doInBackground(String... urls) {
//                adapter = new CustomAdapter(parser.H2HMatches(httpFunctions.getJson("h2h.php?id="+baseMatchId)));
                        matchs = parse.H2HMatches(httpFunctions.getJson("h2h.php?id=" + item.getId()));
                        return "";
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        H2HDialogFragment h2hf = new H2HDialogFragment(matchs, item.getId());
                        h2hf.show(manager, "");
                        dialog.dismiss();
                    }
                };

                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);

            }
        });

        viewHolder.tvPred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask task = new AsyncTask<String, String, String>() {
                    Prediction prediction;
                    ProgressDialog dialog;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        dialog = new ProgressDialog(context);
                        dialog.setMessage(context.getResources().getString(R.string.ms_loading));
                        this.dialog.show();
                    }

                    @Override
                    protected String doInBackground(String... urls) {
//                adapter = new CustomAdapter(parser.H2HMatches(httpFunctions.getJson("h2h.php?id="+baseMatchId)));
                        prediction = parse.generatePredictions(httpFunctions.getJson("predict.php?id=" + item.getId()));
                        return "";
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        PredictionsFragment h2hf = new PredictionsFragment(prediction);
                        h2hf.show(manager, "");
                        String ssh = "<font color='blue' size ='2'>" + context.getResources().getString(R.string.ms_prediction) + "</font>";
                        dialog.dismiss();
                    }
                };

                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
            }
        });

        viewHolder.tvScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask task = new AsyncTask<String, String, String>() {
                    MatchDetails details;
                    ProgressDialog dialog;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        dialog = new ProgressDialog(context);
                        dialog.setMessage(context.getResources().getString(R.string.ms_loading));
                        this.dialog.show();
                    }

                    @Override
                    protected String doInBackground(String... urls) {
//                adapter = new CustomAdapter(parser.H2HMatches(httpFunctions.getJson("h2h.php?id="+baseMatchId)));
                        details = parse.generateMatchDetails(httpFunctions.getJson("details.php?id=" + item.getId()), item);
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
    }

    public void search(String s){
        filteredList.clear();
//        if(s.length() == 0){
//            for(int i = 0; i < mItems.size(); i++){
//                filteredList.add(mItems.get(i));
//            }
//            Log.i("SEARCH ", "FL size = "+filteredList.size());
////            notifyDataSetChanged();
////            return;
//        } else {
            for (Match match : mItems) {
                if (match.getTeam1().toLowerCase().contains(s.toString().toLowerCase())) {
                    filteredList.add(match);
                    continue;
                }

                if (match.getTeam2().toLowerCase().contains(s.toString().toLowerCase())) {
                    filteredList.add(match);
                    continue;
                }
            }
            Log.i("SEARCH ", "FL size = "+filteredList.size());
//        }

//        notifyDataSetChanged();

    }


    @Override
    public int getItemCount() {
        if(filteredList == null){
            return 0;
        }
        return filteredList.size();
    }

//    @Override
//    public Filter getFilter() {
//        return new MatchFilter(mItems, this);
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTeam1;
        private final TextView tvTeam2;
        private final TextView tvScore;
        private final TextView tvMinute;
        private final TextView tvPred;
        private final TextView tvH2H;
        private final TextView tvDate;
        private final TextView tvRed1;
        private final TextView tvRed2;
        private final TextView tvYellow1;
        private final TextView tvYellow2;
        private final TextView tvTime;

        ViewHolder(View v) {
            super(v);
            tvTeam1 = (TextView) v.findViewById(R.id.tvTeam1);
            tvTeam2 = (TextView) v.findViewById(R.id.tvTeam2);
            tvScore = (TextView) v.findViewById(R.id.tvScore);
            tvMinute = (TextView) v.findViewById(R.id.tvMinute);
            tvPred =  (TextView) v.findViewById(R.id.tvPred);
            tvH2H = (TextView) v.findViewById(R.id.tvH2H);
            tvDate = (TextView) v.findViewById(R.id.tvDate);
            tvRed1 = (TextView) v.findViewById(R.id.tvRed1);
            tvRed2 = (TextView) v.findViewById(R.id.tvRed2);
            tvYellow1 = (TextView) v.findViewById(R.id.tvYellow1);
            tvYellow2 = (TextView) v.findViewById(R.id.tvYellow2);
            tvTime = (TextView) v.findViewById(R.id.tvTime);
        }
    }

//    private static class MatchFilter extends Filter{
//        final RecyclerAdapter adapter;
//        final List<Match> originalList;
//        final List<Match> filteredList;
//
//        public MatchFilter(List<Match> originalList, RecyclerAdapter adapter) {
//            super();
//            this.originalList = originalList;
//            this.adapter = adapter;
//            this.filteredList = new ArrayList<>();
//        }
//
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            filteredList.clear();
//            final FilterResults results = new FilterResults();
//            if (constraint.length() == 0) {
//                filteredList.addAll(originalList);
//                Log.i("FILTER", "F list size 0 length query  "+filteredList.size());
//            } else {
////                final String filterPattern = constraint.toString().toLowerCase().trim();
//
//
//
//                for (final Match match : originalList) {
//                    if (match.getTeam1().toLowerCase().contains(constraint.toString().toLowerCase())) {
//                        filteredList.add(match);
//                        continue;
//                    }
//
//                    if (match.getTeam2().toLowerCase().contains(constraint.toString().toLowerCase())) {
//                        filteredList.add(match);
//                        continue;
//                    }
//                }
//
//                Log.i("FILTER", "F list size after search "+filteredList.size());
//            }
//            results.values = filteredList;
//            results.count = filteredList.size();
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            Log.i("FILTER", "F list size 2  "+filteredList.size());
//            adapter.filteredList.clear();
//            adapter.filteredList.addAll((ArrayList<Match>) results.values);
////            adapter.mItems.clear();
////            adapter.mItems.addAll((ArrayList<Match>) results.values);
//            adapter.notifyDataSetChanged();
//        }
//    }

}

