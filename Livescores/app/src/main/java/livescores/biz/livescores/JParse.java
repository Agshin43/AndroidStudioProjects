package livescores.biz.livescores;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.AEADBadTagException;

public class JParse {
    public JParse() {
    }


    private String getDate(long milliSeconds)
    {
        SimpleDateFormat sf = new SimpleDateFormat("dd MMM yyyy");
        return sf.format(new Date(milliSeconds * 1000));
    }


    public ArrayList<Match> getMatches(String in) throws JSONException {
        ArrayList<Match> matches = new ArrayList<>();

        in = "{Match:"+in+"}";
        try {
            JSONObject  jsonRootObject = new JSONObject(in);
            JSONArray jsonArray = jsonRootObject.optJSONArray("Match");

            for(int i=0; i < jsonArray.length(); i++){
                Match mt = new Match();
                JSONObject jsonObject = jsonArray.getJSONObject(i);


                mt.setId(jsonObject.optString("id").toString());
                mt.setTeam1(jsonObject.optString("team1").toString());
                mt.setTeam2(jsonObject.optString("team2").toString());
                mt.setPart(jsonObject.optString("part").toString());
                mt.setMinutes_text(jsonObject.optString("minutes_text").toString());
                mt.setTime(jsonObject.optString("time").toString());
                mt.setScore1(jsonObject.optString("score1").toString());
                mt.setScore2(jsonObject.optString("score2").toString());
                mt.setHalf_score1(jsonObject.optString("half_score1").toString());
                mt.setHalf_score2(jsonObject.optString("half_score2").toString());
                mt.setRed1(jsonObject.optString("red1").toString());
                mt.setRed2(jsonObject.optString("red2").toString());
                mt.setYellow1(jsonObject.optString("yellow1").toString());
                mt.setYellow2(jsonObject.optString("yellow2").toString());



                matches.add(mt);
            }
            if(matches.size() > 0){
                Match mt = matches.get(matches.size() - 1);
                Log.i("MATCHES",mt.getTeam1()+" "+mt.getScore1()+" - "+mt.getScore2()+" "+mt.getTeam2());
            }
        } catch (JSONException e) {e.printStackTrace();}
        return matches;
    }

    public ArrayList<TableTeam> generateTeams(String input){
        ArrayList<TableTeam> ret = new ArrayList<>();

        try{
            JSONArray jArray = new JSONArray(input);
            for(int i = 0; i < jArray.length(); i++){
                TableTeam tm = new TableTeam();

                JSONObject obj = jArray.getJSONObject(i);

                tm.setDraw(obj.optString("draw"));
                tm.setLeague(obj.optString("league"));
                tm.setGames(obj.optString("games"));
                tm.setLost(obj.optString("lost"));
                tm.setWin(obj.optString("win"));
                tm.setPoints(obj.optString("points"));
                tm.setTeam(obj.optString("team"));

                ret.add(tm);
            }

        } catch (JSONException e) {e.printStackTrace();}

        return ret;

    }

    public ArrayList<ArrayList<Match>> getSeparatedMatches(String in) throws JSONException {


        ArrayList<Match> liveMatches = new ArrayList<>();
        ArrayList<Match> todayMatches = new ArrayList<>();
        ArrayList<Match> yesterdayMatches = new ArrayList<>();
        ArrayList<Match> notStartedMatches = new ArrayList<>();
        ArrayList<Match> finishedLiveMatches = new ArrayList<>();
        ArrayList<Match> tomorrowMatches = new ArrayList<>();

        ArrayList<ArrayList<Match>> ret = new ArrayList<>();

        in = "{Match:"+in+"}";
        try {
            JSONObject  jsonRootObject = new JSONObject(in);

            JSONArray jsonArray = jsonRootObject.optJSONArray("Match");

            long yesterdayFinish = customTime(-1, 23, 59, 59);
            long yesterdayStart = customTime(-1, 0, 0, 0);
            long todayFinish = customTime(0, 23, 59, 59);
            long tomorrow01_00 = customTime(1, 1, 0, 0);
            long tomorrow04_59_59 = customTime(1, 4, 59, 59);
            long tomorrowFinish = customTime(1, 23, 59, 59);
            long yesterday22_00 = customTime(-1,22,0,0);


            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy  kk:mm:ss");

            Log.i("yesterdayFinish",dateFormat.format(yesterdayFinish));
            Log.i("yesterdayStart",dateFormat.format(yesterdayStart));
            Log.i("todayFinish",dateFormat.format(todayFinish));
            Log.i("Tomorrow 1 00",dateFormat.format(tomorrow01_00));
            Log.i("tomorrow04_59_59",dateFormat.format(tomorrow04_59_59));
            Log.i("Yesterday 22 00",dateFormat.format(yesterday22_00));



            for(int i=0; i < jsonArray.length(); i++){
                Match mt = new Match();

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                mt.setId(jsonObject.optString("id").toString());
                mt.setTeam1(jsonObject.optString("team1").toString());
                mt.setTeam2(jsonObject.optString("team2").toString());
                mt.setPart(jsonObject.optString("part").toString());
                mt.setMinutes_text(jsonObject.optString("minutes_text").toString());
                mt.setTime(jsonObject.optString("time").toString());
                mt.setScore1(jsonObject.optString("score1").toString());
                mt.setScore2(jsonObject.optString("score2").toString());
                mt.setHalf_score1(jsonObject.optString("half_score1").toString());
                mt.setHalf_score2(jsonObject.optString("half_score2").toString());
                mt.setRed1(jsonObject.optString("red1").toString());
                mt.setRed2(jsonObject.optString("red2").toString());
                mt.setYellow1(jsonObject.optString("yellow1").toString());
                mt.setYellow2(jsonObject.optString("yellow2").toString());
                mt.setUpdateTime(jsonObject.optString("update_time"));

//                long curTime = customTime(0,)

                long cur = System.currentTimeMillis();
                long update = ((Long.valueOf(mt.getUpdateTime()) + 360) * 1000);
//                Log.i("XXX pp",cur + "");
//                Log.i("XXX pp",update + "");
                boolean updated = ( update > cur);

                long lt = Long.valueOf(mt.getTime()) * 1000;

                int part = Integer.valueOf(mt.getPart());


                if((part == 1) || (part == 2) || (part == 3) && updated){
                    liveMatches.add(mt);
//                    continue;
                }

                if(lt > yesterday22_00 && lt < tomorrow01_00)
                {
                    todayMatches.add(mt);
//                    continue;
                }

                if((part == 4 || part == 5) && lt > yesterdayStart && lt <= yesterdayFinish)
                {
                    yesterdayMatches.add(mt);
//                    continue;
                }

                if(lt < tomorrow04_59_59 && part == 0 && updated )
                {
                    notStartedMatches.add(mt);
//                    continue;
                }

                if(lt < todayFinish && lt > yesterday22_00 && (part == 4 || part == 5) && updated)
                {
                    finishedLiveMatches.add(mt);
//                    continue;
                }

                Log.i("++++ >>>> ", todayFinish + " * "+lt +" * "+ tomorrowFinish);
                if(lt > todayFinish && lt < tomorrowFinish)
                {
                    tomorrowMatches.add(mt);
//                    continue;
                }



//                TOMORROW:
//                $start_time = mktime(23, 59, 59, date('m'), date('d'), date('Y'));
//                $end_time = mktime(23, 59, 59, date('m'), date('d')+1, date('Y'));
//                $mysql_where = " `time`> ".$start_time." AND `time`<".$end_time." ";





            }
//            if(finishedLiveMatches.size() > 0){
//                Match mt = finishedLiveMatches.get(finishedLiveMatches.size() - 1);
//                Log.i("ALL MATCHES last", mt.getTeam1() + " " + mt.getScore1() + " - " + mt.getScore2() + " " + mt.getTeam2());
//            }


        } catch (JSONException e) {e.printStackTrace();}

        ret.add(liveMatches);
        ret.add(yesterdayMatches);
        ret.add(todayMatches);
        ret.add(notStartedMatches);
        ret.add(finishedLiveMatches);
        ret.add(tomorrowMatches);

        return ret;
    }

    public ArrayList<League> generateLeagueList(String input){
        ArrayList<League> ret = new ArrayList<>();
//        input = "Matches "+input+"";

        try {
            JSONObject  jsonRootObject = new JSONObject(input);

            int i = 1;

            while (jsonRootObject.has(String.valueOf(i))){
                JSONObject obj = jsonRootObject.getJSONObject(String.valueOf(i));
                i++;

                ret.add(new League(obj.optString("name"), obj.optString("id")));
            }

        } catch (JSONException e) {e.printStackTrace();}

        return ret;

    }

    public ArrayList<MatchH2H> H2HMatches(String input){
        ArrayList<MatchH2H> ret = new ArrayList<>();
        try {
            JSONObject  jsonRootObject = new JSONObject(input);

            int i = 1;

            while (jsonRootObject.has(String.valueOf(i))){
                JSONObject obj = jsonRootObject.getJSONObject(String.valueOf(i));
                i++;
                ret.add(new MatchH2H("999",
                        obj.optString("score1"),
                        obj.optString("score2"),
                        obj.optString("result"),
                        obj.optString("time"),
                        obj.optString("team1"),
                        obj.optString("team2"),
                        obj.optString("league")));
                Log.i("------ H2H ------", obj.optString("team1") + " " + obj.optString("result") + " " + obj.optString("team2"));
            }

        } catch (JSONException e) {e.printStackTrace();}

        return ret;
    }

    public MatchDetails generateMatchDetails(String input, Match match){
        MatchDetails d = new MatchDetails();


        try{
            JSONArray jArray = new JSONArray(input);

            d.setTeam1(match.getTeam1());
            d.setTeam2(match.getTeam2());
            d.setScore1(match.getScore1());
            d.setScore2(match.getScore2());
            d.setDate(getDate(Long.valueOf(match.getTime())));
            d.setMinutes(match.getMinutes_text());
            ArrayList<MatchDetails.Action> actions = new ArrayList<>();

            for(int i = 0; i < jArray.length(); i++){
                JSONObject obj = jArray.getJSONObject(i);

                actions.add(d.new Action(obj.optString("action"), obj.optString("value1"), obj.optString("value2")));

                Log.i("details", obj.optString("action") + " - " + obj.optString("value1") + " - " + obj.optString("value2"));
            }

            d.setActions(actions);

        } catch (JSONException e) {e.printStackTrace();}
        return d;
    }

    public Prediction generatePredictions(String input){
        Prediction ret = new Prediction();
        Log.i("prediction",input);
        try {
            JSONObject  obj = new JSONObject(input);
            ret.setDraw(obj.optString("draw"));
            ret.setTeam1(obj.optString("team1"));
            ret.setTeam2(obj.optString("team2"));
            ret.setTeam1Percent(obj.optString("team1_percent"));
            ret.setTeam2Percent(obj.optString("team2_percent"));
            ret.setLeague(obj.optString("league"));

        } catch (JSONException e) {e.printStackTrace();}

        return ret;
    }

//    private String getDay(int d){
//        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DATE, d);
//        return dateFormat.format(cal.getTime());
//    }

    private long dayRange(int dayBefore, int dayAfter, int h1, int m1, int s1, int h2, int m2, int s2){
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy  hh:mm:ss");
        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.DAY_OF_MONTH,-dayBefore);
        cal1.set(Calendar.HOUR_OF_DAY, h1);
        cal1.set(Calendar.MINUTE, m1);
        cal1.set(Calendar.SECOND, s1);

        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.DAY_OF_MONTH,dayAfter);
        cal2.set(Calendar.HOUR_OF_DAY, h2);
        cal2.set(Calendar.MINUTE, m2);
        cal2.set(Calendar.SECOND, s2);

        Log.i("Before", dateFormat.format(cal1.getTime()));
        Log.i("After", dateFormat.format(cal2.getTime()));

        return cal2.getTimeInMillis() - cal1.getTimeInMillis();
    }

    private long customTime(int dayDiff, int h, int m, int s){
        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.DAY_OF_MONTH, dayDiff);

        cal1.add(Calendar.HOUR_OF_DAY, -cal1.get(Calendar.HOUR_OF_DAY)+h);
        cal1.add(Calendar.MINUTE, -cal1.get(Calendar.MINUTE)+m);
        cal1.add(Calendar.SECOND, -cal1.get(Calendar.SECOND)+s);
//        cal1.set(Calendar.HOUR_OF_DAY, h1);
//        cal1.set(Calendar.MINUTE, m1);
//        cal1.set(Calendar.SECOND, s1);

        return cal1.getTimeInMillis();
    }



}
