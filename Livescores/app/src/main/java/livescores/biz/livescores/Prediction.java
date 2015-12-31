package livescores.biz.livescores;

public class Prediction{
    private String team1;
    private String team2;
    private String team1Percent;
    private String team2Percent;
    private String draw;
    private String league;

    public Prediction() {
    }

    public Prediction(String team1, String team2, String team1Percent, String team2Percent, String draw, String league) {
        this.team1 = team1;
        this.team2 = team2;
        this.team1Percent = team1Percent;
        this.team2Percent = team2Percent;
        this.draw = draw;
        this.league = league;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public String getTeam1Percent() {
        return team1Percent;
    }

    public String getTeam2Percent() {
        return team2Percent;
    }

    public String getDraw() {
        return draw;
    }

    public String getLeague() {
        return league;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public void setTeam1Percent(String team1Percent) {
        this.team1Percent = team1Percent;
    }

    public void setTeam2Percent(String team2Percent) {
        this.team2Percent = team2Percent;
    }

    public void setDraw(String draw) {
        this.draw = draw;
    }

    public void setLeague(String league) {
        this.league = league;
    }
}
