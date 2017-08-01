package com.example.zak.testfragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class FavorisPastFragment extends Fragment {

    private ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> listItemCopie = new ArrayList<HashMap<String, String>>();
    private ArrayList<Drawable> listImages = new ArrayList<Drawable>();
    private ArrayList<String> listFavoris = new ArrayList<String>();
    private ListView listViewFavoris;
    private SimpleAdapter simpleAdapter;
    private ProgressBar progressBar;
    private String favorisClicked;
    public static FavorisPastFragment INSTANCE;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private HashMap map;


    private DatabaseReference mDatabaseRef;
    private FirebaseDatabase database;
    private DatabaseReference mDtbRef;
    private ImageView mImageView;
    private URL url;
    private String path;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_favoris, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Favoris");

        INSTANCE=this;

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        storageRef = ((MainActivity)getActivity()).getStorageRef();

        mImageView = (ImageView) view.findViewById(R.id.imageff);



/*
        listViewFavoris = (ListView)view.findViewById(R.id.ListViewFavoris);
        simpleAdapter = new SimpleAdapter(this.getContext(), listItemCopie, R.layout.affichageitemfavoris,
                new String[] {"img", "name", "image0", "image1", "image2", "image3", "image4", "image5"}, new int[] {R.id.img, R.id.idChange, R.id.img0, R.id.img1, R.id.img2, R.id.img3, R.id.img4, R.id.img5});
        listViewFavoris.setAdapter(simpleAdapter);
        */

        database = FirebaseDatabase.getInstance();
        mDatabaseRef = database.getReference("users");
        //mDtbRef = mDatabaseRef.child(((MainActivity)getActivity()).getUser()).child("favoris");
        storage = FirebaseStorage.getInstance();
        //storageRef = storage.getReferenceFromUrl("gs://testfragments-89798.appspot.com");

       /* MonAdapter adapter = new MonAdapter(this, R.layout.list_complex_extra);
        // Ajout des éléments dans la liste
        adapter.add(new MonObjet("Premier element", ImageBitmaps[0]) );
        adapter.add(new MonObjet("Premier element", ImageBitmaps[0]) );
        listViewFavoris.setListAdapter(adapter);
        */


        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Iterator<DataSnapshot> it = dataSnapshot.child(((MainActivity)getActivity()).getUser()).child("favoris").getChildren().iterator();

                while (it.hasNext()) {
                    String key = it.next().getKey();
                    listFavoris.add(key);
                }
                if(listFavoris.size()!=0) {
                    int  iteratorImage=0;

                    for (String s : listFavoris) {
                        String name = (String) dataSnapshot.child(s).child("name").getValue();
                        String fbID = (String) dataSnapshot.child(s).child("facebookID").getValue();
                        //Profile profile = new Profile(fbID,null,null,null,null,null);


                        map = new HashMap<String, String>();
                        //map.put("img", String.valueOf(R.drawable.profile));
                        map.put("id", s);
                        map.put("name", name);

                        //StorageReference storageRefImage = storageRef.child("images/"+id+".jpg");

                        path= "images/"+s;


                        Thread thread = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                storageRef.child(path).getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] data) {
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        //Drawable d = new BitmapDrawable(getResources(), bitmap);
                                        Drawable d = new BitmapDrawable(getResources(),BitmapFactory.decodeByteArray(data, 0, data.length));
                                        listImages.add(d);
                                        map.put("img", d);
                                        mImageView.setImageBitmap(bitmap);
                                        //mImageView.setBackground(d);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {

                                        map.put("img", String.valueOf(R.drawable.profile));
                                    }
                                });


                            }
                        });

                        thread.start();

                        try {
                            thread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }










                        if(dataSnapshot.child((String) map.get("id")).child("facebook").child("show").getValue()!=null){
                            if((Boolean) dataSnapshot.child((String) map.get("id")).child("facebook").child("show").getValue()){
                                map.put("image"+iteratorImage,String.valueOf(R.drawable.facebook));
                                iteratorImage++;
                            }
                        }
                        if(dataSnapshot.child((String) map.get("id")).child("twitter").child("show").getValue()!=null){
                            if((Boolean) dataSnapshot.child((String) map.get("id")).child("twitter").child("show").getValue()){
                                map.put("image"+iteratorImage,String.valueOf(R.drawable.twitter));
                                iteratorImage++;
                            }
                        }
                        if(dataSnapshot.child((String) map.get("id")).child("instagram").child("show").getValue()!=null){
                            if((Boolean) dataSnapshot.child((String) map.get("id")).child("instagram").child("show").getValue()){
                                map.put("image"+iteratorImage,String.valueOf(R.drawable.instagram));
                                iteratorImage++;
                            }
                        }
                        if(dataSnapshot.child((String) map.get("id")).child("snapchat").child("show").getValue()!=null){
                            if((Boolean) dataSnapshot.child((String) map.get("id")).child("snapchat").child("show").getValue()){
                                map.put("image"+iteratorImage,String.valueOf(R.drawable.snapchat));
                                iteratorImage++;
                            }
                        }
                        if(dataSnapshot.child((String) map.get("id")).child("linkedin").child("show").getValue()!=null){
                            if((Boolean) dataSnapshot.child((String) map.get("id")).child("linkedin").child("show").getValue()){
                                map.put("image"+iteratorImage,String.valueOf(R.drawable.linkedin));
                                iteratorImage++;
                            }
                        }
                        if(dataSnapshot.child((String) map.get("id")).child("google").child("show").getValue()!=null){
                            if((Boolean) dataSnapshot.child((String) map.get("id")).child("google").child("show").getValue()){
                                map.put("image"+iteratorImage,String.valueOf(R.drawable.google));
                                iteratorImage++;
                            }
                        }

                        listItem.add(map);
                        iteratorImage=0;
                    }
                }

                listItemCopie.addAll(listItem);
                simpleAdapter.notifyDataSetChanged();

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listImages.toString();

        listViewFavoris.setOnItemClickListener(
                new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {

                        //Intent intent = new Intent(getApplicationContext(), BeaconActivity.class);
                        HashMap<String, String> map = (HashMap<String, String>)arg0.getItemAtPosition(position);
                        favorisClicked=map.get("id");
                        //intent.putExtra("map", map);
                        //startActivity(intent);

                        ((MainActivity)getActivity()).launchFragment(new SingleFavorisFragment(), null);
                    }
                }
        );



    }


    public String getFavorisClicked() {
        return favorisClicked;
    }


}
