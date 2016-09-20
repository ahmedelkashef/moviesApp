package com.example.ahmedelkashef.MoviesApp;

import java.io.Serializable;

/**
 * Created by ahmedelkashef on 8/15/2016.
 */
public class MovieContext implements Serializable {

    private String Title;
    private String ImageURL;
    private int ID;
    private String Overview;
    private String ReleaseDate;
    private String Rate;
    private String Ismoviefavaourite;

    public MovieContext() {
    }

    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }

    public String getIsmoviefavaourite() {
        return Ismoviefavaourite;
    }

    public void setIsmoviefavaourite(String ismoviefavaourite) {
        Ismoviefavaourite = ismoviefavaourite;
    }

    public String getReleaseDate() {
        return ReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        ReleaseDate = releaseDate;
    }



    public String getOverview() {
        return Overview;
    }

    public void setOverview(String overview) {
        Overview = overview;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
