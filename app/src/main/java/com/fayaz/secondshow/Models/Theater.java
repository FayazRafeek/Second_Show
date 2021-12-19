package com.fayaz.secondshow.Models;

public class Theater {

    String theaterId;
    String theaterName;
    String location;
    String email;
    String password;

    public Theater(String theaterName, String location, String email, String password) {
        this.theaterName = theaterName;
        this.location = location;
        this.email = email;
        this.password = password;
    }

    public Theater() {
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
