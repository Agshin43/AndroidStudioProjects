package livescores.biz.livescores;

public class TableTeam {
    private String league;
    private String team;
    private String games;
    private String win;
    private String draw;
    private String lost;
    private String points;

    public TableTeam(String league, String team, String games, String win, String draw, String lost, String points) {
        this.league = league;
        this.team = team;
        this.games = games;
        this.win = win;
        this.draw = draw;
        this.lost = lost;
        this.points = points;
    }

    public TableTeam() {
    }

    public String getLeague() {
        return league;
    }

    public String getTeam() {
        return team;
    }

    public String getGames() {
        return games;
    }

    public String getWin() {
        return win;
    }

    public String getDraw() {
        return draw;
    }

    public String getLost() {
        return lost;
    }

    public String getPoints() {
        return points;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void setGames(String games) {
        this.games = games;
    }

    public void setWin(String win) {
        this.win = win;
    }

    public void setDraw(String draw) {
        this.draw = draw;
    }

    public void setLost(String lost) {
        this.lost = lost;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
