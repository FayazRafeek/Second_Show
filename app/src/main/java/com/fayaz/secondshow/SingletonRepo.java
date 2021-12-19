package com.fayaz.secondshow;

import com.fayaz.secondshow.Models.Show;

public class SingletonRepo {

    public static SingletonRepo INSTANCE = new SingletonRepo();

    public static SingletonRepo getInstance() {
        if(INSTANCE == null){
            return new SingletonRepo();
        } else return INSTANCE;
    }

//    Global vars

    public Show showInfo;

    public Show getShowInfo() {
        return showInfo;
    }

    public void setShowInfo(Show showInfo) {
        this.showInfo = showInfo;
    }

    public Show editShow;

    public Show getEditShow() {
        return editShow;
    }

    public void setEditShow(Show editShow) {
        this.editShow = editShow;
    }

    public Show bookShow;

    public Show getBookShow() {
        return bookShow;
    }

    public void setBookShow(Show bookShow) {
        this.bookShow = bookShow;
    }
}
