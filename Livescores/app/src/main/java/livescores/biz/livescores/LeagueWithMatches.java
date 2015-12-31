package livescores.biz.livescores;

import java.util.ArrayList;

public class LeagueWithMatches {

    private String Name;
    private String id;
    private ArrayList<Match> matches;
    private boolean isLoading;
    private boolean loaded;


    public LeagueWithMatches(String name, String id, boolean isLoading, boolean loaded) {
        Name = name;
        this.id = id;
        this.isLoading = isLoading;
        this.loaded = loaded;
    }

    public String getName() {
        return Name;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Match> getMatches() {
        return matches;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMatches(ArrayList<Match> matches) {
        this.matches = matches;
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
}
