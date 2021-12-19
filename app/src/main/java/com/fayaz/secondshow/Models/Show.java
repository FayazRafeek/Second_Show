package com.fayaz.secondshow.Models;

import java.util.ArrayList;

public class Show {

    String theaterId;
    String theaterName;
    String showId;
    String showName;
    String rating;
    String price;
    String posterUrl;
    String desc;


    ArrayList<String> shows;

    public Show( String showId,String theaterId, String showName, String rating, String price, ArrayList<String> shows, String theaterName) {
        this.theaterId = theaterId;
        this.showId = showId;
        this.showName = showName;
        this.rating = rating;
        this.price = price;
        this.shows = shows;
        this.theaterName = theaterName;
    }

    public Show() {

    }

    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public ArrayList<String> getShows() {
        return shows;
    }

    public void setShows(ArrayList<String> shows) {
        this.shows = shows;
    }

    public String getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(String theaterId) {
        this.theaterId = theaterId;
    }

    public String getTheaterName() {
        return theaterName;
    }

    public void setTheaterName(String theaterName) {
        this.theaterName = theaterName;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
