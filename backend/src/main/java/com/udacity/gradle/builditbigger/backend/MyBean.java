package com.udacity.gradle.builditbigger.backend;

import finalproject.sidia.com.jokeslib.JokesLibMain;

public class MyBean {

    private String myData;

    public String getData() {

        this.myData = JokesLibMain.getInstance().getJokesRandom();

        return myData;
    }

}