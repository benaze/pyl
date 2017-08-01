package com.example.zak.testfragments;

import android.content.Context;
import android.media.Image;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.zak.testfragments.MainActivity.INSTANCE;

/**
 * Created by Zak on 18/01/2017.
 */

public class CustomPingAdapter extends ArrayAdapter<DataPingModel> implements View.OnClickListener{

    private ArrayList<DataPingModel> dataSet;
    Context mContext;

    //ViewHolder viewHolder;

    // View lookup cache

   /* private static class ViewHolder {
        TextView txtName;
        ProfilePictureView photo;
        ImageView favorisStar;
        TextView distanceChange;
    }
    */



    public CustomPingAdapter(ArrayList<DataPingModel> data, Context context) {
        super(context, R.layout.affichageitem, data);
        this.dataSet = data;
        this.mContext=context;

        //viewHolder = new ViewHolder();

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataPingModel dataModel=(DataPingModel)object;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = database.getReference("users");


        switch (v.getId())
        {

            case R.id.favorisStar:

               /*
               FirebaseDatabase database = FirebaseDatabase.getInstance();
               DatabaseReference mDatabaseRef = database.getReference("users");
                mDatabaseRef.child(((MainActivity)INSTANCE).getUser()).child("favoris").child(dataModel.getId()).removeValue();
                ((MainActivity)INSTANCE).launchFragment(new SingleFavorisFragment());
                */
                ImageView imageview= (ImageView)v;

                if(dataModel.getFavoris()){
                    Snackbar.make(v, "Favoris supprimé: " +dataModel.getName(), Snackbar.LENGTH_LONG)
                            .setAction("No action", null).show();
                    imageview.setImageResource(R.drawable.nonfavoris);
                    //viewHolder.favorisStar.setImageDrawable(v.getResources().getDrawable(R.drawable.nonfavoris));
                    //enlever de bdd
                    mDatabaseRef.child(((MainActivity)INSTANCE).getUser()).child("favoris").child(dataModel.getId()).removeValue();
                    dataModel.setFavoris(false);
                }
                else{
                    Snackbar.make(v, "Favoris ajouté : " +dataModel.getName(), Snackbar.LENGTH_LONG)
                            .setAction("No action", null).show();
                    imageview.setImageResource(R.drawable.favoris);
                    //viewHolder.favorisStar.setImageDrawable(v.getResources().getDrawable(R.drawable.favoris));

                    //rajouter dans bdd
                    Map<String, String> newFav = new HashMap<String, String>();
                    String id = dataModel.getId();
                    newFav.put(id,id);
                    mDatabaseRef.child(((MainActivity)INSTANCE).getUser()).child("favoris").child(id).setValue(id);


                    dataModel.setFavoris(true);
                }
                break;

        }

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataPingModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        // view lookup cache stored in tag

        final View result;

        ViewHolder viewHolder = new ViewHolder();





        if (convertView == null) {


            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.affichageitem, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.idChange);
            viewHolder.distanceChange = (TextView) convertView.findViewById(R.id.distanceChange);
            viewHolder.photo = (ProfilePictureView) convertView.findViewById(R.id.photoBeacon);
            viewHolder.favorisStar = (ImageView) convertView.findViewById(R.id.favorisStar);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);



            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        //Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        //result.startAnimation(animation);
        lastPosition = position;

        if (dataModel.getName() != null) {
            viewHolder.txtName.setText(dataModel.getName());
        }
        if (dataModel.getPhoto() != null){

            viewHolder.photo.setProfileId(dataModel.getPhoto());
        }
        if (dataModel.getDistance() != null){

            viewHolder.distanceChange.setText(dataModel.getDistance());
        }
        if(dataModel.getFavoris()){
            viewHolder.favorisStar.setImageResource(R.drawable.favoris);
        }
        if (dataModel.getDate() != null){

            viewHolder.date.setText(dataModel.getDate());
        }






            viewHolder.favorisStar.setOnClickListener(this);
        viewHolder.favorisStar.setTag(position);


        // Return the completed view to render on screen
        return convertView;
    }
}