package com.example.zak.testfragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.facebook.login.widget.ProfilePictureView.TAG;


public class SingleFavorisFragment extends Fragment {
    private DatabaseReference mDatabaseRef;
    private FirebaseDatabase database;
    private DatabaseReference mDtbRef;
    private DatabaseReference mDtbRefFav;

    private TextView beaconName;
    private TextView facebookText;
    private TextView twitterText;
    private TextView instagramText;
    private TextView snapchatText;
    private TextView linkedinText;
    private TextView googleText;

    private ImageView imageFavoris;
    private ProfilePictureView favorisImage;
    private boolean favoris;

    private String facebookID;
    private String twitterID;
    private String instagramID;
    private String facebookName;
    private RelativeLayout facebookLayout;
    private RelativeLayout twitterLayout;
    private RelativeLayout instagramLayout;
    private RelativeLayout snapchatLayout;
    private RelativeLayout linkedinLayout;
    private RelativeLayout googleLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_single_favoris, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Favoris");



        favoris = true;

        beaconName = (TextView) view.findViewById(R.id.favoris_name);

        facebookText = (TextView) view.findViewById(R.id.editFacebookFavorisView);
        twitterText = (TextView) view.findViewById(R.id.editTwitterFavorisView);
        instagramText = (TextView) view.findViewById(R.id.editInstagramFavorisView);
        snapchatText = (TextView) view.findViewById(R.id.editSnapchatFavorisView);
        linkedinText = (TextView) view.findViewById(R.id.editLinkedinFavorisView);
        googleText = (TextView) view.findViewById(R.id.editGoogleFavorisView);

        facebookLayout = (RelativeLayout) view.findViewById(R.id.facebookFavLayout);
        twitterLayout = (RelativeLayout) view.findViewById(R.id.twitterFavLayout);
        instagramLayout = (RelativeLayout) view.findViewById(R.id.instagramFavLayout);
        snapchatLayout = (RelativeLayout) view.findViewById(R.id.snapchatFavLayout);
        linkedinLayout = (RelativeLayout) view.findViewById(R.id.linkedinFavLayout);
        googleLayout = (RelativeLayout) view.findViewById(R.id.googleFavLayout);

        facebookLayout.setVisibility(View.GONE);
        twitterLayout.setVisibility(View.GONE);
        instagramLayout.setVisibility(View.GONE);
        snapchatLayout.setVisibility(View.GONE);
        linkedinLayout.setVisibility(View.GONE);
        googleLayout.setVisibility(View.GONE);

        imageFavoris = (ImageView) view.findViewById(R.id.favorisbeacon);

        favorisImage = (ProfilePictureView) view.findViewById(R.id.favorisPicture);

        facebookText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(facebookID!=null) {
                    //Toast.makeText(((MainActivity) getActivity()), "https://www.facebook.com/app_scoped_user_id/" + facebookID, Toast.LENGTH_LONG).show();
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/app_scoped_user_id/" + facebookID));
                    startActivity(browserIntent);
                }



                /*
                    try {
                        if(facebookID!=null) {

                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/" + facebookID));
                            startActivity(intent);
                        }
                    } catch (Exception e) {


                        if(facebookID!=null) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/app_scoped_user_id/" + facebookID));
                            startActivity(browserIntent);
                        }

                    }
                */

            }
        });
        twitterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(twitterID!=null) {

                    //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/"+twitterID));

                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("twitter://user?screen_name="+twitterID));
                        startActivity(intent);

                    }catch (Exception e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://twitter.com/"+twitterID)));
                    }

                }


            }
        });
        instagramText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(instagramID!=null) {

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/"+instagramID));
                    startActivity(browserIntent);




                }


            }
        });






        database = FirebaseDatabase.getInstance();
        mDatabaseRef = database.getReference("users");


        mDtbRefFav = mDatabaseRef.child(((MainActivity)getActivity()).getUser()).child("favoris");



        imageFavoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favoris){
                    imageFavoris.setImageDrawable(getResources().getDrawable(R.drawable.nonfavoris));
                    //enlever de bdd
                    mDatabaseRef.child(((MainActivity)getActivity()).getUser()).child("favoris").child(FavorisFragment.INSTANCE.getFavorisClicked()).removeValue();
                    favoris=false;
                }
                else{
                    imageFavoris.setImageDrawable(getResources().getDrawable(R.drawable.favoris));
                    //rajouter dans bdd
                    Map<String, String> newFav = new HashMap<String, String>();
                    String id = FavorisFragment.INSTANCE.getFavorisClicked();
                    newFav.put(id,id);
                    mDatabaseRef.child(((MainActivity)getActivity()).getUser()).child("favoris").child(id).setValue(id);
                    favoris=true;
                }



            }
        });




        //mDtbRef = mDatabaseRef.child(FavorisFragment.INSTANCE.getFavorisClicked());
        mDtbRef = mDatabaseRef.child(FavorisFragment.INSTANCE.getFavorisClicked());

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
                    Boolean show=false;
                    if((Boolean) dataSnapshot.child(key).child("show").getValue()!=null) {
                        show = (Boolean) dataSnapshot.child(key).child("show").getValue();
                    }


                    //Toast.makeText(getActivity(), "show: " + show, Toast.LENGTH_LONG).show();

                    switch (key)
                    {
                        case "facebookID":

                            favorisImage.setProfileId((String) dataSnapshot.child(key).getValue());
                            facebookID = (String) dataSnapshot.child(key).getValue();


                        case "name":
                            beaconName.setText((String) dataSnapshot.child(key).getValue());

                            break;
                        case "facebook":

                            if(show  && id != null){
                                facebookText.setText(id);
                                facebookName = id;
                                facebookLayout.setVisibility(View.VISIBLE);

                            }
                            else{


                            }
                            break;
                        case "twitter":

                            if(show && id != null){
                                twitterText.setText("@"+id);
                                twitterID = id;
                                twitterLayout.setVisibility(View.VISIBLE);
                            }
                            else{


                            }
                            break;
                        case "instagram":

                            if(show  && id != null){
                                instagramText.setText(id);
                                instagramID = id;
                                instagramLayout.setVisibility(View.VISIBLE);

                            }
                            else{


                            }
                            break;
                        case "snapchat":
                            snapchatText.setText(id);
                            if(show  && id != null){
                                snapchatText.setText(id);
                                snapchatLayout.setVisibility(View.VISIBLE);
                            }
                            else{


                            }
                            break;
                        case "linkedin":

                            if(show  && id != null){
                                linkedinText.setText(id);
                                linkedinLayout.setVisibility(View.VISIBLE);
                            }
                            else{


                            }
                            break;
                        case "google":

                            if(show  && id != null){
                                googleText.setText(id);
                                googleLayout.setVisibility(View.VISIBLE);
                            }
                            else{


                            }
                            break;
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
