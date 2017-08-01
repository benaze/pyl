package com.example.zak.testfragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zcw.togglebutton.ToggleButton;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import static com.facebook.login.widget.ProfilePictureView.TAG;


public class BeaconFragment extends Fragment {
    private DatabaseReference mDatabaseRef;
    private FirebaseDatabase database;
    private DatabaseReference mDtbRef;
    private DatabaseReference mDtbRefUser;
    private DatabaseReference mDtbRefFav;

    private TextView beaconName;
    private TextView facebookText;
    private TextView twitterText;
    private TextView instagramText;
    private TextView snapchatText;
    private TextView linkedinText;
    private TextView googleText;

    private ImageView imageFavoris;
    private boolean favoris;
    private ProfilePictureView beaconPhoto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_beacon, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Beacon");

        beaconName = (TextView) view.findViewById(R.id.beacon_name);

        facebookText = (TextView) view.findViewById(R.id.editFacebookBeaconView);
        twitterText = (TextView) view.findViewById(R.id.editTwitterBeaconView);
        instagramText = (TextView) view.findViewById(R.id.editInstagramBeaconView);
        snapchatText = (TextView) view.findViewById(R.id.editSnapchatBeaconView);
        linkedinText = (TextView) view.findViewById(R.id.editLinkedinBeaconView);
        googleText = (TextView) view.findViewById(R.id.editGoogleBeaconView);

        imageFavoris = (ImageView) view.findViewById(R.id.favorisbeacon);

        beaconPhoto = (ProfilePictureView) view.findViewById(R.id.beaconPhoto);

        database = FirebaseDatabase.getInstance();
        mDatabaseRef = database.getReference("users");


        mDtbRefFav = mDatabaseRef.child(((MainActivity)getActivity()).getUser()).child("favoris");

        /*
        mDtbRefFav.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                while (it.hasNext()) {
                    String key = it.next().getKey(); // chaque user (beacon id) 2f234454-cf6d-4a0f-adf2-f4911ba9ffa6
                    String fav = (String) dataSnapshot.child(key).getValue();

                    if(fav==PingFragment.INSTANCE.getBeaconClicked()){
                        favoris=true;
                        imageFavoris.setImageDrawable(getResources().getDrawable(R.drawable.favoris));
                    }

                }

            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }

        });
        */


        imageFavoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favoris){
                    imageFavoris.setImageDrawable(getResources().getDrawable(R.drawable.nonfavoris));
                    //enlever de bdd
                    mDatabaseRef.child(((MainActivity)getActivity()).getUser()).child("favoris").child(PingFragment.INSTANCE.getBeaconClicked()).removeValue();
                    favoris=false;
                }
                else{
                    imageFavoris.setImageDrawable(getResources().getDrawable(R.drawable.favoris));
                    //rajouter dans bdd
                    Map<String, String> newFav = new HashMap<String, String>();
                    String id = PingFragment.INSTANCE.getBeaconClicked();
                    newFav.put(id,id);
                    mDatabaseRef.child(((MainActivity)getActivity()).getUser()).child("favoris").child(id).setValue(id);
                    favoris=true;
                }



            }
        });


        mDtbRefUser = mDatabaseRef.child(((MainActivity)getActivity()).getUser()).child("favoris");

        mDtbRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                while (it.hasNext()) {
                    String key = it.next().getKey();
                    if(PingFragment.INSTANCE.getBeaconClicked().equals(key)){
                        imageFavoris.setImageDrawable(getResources().getDrawable(R.drawable.favoris));
                        break;

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDtbRef = mDatabaseRef.child(PingFragment.INSTANCE.getBeaconClicked());

        mDtbRef.addListenerForSingleValueEvent(new ValueEventListener(){
            //mDtbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again whenever data at this location is updated.

                //HashMap value = dataSnapshot.getValue(String.class);
                Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                while (it.hasNext()) {
                    String key = it.next().getKey();
                    //Toast.makeText(getActivity(), key, Toast.LENGTH_LONG).show();
                    String id = (String) dataSnapshot.child(key).child("id").getValue();
                    //Toast.makeText(getActivity(), "id: " + id, Toast.LENGTH_LONG).show();
                    Boolean show = (Boolean) dataSnapshot.child(key).child("show").getValue();
                    //Toast.makeText(getActivity(), "show: " + show, Toast.LENGTH_LONG).show();

                    switch (key) {
                        case "facebookID":

                            beaconPhoto.setProfileId((String) dataSnapshot.child(key).getValue());
                            break;

                        case "name":
                            beaconName.setText((String) dataSnapshot.child(key).getValue());

                            break;
                        case "facebook":

                            if(show){
                                facebookText.setText(id);
                            }
                            else{

                            }
                            break;
                        case "twitter":

                            if(show){
                                twitterText.setText(id);
                            }
                            else{

                            }
                            break;
                        case "instagram":

                            if(show){
                                instagramText.setText(id);
                            }
                            else{

                            }
                            break;
                        case "snapchat":
                            snapchatText.setText(id);
                            if(show){
                                snapchatText.setText(id);
                            }
                            else{

                            }
                            break;
                        case "linkedin":

                            if(show){
                                linkedinText.setText(id);
                            }
                            else{

                            }
                            break;
                        case "google":

                            if(show){
                                googleText.setText(id);
                            }
                            else{

                            }
                            break;
                        /*case "favoris":

                            Iterator<DataSnapshot> ito = dataSnapshot.child(key).getChildren().iterator();

                            String beacon = PingFragment.INSTANCE.getBeaconClicked();

                            while (ito.hasNext()) {

                                String nextKey = ito.next().getKey();
                                if(nextKey.equals(beacon)){
                                //if(nextKey==beacon){
                                    favoris = true;

                                    imageFavoris.setImageDrawable(getResources().getDrawable(R.drawable.favoris));

                                }
                            }
                            break;*/
                        default:
                            /*Action*/;
                    }


                }
                //Log.d(TAG, "Value is: " + value);
                //Toast.makeText(MainActivity.this, value, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }



}
