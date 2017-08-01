package com.example.zak.testfragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

import static com.facebook.login.widget.ProfilePictureView.TAG;


public class PingFragment extends Fragment{

    private ListView listViewBeacons;
    private SimpleAdapter simpleAdapter;
    private ArrayList<HashMap<String, String>> listItemCopie = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> list;
    private ArrayList<HashMap<String, String>> list2 = new ArrayList<HashMap<String, String>>();
    private StorageReference storageRef;
    private String beaconClicked;
    private ProgressBar progressBar;
    private FirebaseDatabase database;
    private DatabaseReference mDatabaseRef;
    public static PingFragment INSTANCE;

    private ArrayList<DataPingModel> dataModels;
    private ArrayList<DataPingModel> dataModelsCopie = new ArrayList<>();
    private String name;
    private String photo;
    private static CustomPingAdapter adapter;
    private TextView emptyListText;
    private ImageView background;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_ping, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("PYL");

        INSTANCE=this;

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        progressBar.setVisibility(View.GONE);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);

        background= (ImageView) view.findViewById(R.id.background_ping);
        if(adapter!=null){
            background.setVisibility(View.GONE);
        }

        emptyListText = (TextView) view.findViewById(R.id.emptyListText);
        emptyListText.setVisibility(View.GONE);



        database = FirebaseDatabase.getInstance();
        mDatabaseRef = database.getReference("users");

        listViewBeacons = (ListView)view.findViewById(R.id.ListViewBeacons);
        dataModels= new ArrayList<>();
        adapter= new CustomPingAdapter(dataModelsCopie,getContext());
        listViewBeacons.setAdapter(adapter);


        //Récupération du ListView présent dans notre IHM


        /*
         listViewBeacons = (ListView)view.findViewById(R.id.ListViewBeacons);
         simpleAdapter = new SimpleAdapter(this.getContext(), listItemCopie, R.layout.affichageitem,
         new String[] {"img", "name", "distance", "image0", "image1", "image2", "image3", "image4", "image5"}, new int[] {R.id.img, R.id.idChange, R.id.distanceChange, R.id.image0, R.id.image1, R.id.image2, R.id.image3, R.id.image4, R.id.image5});
         listViewBeacons.setAdapter(simpleAdapter);

         */


        //fait un refresh une première fois tout seul
        //simpleAdapter.notifyDataSetChanged();

        //click sur l'étoile dans la listview(recherche google: onclick element in listview
        //http://androidforbeginners.blogspot.fr/2010/03/clicking-buttons-in-listview-row.html
        //http://stackoverflow.com/questions/12596199/android-how-to-set-onclick-event-for-button-in-list-item-of-listview


        ImageButton refresh = (ImageButton) view.findViewById(R.id.button_refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                background.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                emptyListText.setVisibility(View.GONE);

                //list = new ArrayList<HashMap<String, String>>(((MainActivity)getActivity()).getListItem());
                list=((MainActivity)getActivity()).getListItem();


                mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener(){
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        System.out.println("We're done loading the initial "+dataSnapshot.getChildrenCount()+" items");

                        //Log.w("myApp", "We're done loading the initial "+dataSnapshot.getChildrenCount()+" items");
                        int  iteratorImage=0;
                        //dataModels.clear();
                        adapter.clear();

                        for( HashMap hmap : list){
                            //if(hmap.get("id")== key){

                            String id = (String) hmap.get("id");
                            HashMap map = new HashMap<String, String>();
                            String distance = (String) hmap.get("distance");
                            String date= (String) hmap.get("date");
                            boolean favoris=false;

                            if(id != null) {
                                //faire un if name existe pas
                                name = (String) dataSnapshot.child(id).child("name").getValue();

                                photo = (String) dataSnapshot.child(id).child("facebookID").getValue();

                                String user = ((MainActivity)getActivity()).getUser();
                                favoris=false;

                                if(dataSnapshot.child(user).child("favoris").child(id).getValue()!=null){
                                    favoris=true;
                                }



                                //hmap.put("name", name);
                                /*


                                if(dataSnapshot.child((String) hmap.get("id")).child("facebook")!=null){
                                    if((Boolean) dataSnapshot.child((String) hmap.get("id")).child("facebook").child("show").getValue()){
                                        hmap.put("image"+iteratorImage,String.valueOf(R.drawable.facebook));
                                        iteratorImage++;
                                    }
                                }
                                if(dataSnapshot.child((String) hmap.get("id")).child("twitter")!=null){
                                    if((Boolean) dataSnapshot.child((String) hmap.get("id")).child("twitter").child("show").getValue()){
                                        hmap.put("image"+iteratorImage,String.valueOf(R.drawable.twitter));
                                        iteratorImage++;
                                    }
                                }
                                if(dataSnapshot.child((String) hmap.get("id")).child("instagram")!=null){
                                    if((Boolean) dataSnapshot.child((String) hmap.get("id")).child("instagram").child("show").getValue()){
                                        hmap.put("image"+iteratorImage,String.valueOf(R.drawable.instagram));
                                        iteratorImage++;
                                    }
                                }
                                if(dataSnapshot.child((String) hmap.get("id")).child("snapchat")!=null){
                                    if((Boolean) dataSnapshot.child((String) hmap.get("id")).child("snapchat").child("show").getValue()){
                                        hmap.put("image"+iteratorImage,String.valueOf(R.drawable.snapchat));
                                        iteratorImage++;
                                    }
                                }
                                if(dataSnapshot.child((String) hmap.get("id")).child("linkedin")!=null){
                                    if((Boolean) dataSnapshot.child((String) hmap.get("id")).child("linkedin").child("show").getValue()){
                                        hmap.put("image"+iteratorImage,String.valueOf(R.drawable.linkedin));
                                        iteratorImage++;
                                    }
                                }
                                if(dataSnapshot.child((String) hmap.get("id")).child("google")!=null){
                                    if((Boolean) dataSnapshot.child((String) hmap.get("id")).child("google").child("show").getValue()){
                                        hmap.put("image"+iteratorImage,String.valueOf(R.drawable.google));
                                        iteratorImage++;
                                    }
                                }
                                */


                            }

                            DataPingModel dm = new DataPingModel(name,photo,id, distance, map,favoris, date);
                            dataModels.add(dm);
                            adapter.add(dm);
                            //list2.add(hmap);


                        }
                        //adapter.clear();
                        //dataModelsCopie.clear();
                        //dataModelsCopie.addAll(dataModels);




                        adapter.notifyDataSetChanged();
                        if(adapter.getCount()==0){
                            emptyListText.setVisibility(View.VISIBLE);
                        }







                        /*
                        listItemCopie.clear();

                        listItemCopie.addAll(list2);
                        list2.clear();
                        simpleAdapter.notifyDataSetChanged();
                        */
                        progressBar.setVisibility(View.GONE);

                }
                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }

            });



            }
        });

        listViewBeacons.setOnItemClickListener(
                new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {



                        DataPingModel dm = dataModels.get(position);
                        beaconClicked=dm.getId();

                        ((MainActivity)getActivity()).launchFragment(new BeaconFragment(), "BeaconFragment");
                    }
                }
        );


    }


    public String getBeaconClicked() {
        return beaconClicked;
    }
}
