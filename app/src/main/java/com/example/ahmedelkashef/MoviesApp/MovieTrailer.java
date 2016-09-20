package com.example.ahmedelkashef.MoviesApp;

/**
 * Created by ahmedelkashef on 8/27/2016.
 */
public class MovieTrailer {
    private String Id;
    private String Key;
    private String Name;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSite() {
        return Site;
    }

    public void setSite(String site) {
        Site = site;
    }

    private String Site;
}
