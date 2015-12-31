package livescores.biz.livescores;

/**
 * Created by agshin on 12/20/15.
 */
public class MatchH2H {
    private String matchId;
    private String score1;
    private String score2;
    private String result;
    private String date;
    private String team1;
    private String team2;
    private String league;

    public MatchH2H(String matchId, String score1, String score2, String result, String date, String team1, String team2, String league) {
        this.matchId = matchId;
        this.score1 = score1;
        this.score2 = score2;
        this.result = result;
        this.date = date;
        this.team1 = team1;
        this.team2 = team2;
        this.league = league;
    }

    public String getMatchId() {
        return matchId;
    }

    public String getScore1() {
        return score1;
    }

    public String getScore2() {
        return score2;
    }

    public String getResult() {
        return result;
    }

    public String getDate() {
        return date;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public String getLeague() {
        return league;
    }
}
