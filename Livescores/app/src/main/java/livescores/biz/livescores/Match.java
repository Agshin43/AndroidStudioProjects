package livescores.biz.livescores;

import android.util.LruCache;

public class Match {
    private String id;
    private String team1;
    private String team2;
    private String part;
    private String minutes_text;
    private String time;
    private String score1; 
    private String score2;
    private String half_score1;
    private String half_score2;
    private String red1;
    private String red2;
    private String yellow1;
    private String yellow2;
    private String updateTime;

    private boolean found;

    public Match() {
    }

    public Match(String team1, String team2, String part, String minutes_text, String time, String score1, String score2, String half_score1, String half_score2, String red1, String red2, String yellow1, String yellow2, String updateTime) {
        this.team1 = team1;
        this.team2 = team2;
        this.part = part;
        this.minutes_text = minutes_text;
        this.time = time;
        this.score1 = score1;
        this.score2 = score2;
        this.half_score1 = half_score1;
        this.half_score2 = half_score2;
        this.red1 = red1;
        this.red2 = red2;
        this.yellow1 = yellow1;
        this.yellow2 = yellow2;
        this.updateTime = updateTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public String getId() {
        return id;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public String getPart() {
        return part;
    }

    public String getMinutes_text() {
        return minutes_text;
    }

    public String getTime() {
        return time;
    }

    public String getScore1() {
        return score1;
    }

    public String getScore2() {
        return score2;
    }

    public String getHalf_score1() {
        return half_score1;
    }

    public String getHalf_score2() {
        return half_score2;
    }

    public String getRed1() {
        return red1;
    }

    public String getRed2() {
        return red2;
    }

    public String getYellow1() {
        return yellow1;
    }

    public String getYellow2() {
        return yellow2;
    }


    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public void setMinutes_text(String minutes_text) {
        this.minutes_text = minutes_text;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setScore1(String score1) {
        this.score1 = score1;
    }

    public void setScore2(String score2) {
        this.score2 = score2;
    }

    public void setHalf_score1(String half_score1) {
        this.half_score1 = half_score1;
    }

    public void setHalf_score2(String half_score2) {
        this.half_score2 = half_score2;
    }

    public void setRed1(String red1) {
        this.red1 = red1;
    }

    public void setRed2(String red2) {
        this.red2 = red2;
    }

    public void setYellow1(String yellow1) {
        this.yellow1 = yellow1;
    }

    public void setYellow2(String yellow2) {
        this.yellow2 = yellow2;
    }

    public void setId(String id) {
        this.id = id;
    }
}
