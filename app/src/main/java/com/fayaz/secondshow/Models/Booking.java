package com.fayaz.secondshow.Models;

public class Booking {

    String bookingId;
    String showId;
    String userId;
    String showName;
    String theaterId;
    String theaterName;
    String price;
    Integer no_of_seats;
    String date;
    String timing;


    public Booking(String bookingId, String showId, String userId, String showName, String theaterId, String theaterName, String price, Integer no_of_seats, String date, String timing) {
        this.bookingId = bookingId;
        this.showId = showId;
        this.userId = userId;
        this.showName = showName;
        this.theaterId = theaterId;
        this.theaterName = theaterName;
        this.price = price;
        this.no_of_seats = no_of_seats;
        this.date = date;
        this.timing = timing;
    }

    public Booking() {
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getNo_of_seats() {
        return no_of_seats;
    }

    public void setNo_of_seats(Integer no_of_seats) {
        this.no_of_seats = no_of_seats;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
