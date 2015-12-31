package livescores.biz.livescores;

import java.util.ArrayList;

public class MatchesHelper {
    private ArrayList<Match> matches;
    private int type;
    private String leagueId;
    private String name;
    private boolean isLoading;
    private  boolean loaded;

    public MatchesHelper(ArrayList<Match> matches, int type, String leagueId, String name, boolean isLoading, boolean loaded) {
        this.matches = matches;
        this.type = type;
        this.leagueId = leagueId;
        this.name = name;
        this.isLoading = isLoading;
        this.loaded = loaded;
    }

    public ArrayList<Match> search(String s){
        ArrayList<Match> ret = new ArrayList<>();

        for(int i = 0; i < matches.size(); i++){
            if(matches.get(i).getMinutes_text().contains(s)){
                ret.add(matches.get(i));
                continue;
            }
            if(matches.get(i).getScore2().contains(s)){
                ret.add(matches.get(i));
                continue;
            }
            if(matches.get(i).getScore1().contains(s)){
                ret.add(matches.get(i));
                continue;
            }
            if(matches.get(i).getTeam1().contains(s)){
                ret.add(matches.get(i));
                continue;
            }
            if(matches.get(i).getTeam2().contains(s)){
                ret.add(matches.get(i));
                continue;
            }
        }

        return ret;
    }

    public MatchesHelper(int type, String name, boolean isLoading, boolean loaded, String leagueId) {
        this.type = type;
        this.name = name;
        this.isLoading = isLoading;
        this.loaded = loaded;
        this.leagueId = leagueId;
    }

    public ArrayList<Match> getMatches() {
        return matches;
    }

    public int getType() {
        return type;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public String getName() {
        return name;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setMatches(ArrayList<Match> matches) {
        this.matches = matches;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
}
