package com.example.zak.testfragments;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.zcw.togglebutton.ToggleButton;

import java.util.Iterator;

import static com.facebook.login.widget.ProfilePictureView.TAG;


public class ProfilFragment extends Fragment {

    private DatabaseReference mDatabaseRef;
    private FirebaseDatabase database;
    private DatabaseReference mDtbRef;


    private TextView profilName;
    private TextView facebookText;
    private TextView twitterText;
    private TextView instagramText;
    private TextView snapchatText;
    private TextView linkedinText;
    private TextView googleText;
    private ToggleButton toggleBtnFacebook;
    private ToggleButton toggleBtnTwitter;
    private ToggleButton toggleBtnInstagram;
    private ToggleButton toggleBtnSnapchat;
    private ToggleButton toggleBtnLinkedin;
    private ToggleButton toggleBtnGoogle;
    private ImageView modifFacebook;
    private ImageView modifTwitter;
    private ImageView modifInstagram;
    private ImageView modifSnapchat;
    private ImageView modifLinkedin;
    private ImageView modifGoogle;
    private ProfilePictureView profilPhoto;
    public static ProfilFragment INSTANCE;
    private String networkClicked;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_profil, container, false);
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Profil");

        INSTANCE=this;

        profilName = (TextView) view.findViewById(R.id.profil_name);
        profilPhoto = (ProfilePictureView) view.findViewById(R.id.profilPhoto);

        facebookText = (TextView) view.findViewById(R.id.editFacebookProfilView);
        twitterText = (TextView) view.findViewById(R.id.editTwitterProfilView);
        instagramText = (TextView) view.findViewById(R.id.editInstagramProfilView);
        snapchatText = (TextView) view.findViewById(R.id.editSnapchatProfilView);
        linkedinText = (TextView) view.findViewById(R.id.editLinkedinProfilView);
        googleText = (TextView) view.findViewById(R.id.editGoogleProfilView);

        toggleBtnFacebook = (ToggleButton) view.findViewById(R.id.toggle_facebook);
        toggleBtnTwitter = (ToggleButton) view.findViewById(R.id.toggle_twitter);
        toggleBtnInstagram = (ToggleButton) view.findViewById(R.id.toggle_instagram);
        toggleBtnSnapchat = (ToggleButton) view.findViewById(R.id.toggle_snapchat);
        toggleBtnLinkedin = (ToggleButton) view.findViewById(R.id.toggle_linkedin);
        toggleBtnGoogle = (ToggleButton) view.findViewById(R.id.toggle_google);

        modifFacebook = (ImageView) view.findViewById(R.id.modif_facebook);
        modifTwitter = (ImageView) view.findViewById(R.id.modif_twitter);
        modifInstagram = (ImageView) view.findViewById(R.id.modif_instagram);
        modifSnapchat = (ImageView) view.findViewById(R.id.modif_snapchat);
        modifLinkedin = (ImageView) view.findViewById(R.id.modif_linkedin);
        modifGoogle = (ImageView) view.findViewById(R.id.modif_google);



        database = FirebaseDatabase.getInstance();
        mDatabaseRef = database.getReference("users");
        mDtbRef = mDatabaseRef.child(((MainActivity)getActivity()).getUser());


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://testfragments-89798.appspot.com");

        //mDtbRef = mDatabaseRef.child("1000");


        //Toast.makeText(MainActivity.this, mDatabaseRef.toString(), Toast.LENGTH_LONG).show();
        //mDatabaseRef.addValueEventListener(new ValueEventListener() {


        profilPhoto.setProfileId(((MainActivity)getActivity()).getFacebookProfileID());




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

                    switch (key)
                    {
                        case "name":
                            profilName.setText((String) dataSnapshot.child(key).getValue());

                            break;
                        case "facebook":
                            facebookText.setText(id);
                            if(show){
                                toggleBtnFacebook.setToggleOn();
                            }
                            else{
                                toggleBtnFacebook.setToggleOff();
                            }
                        break;
                        case "twitter":
                            twitterText.setText(id);
                            if(show){
                                toggleBtnTwitter.setToggleOn();
                            }
                            else{
                                toggleBtnTwitter.setToggleOff();
                            }
                            break;
                        case "instagram":
                            instagramText.setText(id);
                            if(show){
                                toggleBtnInstagram.setToggleOn();
                            }
                            else{
                                toggleBtnInstagram.setToggleOff();
                            }
                            break;
                        case "snapchat":
                            snapchatText.setText(id);
                            if(show){
                                toggleBtnSnapchat.setToggleOn();
                            }
                            else{
                                toggleBtnSnapchat.setToggleOff();
                            }
                            break;
                        case "linkedin":
                            linkedinText.setText(id);
                            if(show){
                                toggleBtnLinkedin.setToggleOn();
                            }
                            else{
                                toggleBtnLinkedin.setToggleOff();
                            }
                            break;
                        case "google":
                            googleText.setText(id);
                            if(show){
                                toggleBtnGoogle.setToggleOn();
                            }
                            else{
                                toggleBtnGoogle.setToggleOff();
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

        toggleBtnFacebook.setOnToggleChanged(new ToggleButton.OnToggleChanged(){
            @Override
            public void onToggle(boolean on) {
                DatabaseReference myRef = mDtbRef.child("facebook").child("show");
                myRef.setValue(on);
            }
        });

        toggleBtnTwitter.setOnToggleChanged(new ToggleButton.OnToggleChanged(){
            @Override
            public void onToggle(boolean on) {
                DatabaseReference myRef = mDtbRef.child("twitter").child("show");
                myRef.setValue(on);
            }
        });

        toggleBtnInstagram.setOnToggleChanged(new ToggleButton.OnToggleChanged(){
            @Override
            public void onToggle(boolean on) {
                DatabaseReference myRef = mDtbRef.child("instagram").child("show");
                myRef.setValue(on);
            }
        });

        toggleBtnSnapchat.setOnToggleChanged(new ToggleButton.OnToggleChanged(){
            @Override
            public void onToggle(boolean on) {
                DatabaseReference myRef = mDtbRef.child("snapchat").child("show");
                myRef.setValue(on);
            }
        });

        toggleBtnLinkedin.setOnToggleChanged(new ToggleButton.OnToggleChanged(){
            @Override
            public void onToggle(boolean on) {
                DatabaseReference myRef = mDtbRef.child("linkedin").child("show");
                myRef.setValue(on);
            }
        });

        toggleBtnGoogle.setOnToggleChanged(new ToggleButton.OnToggleChanged(){
            @Override
            public void onToggle(boolean on) {
                DatabaseReference myRef = mDtbRef.child("google").child("show");
                myRef.setValue(on);
            }
        });

        modifFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkClicked="facebook";
                EditSocialFragment fragment = new EditSocialFragment();
                ((MainActivity)getActivity()).launchFragment(fragment, null);

            }
        });

        modifTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkClicked="twitter";
                EditSocialFragment fragment = new EditSocialFragment();
                ((MainActivity)getActivity()).launchFragment(fragment, null);

            }
        });

        modifInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkClicked="instagram";
                EditSocialFragment fragment = new EditSocialFragment();
                ((MainActivity)getActivity()).launchFragment(fragment, null);

            }
        });

        modifSnapchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkClicked="snapchat";
                EditSocialFragment fragment = new EditSocialFragment();
                ((MainActivity)getActivity()).launchFragment(fragment, null);

            }
        });

        modifLinkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkClicked="linkedin";
                EditSocialFragment fragment = new EditSocialFragment();
                ((MainActivity)getActivity()).launchFragment(fragment, null);

            }
        });

        modifGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkClicked="google";
                EditSocialFragment fragment = new EditSocialFragment();
                ((MainActivity)getActivity()).launchFragment(fragment, null);

            }
        });



        /*
        //切换开关
        toggleBtnFb.toggle();

        toggleBtnFb.toggle(false);

        toggleBtnFb.setOnToggleChanged(new ToggleButton.OnToggleChanged(){
            @Override
            public void onToggle(boolean on) {

            }
        });

        toggleBtnFb.setToggleOn();
        toggleBtnFb.setToggleOff();
        //无动画切换
        toggleBtnFb.setToggleOn(false);
        toggleBtnFb.setToggleOff(false);

        //禁用动画
        toggleBtnFb.setAnimate(false);
        */
    }

    public String getNetworkClicked() {
        return networkClicked;
    }


}
