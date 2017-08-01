package com.example.zak.testfragments;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.util.HashMap;

/**
 * Created by anupamchugh on 09/02/16.
 */
public class DataModel {

    private HashMap map;
    private String name;
    private String image;
    private String id;
    private Boolean favoris=true;


    public DataModel(String name, String image, String id, HashMap map) {
        this.name=name;

        this.image= image;
        this.id=id;
        this.map=map;

    }


    public String getName() {
        return name;
    }

    public String getPhoto() {
        return image;
    }

    public HashMap getMap() {
        return map;
    }

    public String getId() {
        return id;
    }


    public void setMap(HashMap<String,String> map) {
        this.map = map;
    }

    public Boolean getFavoris() {
        return favoris;
    }

    public void setFavoris(boolean favoris) {
        this.favoris = favoris;
    }
}
