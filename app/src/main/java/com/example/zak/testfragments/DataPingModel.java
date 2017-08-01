package com.example.zak.testfragments;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import java.util.HashMap;

/**
 * Created by anupamchugh on 09/02/16.
 */
public class DataPingModel {

    private HashMap map;
    private String name;
    private String image;
    private String id;
    private String distance;
    private String date;
    private boolean favoris;



    public DataPingModel(String name, String image, String id, String distance, HashMap map, boolean favoris, String date) {
        this.name=name;

        this.image= image;
        this.id=id;
        this.map=map;
        this.distance = distance;
        this.favoris=favoris;
        this.date=date;


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

    public String getDistance() { return distance;}


    public void setMap(HashMap<String,String> map) {
        this.map = map;
    }

    public boolean getFavoris() {
        return favoris;
    }

    public void setFavoris(boolean favoris) {
        this.favoris = favoris;
    }

    public String getDate() {
        return date;
    }
}
