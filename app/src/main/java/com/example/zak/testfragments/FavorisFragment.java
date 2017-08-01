package com.example.zak.testfragments;

        import android.graphics.Bitmap;
        import android.graphics.drawable.Drawable;
        import android.os.Bundle;
        import android.support.annotation.Nullable;
        import android.support.v4.app.Fragment;
        import android.support.v7.app.ActionBar;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ListView;
        import android.widget.ProgressBar;
        import android.widget.SimpleAdapter;

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
        import java.util.List;


public class FavorisFragment extends Fragment {

    private ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> listItemCopie = new ArrayList<HashMap<String, String>>();
    private ArrayList<String> listFavoris = new ArrayList<String>();
    private ListView listViewFavoris;
    private SimpleAdapter simpleAdapter;
    private ProgressBar progressBar;
    private String favorisClicked;
    public static FavorisFragment INSTANCE;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    //private  map;
    private ArrayList<DataModel> listDataModel = new ArrayList<DataModel>();


    private DatabaseReference mDatabaseRef;
    private FirebaseDatabase database;
    private DatabaseReference mDtbRef;
    private URL url;
    private String path;

    private ArrayList<DataModel> dataModels;
    private static CustomAdapter adapter;
    private Bitmap bitmap;
    private String name=null;
    private String favorisPhoto =null;
    private Drawable drawable;
    private ActionBar supportActionBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_test, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Favoris");

        dataModels=null;
        adapter=null;


        INSTANCE = this;

        storageRef = ((MainActivity) getActivity()).getStorageRef();


        database = FirebaseDatabase.getInstance();
        mDatabaseRef = database.getReference("users");
        storage = FirebaseStorage.getInstance();


        listViewFavoris = (ListView) view.findViewById(R.id.list);
        dataModels = new ArrayList<>();


        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Iterator<DataSnapshot> it = dataSnapshot.child(((MainActivity) getActivity()).getUser()).child("favoris").getChildren().iterator();

                while (it.hasNext()) {
                    String key = it.next().getKey();
                    listFavoris.add(key);
                }
                if (listFavoris.size() != 0) {
                    int iteratorImage = 0;

                    for (String id : listFavoris) {
                        name = (String) dataSnapshot.child(id).child("name").getValue();
                        favorisPhoto = (String) dataSnapshot.child(id).child("facebookID").getValue();

                        HashMap map = new HashMap<String, String>();
                        List<String> networks = new ArrayList<String>();





                        /*


                        if(dataSnapshot.child(id).child("facebook").child("show").getValue()!=null){
                            if((Boolean) dataSnapshot.child(id).child("facebook").child("show").getValue()){
                                map.put("image"+iteratorImage,"facebook");
                                //networks.add()
                                iteratorImage++;
                            }

                        }
                        if(dataSnapshot.child(id).child("twitter").child("show").getValue()!=null){
                            if((Boolean) dataSnapshot.child(id).child("twitter").child("show").getValue()){
                                map.put("image"+iteratorImage,"twitter");
                                iteratorImage++;
                            }
                        }
                        if(dataSnapshot.child(id).child("instagram").child("show").getValue()!=null){
                            if((Boolean) dataSnapshot.child(id).child("instagram").child("show").getValue()){
                                map.put("image"+iteratorImage,"instagram");
                                iteratorImage++;
                            }
                        }
                        if(dataSnapshot.child(id).child("snapchat").child("show").getValue()!=null){
                            if((Boolean) dataSnapshot.child(id).child("snapchat").child("show").getValue()){
                                map.put("image"+iteratorImage,"snapchat");
                                iteratorImage++;
                            }
                        }
                        if(dataSnapshot.child(id).child("linkedin").child("show").getValue()!=null){
                            if((Boolean) dataSnapshot.child(id).child("linkedin").child("show").getValue()){
                                map.put("image"+iteratorImage,"linkedin");
                                iteratorImage++;
                            }
                        }
                        if(dataSnapshot.child(id).child("google").child("show").getValue()!=null){
                            if((Boolean) dataSnapshot.child(id).child("google").child("show").getValue()){
                                map.put("image"+iteratorImage,"google");
                                iteratorImage++;
                            }
                        }
                        */


                        iteratorImage = 0;


                        //listDataModel.add(new DataModel(name,bitmap));

                        DataModel dm = new DataModel(name, favorisPhoto, id, map);
                        dataModels.add(dm);

                        //map.clear()


                    }
                }

                /*
                listItemCopie.addAll(listItem);
                simpleAdapter.notifyDataSetChanged();
                */

                adapter = new CustomAdapter(dataModels, getContext());

                listViewFavoris.setAdapter(adapter);


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        /*for(DataModel dm : listDataModel){
            dataModels.add(dm);
        }*/


        listViewFavoris.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {

                        //Intent intent = new Intent(getApplicationContext(), BeaconActivity.class);
                        //HashMap<String, String> map = (HashMap<String, String>)arg0.getItemAtPosition(position);
                        DataModel dm = dataModels.get(position);
                        favorisClicked = dm.getId();
                        //intent.putExtra("map", map);
                        //startActivity(intent);


                        ((MainActivity) getActivity()).launchFragment(new SingleFavorisFragment(), "SingleFavorisFragment");
                    }
                }
        );


    }


    public String getFavorisClicked() {
        return favorisClicked;
    }

    public ActionBar getSupportActionBar() {
        return supportActionBar;
    }


}
