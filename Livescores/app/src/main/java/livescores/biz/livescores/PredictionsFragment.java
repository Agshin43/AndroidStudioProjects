package livescores.biz.livescores;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by agshin on 12/22/15.
 */
public class PredictionsFragment extends DialogFragment {

    private Prediction prediction;
    public PredictionsFragment(Prediction prediction) {
        this.prediction = prediction;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.prediction, container, false);

//        Log.i("Fragment", "Percent draw = "+prediction.getDraw());
        v.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        int p1 = Integer.valueOf(prediction.getTeam1Percent());
        int p2 = Integer.valueOf(prediction.getDraw());
        int p3 = Integer.valueOf(prediction.getTeam2Percent());

        if(p1 > p2 && p1 > p3){
            ((TextView) v.findViewById(R.id.tvPercent1)).setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.m_match_lost));
            ((TextView) v.findViewById(R.id.tvPercent1)).setTextColor(ContextCompat.getColor(getActivity(), R.color.m_white));
        } else if(p2 > p1 && p2 > p3){
            ((TextView) v.findViewById(R.id.tvPercentDraw)).setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.m_match_lost));
            ((TextView) v.findViewById(R.id.tvPercentDraw)).setTextColor(ContextCompat.getColor(getActivity(), R.color.m_white));
        } else if(p3 > p1 && p3 > p2){
            ((TextView) v.findViewById(R.id.tvPercent2)).setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.m_match_lost));
            ((TextView) v.findViewById(R.id.tvPercent2)).setTextColor(ContextCompat.getColor(getActivity(),R.color.m_white));
        }


        ((TextView) v.findViewById(R.id.tvLeague)).setText(prediction.getLeague());
        ((TextView) v.findViewById(R.id.tvTeam1)).setText(prediction.getTeam1());
        ((TextView) v.findViewById(R.id.tvTeam2)).setText(prediction.getTeam2());
        ((TextView) v.findViewById(R.id.tvPercent1)).setText(prediction.getTeam1Percent()+" %");
        ((TextView) v.findViewById(R.id.tvPercent2)).setText(prediction.getTeam2Percent()+" %");
        ((TextView) v.findViewById(R.id.tvPercentDraw)).setText(prediction.getDraw()+" %");

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(getDialog() == null){
            return;
        }

        String ssh = "<font color='blue' size ='2'>" + getActivity().getResources().getString(R.string.ms_prediction) + "</font>";
        getDialog().setTitle(Html.fromHtml(ssh));
    }

}
