package livescores.biz.livescores;

import java.util.ArrayList;

/**
 * Created by agshin on 12/21/15.
 */
public class MatchDetails {
    private String team1;
    private String team2;
    private String score1;
    private String score2;
    private String minutes;
    private String date;

    private ArrayList<Action> actions;


    public MatchDetails() {
    }

    public MatchDetails(String team1, String team2, String score1, String score2, String minutes, String date, ArrayList<Action> actions) {
        this.team1 = team1;
        this.team2 = team2;
        this.score1 = score1;
        this.score2 = score2;
        this.minutes = minutes;
        this.date = date;
        this.actions = actions;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public String getScore1() {
        return score1;
    }

    public String getScore2() {
        return score2;
    }

    public String getMinutes() {
        return minutes;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public void setScore1(String score1) {
        this.score1 = score1;
    }

    public void setScore2(String score2) {
        this.score2 = score2;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }

    public class Action{
        public String name;
        public String value1;
        public String value2;



        public Action(String name, String value1, String value2) {
            this.name = name;
            this.value1 = value1;
            this.value2 = value2;
        }

        public String getName() {
            return name;
        }

        public String getValue1() {
            return value1;
        }

        public String getValue2() {
            return value2;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setValue1(String value1) {
            this.value1 = value1;
        }

        public void setValue2(String value2) {
            this.value2 = value2;
        }
    }
}
