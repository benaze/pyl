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

public class CustomAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener{

    private ArrayList<DataModel> dataSet;
    Context mContext;

    public int getImage(Object image) {
        if(image=="linkedin"){
            return R.drawable.linkedin;
        }
        else if(image=="google"){
            return R.drawable.google;
        }
        else if(image=="snapchat"){
            return R.drawable.snapchat;
        }
        else if(image=="facebook"){
            return R.drawable.facebook;
        }
        else if(image=="twitter"){
            return R.drawable.twitter;
        }
        else if(image=="instagram"){
            return R.drawable.instagram;
        }

        return 0;

    }

    /*
    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        ProfilePictureView photo;
        ImageView image0;
        ImageView image1;
        ImageView image2;
        ImageView image3;
        ImageView image4;
        ImageView image5;
        ImageView favorisStar;
    }
    */

    public CustomAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.affichageitemfavoris, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        DataModel dataModel=(DataModel)object;
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
                            //favoris=false;
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

                            //favoris=true;
                            dataModel.setFavoris(true);
                        }
                break;

        }

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
         // view lookup cache stored in tag

        final View result;

        ViewHolder viewHolder = new ViewHolder();

        if (convertView == null) {


            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.affichageitemfavoris, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.idChange);
            viewHolder.photo = (ProfilePictureView) convertView.findViewById(R.id.favorisPhoto);
            viewHolder.favorisStar = (ImageView) convertView.findViewById(R.id.favorisStar);

            /*viewHolder.image0 = (ImageView) convertView.findViewById(R.id.img0);
            viewHolder.image1 = (ImageView) convertView.findViewById(R.id.img1);
            viewHolder.image2 = (ImageView) convertView.findViewById(R.id.img2);
            viewHolder.image3 = (ImageView) convertView.findViewById(R.id.img3);
            viewHolder.image4 = (ImageView) convertView.findViewById(R.id.img4);
            viewHolder.image5 = (ImageView) convertView.findViewById(R.id.img5);
            */


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


        /*
        if (dataSet.get(position).getMap().get("image0") != null){

            int image = getImage(dataSet.get(position).getMap().get("image0"));
            //viewHolder.image0.setImageResource((int)dataModel.getMap().get("image0"));
            viewHolder.image0.setImageResource(image);
        }
        if (dataSet.get(position).getMap().get("image1") != null){

            //viewHolder.image1.setImageResource((int)dataModel.getMap().get("image1"));

            int image = getImage(dataSet.get(position).getMap().get("image1"));

            viewHolder.image1.setImageResource(image);
        }
        if (dataSet.get(position).getMap().get("image2") != null){

            //viewHolder.image2.setImageResource((int)dataModel.getMap().get("image2"));

            int image = getImage(dataSet.get(position).getMap().get("image2"));

            viewHolder.image2.setImageResource(image);
        }
        if (dataSet.get(position).getMap().get("image3") != null){

            //viewHolder.image3.setImageResource((int)dataModel.getMap().get("image3"));

            int image = getImage(dataSet.get(position).getMap().get("image3"));

            viewHolder.image3.setImageResource(image);
        }
        if (dataSet.get(position).getMap().get("image4") != null){

            //viewHolder.image4.setImageResource((int)dataModel.getMap().get("image4"));

            int image = getImage(dataSet.get(position).getMap().get("image4"));

            viewHolder.image4.setImageResource(image);
        }
        if (dataSet.get(position).getMap().get("image5") != null){

            //viewHolder.image5.setImageResource((int)dataModel.getMap().get("image5"));

            int image = getImage(dataSet.get(position).getMap().get("image5"));

            viewHolder.image5.setImageResource(image);
        }
        */




        viewHolder.favorisStar.setOnClickListener(this);
        viewHolder.favorisStar.setTag(position);


        // Return the completed view to render on screen
        return convertView;
    }
}