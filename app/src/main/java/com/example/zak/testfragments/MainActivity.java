package com.example.zak.testfragments;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BeaconConsumer {

//https://www.simplifiedcoding.net/android-navigation-drawer-example-using-fragments/

    private BeaconTransmitter beaconTransmitter;
    private BeaconManager beaconManager;
    private Beacon beacon;
    private List<String> mBeacons = new ArrayList<String>();
    private Collection<Beacon> beaconsPast;

    private HashMap<String, String> map;
    private ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
    private DatabaseReference mDatabaseRef;
    private FirebaseDatabase database;
    private String user;
    private CallbackManager callbackManager;

    private TextView nameView;
    private ProfilePictureView profileImage;

    private ProfileTracker mProfileTracker;


    private String facebookID;
    private String beaconID;

    private FirebaseStorage storage;
    private StorageReference storageRef;

    private ActionBarDrawerToggle toggle;

    public static MainActivity INSTANCE;
    private String facebookProfileID;

    private LoginButton loginButton;
    private DrawerLayout drawer;

    private SimpleDateFormat sdf;
    private Calendar c;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INSTANCE=this;
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        callbackManager = CallbackManager.Factory.create();

        setBluetooth(true);

        AppEventsLogger.activateApp(this);

        //ask for permission to activate bluetooth:
        // https://developer.android.com/training/permissions/requesting.html#perm-request

        //check if a device supports transmission
        Toast.makeText(MainActivity.this, this.checkTransmissionSupported(), Toast.LENGTH_LONG).show();


        database = FirebaseDatabase.getInstance();
        mDatabaseRef = database.getReference("users");

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://testfragments-89798.appspot.com");







        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



         drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        /*drawer.post(new Runnable() {
            @Override public void run() {
                toggle.syncState();
            }
        });*/
        toggle.syncState();


        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        //toggle.setDrawerIndicatorEnabled(false);
        //toggle.setDrawerArrowDrawable();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerview = navigationView.getHeaderView(0);

        //le button est caché(0px en hauteur et largeur dans le header)
        loginButton = (LoginButton) headerview.findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile");

        nameView = (TextView) headerview.findViewById(R.id.nameView);
        profileImage = (ProfilePictureView) headerview.findViewById(R.id.profilePicture);
        profileImage.setVisibility(View.GONE);



        LinearLayout header = (LinearLayout) headerview.findViewById(R.id.header);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user!=null) {
                    launchFragment(new ProfilFragment(), null);
                }
            }
        });



        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                loginButton.setVisibility(View.VISIBLE);

                if (currentAccessToken == null){

                    //deconnexion
                    unsetProfil();

                }
            }
        };


        //si pas connecté à facebook
        if(Profile.getCurrentProfile() == null) {
            launchFragment(new StartFragment(), null);
        }
        else {
            //faire un check inscription avant, là il y est pas
            final Profile profile = Profile.getCurrentProfile();

            facebookID = profile.getId();

            mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener(){
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean inscrit=false;
                    Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                    while (it.hasNext()) {
                        String key = it.next().getKey();
                        String fbID = (String) dataSnapshot.child(key).child("facebookID").getValue();
                        if(fbID!=null){
                            if(fbID.equals(facebookID)){
                                inscrit=true;
                                beaconID=key;
                            }
                        }

                    }
                    if(inscrit){
                        setProfil(profile,beaconID);

                    }
                    else{
                        String userID = inscription(profile);
                        setProfil(profile, userID);
                    }

                    //mProfileTracker.stopTracking();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            //setProfil(profile);

        }


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                toggle.setDrawerIndicatorEnabled(true);
                loginButton.setVisibility(View.GONE);
                if(Profile.getCurrentProfile() == null) {

                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, final Profile profile2) {
                            // profile2 is the new profile

                            facebookID = profile2.getId();

                            mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener(){
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    boolean inscrit=false;
                                    Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                                    while (it.hasNext()) {
                                        String key = it.next().getKey();
                                        String fbID = (String) dataSnapshot.child(key).child("facebookID").getValue();
                                        if(fbID!=null){
                                            if(fbID.equals(facebookID)){
                                                inscrit=true;
                                                beaconID=key;
                                            }
                                        }

                                    }
                                    if(inscrit){
                                        setProfil(profile2, beaconID);

                                    }
                                    else{
                                        String userID = inscription(profile2);
                                        setProfil(profile2, userID);
                                    }

                                    mProfileTracker.stopTracking();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        }
                    };

                }
                else {

                    //quand est ce qu'il est activé???

                }
;
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });





        //displaySelectedScreen(R.id.nav_p1);




    }



    @Override
    public void onBackPressed() {
        /*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();


            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                super.onBackPressed();
                //additional code
            } else {
                getSupportFragmentManager().popBackStack();
            }

        }
        */


/*
        Fragment f =getSupportFragmentManager().findFragmentByTag("SingleFavorisFragment");
        List<Fragment> fs = getSupportFragmentManager().getFragments();

        if(fs!= null && f!=null && f.isVisible()){
            Fragment fr = getSupportFragmentManager().findFragmentByTag("Favoris");



            this.getSupportFragmentManager().popBackStack();

        }
        */

        /*

        List<Fragment> fs = getSupportFragmentManager().getFragments();

        Fragment f =getSupportFragmentManager().findFragmentByTag("BeaconFragment");


        if(fs!= null && f!=null && f.isVisible()) {

            this.getSupportFragmentManager().popBackStack();
        }

*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        /*
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_f1) {
            // Handle the camera action
        } else if (id == R.id.nav_p1) {


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        */
        displaySelectedScreen(item.getItemId());
        return true;
    }

    public void startEmitting(){

        List<Long> listLong = new ArrayList<Long>();
        listLong.add(new Long(146));

        beaconManager = BeaconManager.getInstanceForApplication(this);
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        // beaconManager.getBeaconParsers().add(new BeaconParser().
        //        setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);

        beacon = new Beacon.Builder()
                .setId1(user)
                //.setId1("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6")// Pattern.compile("^[0-9A-Fa-f]{8}-?[0-9A-Fa-f]{4}-?[0-9A-Fa-f]{4}-?[0-9A-Fa-f]{4}-?[0-9A-Fa-f]{12}$");
                .setId2("65535")
                .setId3("2")
                .setManufacturer(0x0118)
                .setTxPower(-59)
                //.setDataFields(Arrays.asList(new Long[] {0l}))
                .setDataFields(listLong)
                .setExtraDataFields(Arrays.asList(new Long[] {0l}))
                .setBluetoothName("")
                .build();
        BeaconParser beaconParser = new BeaconParser()
                .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");
        beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
        beaconTransmitter.startAdvertising(beacon);
    }

    private void displaySelectedScreen(int itemId) {

        if(user!=null) {
            //creating fragment object
            Fragment fragment = null;
            String tag = null;

            //initializing the fragment object which is selected
            switch (itemId) {
                case R.id.nav_p1:
                    fragment = new PingFragment();
                    break;
                /*case R.id.nav_f1:
                    fragment = new FavorisFragment();
                    break;
                    */
                case R.id.nav_test:
                    fragment = new FavorisFragment();
                    tag= "TestFragment";
                    break;
                case R.id.nav_deco:
                    LoginManager.getInstance().logOut();
                    break;
            }

            launchFragment(fragment,tag);
        }

    }

    public void launchFragment(Fragment fragment, String tag){
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment, tag);
            ft.addToBackStack(fragment.getTag());
            ft.commit();

            //fragmentManager.beginTransaction().replace(R.id.content_frame,fragment).addToBackStack("tag").commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                boolean inside=false;

                c = Calendar.getInstance();
                sdf = new SimpleDateFormat("dd/MM HH:mm");


                for(Beacon b: beacons){

                    String strDate = sdf.format(c.getTime());

                    for(HashMap m : listItem){
                        String beacon= ""+b.getId1();
                        String itemBeacon = (String) m.get("id");

                        if (beacon.equals(itemBeacon)){
                            inside=true;
                            m.put("distance", ""+Math.round(b.getDistance())+" mètres");
                            m.put("date", strDate);
                        }
                    }
                    if(!inside){
                        map = new HashMap<String, String>();
                        map.put("id", ""+b.getId1());
                        map.put("distance", ""+Math.round(b.getDistance())+" mètres");
                        map.put("date", strDate);

                        listItem.add(map);
                    }

                    inside=false;
                }




                /*


                //if(beacons.size()!=numberBeacons){
                //if(beaconsPast!=beacons && beacons.size() > beaconsPast.size()) { //probleme car si il detecte un seul beacon il rentre pas dans la condition
                if(beaconsPast!=beacons && beacons.size() > 0) {
                    beaconsPast = beacons;

                    mBeacons.clear();
                    listItem.clear();
                    for (Beacon oneBeacon : beacons) {

                        //mBeacons.add("distance: " + Math.round(oneBeacon.getDistance()) + " id:" + oneBeacon.getId1() + "/" + oneBeacon.getId2() + "/" + oneBeacon.getId3());

                        //plus tart mettre le beacon directement dans une liste de beacon et envoyer le beacon à l'activity
                        map = new HashMap<String, String>();
                        map.put("id", ""+oneBeacon.getId1());
                        map.put("distance", ""+Math.round(oneBeacon.getDistance())+" mètres");
                        //map.put("img", String.valueOf(R.drawable.profile));
                        //identifier avec bluetoothadress elle est unique
                        //map.put("bluetoothAdress", oneBeacon.getBluetoothAddress());
                        map.put("bluetoothName", oneBeacon.getBluetoothName());
                        //map.put("beaconTypeCode", oneBeacon.getBeaconTypeCode()+"");
                        //map.put("parserIdentifier", oneBeacon.getParserIdentifier()+"");

                        //map.put("dataFields", oneBeacon.getDataFields().toString());
                        //map.put("extraDataFields", oneBeacon.getExtraDataFields().toString());
                        //map.put("id2", oneBeacon.getId2()+"");
                        //map.put("id3", oneBeacon.getId3()+"");
                        //map.put("identifiers", oneBeacon.getIdentifiers().toString());
                        //map.put("manufacturer", oneBeacon.getManufacturer()+"");
                        //map.put("rssi", oneBeacon.getRssi()+"");
                        //map.put("serviceUuid", oneBeacon.getServiceUuid()+"");
                        //map.put("txPower", oneBeacon.getTxPower()+"");
                        listItem.add(map);


                        //Toast.makeText(MainActivity.this, oneBeacon.getDataFields().get(0)+"", Toast.LENGTH_LONG).show();

                    }
                }

                */
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {    }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
        //beaconTransmitter.stopAdvertising();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void setProfil(Profile profile, final String beaconID) {
        nameView.setText(profile.getName());
        facebookProfileID=profile.getId();

        profileImage.setProfileId(profile.getId());
        profileImage.setVisibility(View.VISIBLE);


        //envoie la photo de l'user sur le storage firebase, enlevé car on use le profilepictureview de facebook à la place

       /*

        url = null;
        try {
            url = new URL(profile.getProfilePictureUri(50,50).toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }



        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    is = url.openConnection().getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }
        });

        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        UploadTask uploadTask = storageRef.child("images/"+beaconID).putStream(is);


*/

        user=beaconID;
        if(beaconTransmitter!=null) {
            if (beaconTransmitter.isStarted()) {
                beaconTransmitter.stopAdvertising();
            }
        }
        startEmitting();
        displaySelectedScreen(R.id.nav_p1);


    }

    private void unsetProfil() {
        facebookProfileID=null;
        nameView.setText("");
        profileImage.setProfileId(null);
        profileImage.setVisibility(View.GONE);
        user=null;
        if(beaconTransmitter!=null) {
            if (beaconTransmitter.isStarted()) {
                beaconTransmitter.stopAdvertising();
            }
        }
        //displaySelectedScreen(R.id.nav_p1);
        launchFragment(new StartFragment(), null);
        //stopadvertising etc

    }

    private String inscription(Profile profile){


        String facebookID = profile.getId();
        String name = profile.getName();

        //faire un ntest pour voir si l'uuid existe pas deja
        String beaconID = UUID.randomUUID().toString();


        mDatabaseRef.child(beaconID).child("facebookID").setValue(facebookID);
        mDatabaseRef.child(beaconID).child("name").setValue(name);

        return beaconID;

    }


    public ArrayList getListItem(){
        return listItem;
    }

    public StorageReference getStorageRef(){
        return storageRef;
    }

    public String getUser() {
        return user;
    }

    private String checkTransmissionSupported() {
        int result = BeaconTransmitter.checkTransmissionSupported(getApplicationContext());
        switch(result){
            case 0:
                return "SUPPORTED";
            case 1:
                return "NOT_SUPPORTED_MIN_SDK";
            case 2:
                return "NOT_SUPPORTED_BLE";
            case 3:
                return "NOT_SUPPORTED_MULTIPLE_ADVERTISEMENTS";
            case 4:
                return "NOT_SUPPORTED_CANNOT_GET_ADVERTISER";
            case 5:
                return "NOT_SUPPORTED_CANNOT_GET_ADVERTISER_MULTIPLE_ADVERTISEMENTS";
            default:
                return "";
        }

    }

    public static boolean setBluetooth(boolean enable) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabled = bluetoothAdapter.isEnabled();
        if (enable && !isEnabled) {
            return bluetoothAdapter.enable();
        }
        else if(!enable && isEnabled) {
            return bluetoothAdapter.disable();
        }
        // No need to change bluetooth state
        return true;
    }

    public String getFacebookProfileID() {
        return facebookProfileID;
    }

    public DrawerLayout getDrawer() {
        return drawer;
    }

    public ActionBarDrawerToggle getToggle() {
        return toggle;
    }
}
