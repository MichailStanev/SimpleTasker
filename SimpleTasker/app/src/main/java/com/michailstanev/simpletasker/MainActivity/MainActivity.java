package com.michailstanev.simpletasker.MainActivity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.michailstanev.simpletasker.Fragments.AddFenceFragment;
import com.michailstanev.simpletasker.Fragments.MyMapFragment;
import com.michailstanev.simpletasker.Fragments.MessageFragment;
import com.michailstanev.simpletasker.Fragments.ProfileFragment;
import com.michailstanev.simpletasker.MyFenceReceiver;
import com.michailstanev.simpletasker.R;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;

import static com.michailstanev.simpletasker.MyFenceReceiver.FENCE_RECEIVER_ACTION;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private DrawerLayout drawer;
    private long backPressedTime;
    private Toast backToast;
    private String userId;
    Boolean bool = false;

    protected PendingIntent myPendingIntent;
    protected GoogleApiClient mGoogleApiClient;
    protected MyFenceReceiver mMyFenceReceiver;

    StitchAppClient stitchAppClient;

    private static AppCompatActivity instance ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;

        // Create the StitchAppClient
        //final StitchAppClient stitchAppClient = Stitch.initializeAppClient(getString(R.string.my_app_id));

        Stitch.initializeDefaultAppClient(getString(R.string.my_app_id));
        stitchAppClient = Stitch.getDefaultAppClient();


        //Login
        doLogin();

        //Toolbar code
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        //Setting starting Fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MessageFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_message);
        }

//        //Setting intent for RegisterFence
////        Intent intent = new Intent(FENCE_RECEIVER_ACTION);
////        myPendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
////
////
        //Starting up FenceReceiver
        mMyFenceReceiver = new MyFenceReceiver();
        registerReceiver(mMyFenceReceiver , new IntentFilter(FENCE_RECEIVER_ACTION));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_message:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MessageFragment()).commit();
                break;
            case R.id.nav_chat:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AddFenceFragment()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ProfileFragment()).commit();
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Didn't i say you cant share yet!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_send:
                Toast.makeText(this, "Sadly nothing cool :(", Toast.LENGTH_SHORT).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        //Close Drawer on back pressed

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(fragment instanceof MyMapFragment){
            bool = true;
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new AddFenceFragment()).commit();
        }
        //Double back to exit application
        else if (backPressedTime + 2000 > System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            finishAffinity();
            System.exit(0);
            return;
        }
        //Showing toast to double press back to exit
        else{
            backToast = Toast.makeText(getBaseContext() , "Press back again to exit" , Toast.LENGTH_SHORT);
            backToast.show();
            backPressedTime = System.currentTimeMillis();
        }
    }

    private void doLogin() {
        Stitch.getDefaultAppClient().getAuth().loginWithCredential(new AnonymousCredential())
                .addOnSuccessListener(user -> {
                    userId = user.getId();
                    invalidateOptionsMenu();
                    Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    invalidateOptionsMenu();
                    Log.d(TAG, "error logging in", e);
                    Toast.makeText(MainActivity.this, "Failed logging in", Toast.LENGTH_SHORT).show();
                });
    }


//    public void registerFence() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
//                PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
//        } else {
//            AwarenessFence inLocationFence = LocationFence.in(43.243423, 27.880923, 200, 1);
//
//            Awareness.FenceApi.updateFences(
//                    mGoogleApiClient,
//                    new FenceUpdateRequest.Builder()
//                            .addFence(IN_LOCATION_FENCE_KEY, inLocationFence, myPendingIntent)
//                            .build())
//                    .setResultCallback(new ResultCallback<Status>() {
//                        @Override
//                        public void onResult(@NonNull Status status) {
//                            if (status.isSuccess()) {
//                                Log.d(TAG, "onResult: Register Success");
//                            } else {
//                                Log.d(TAG, "onResult: Register Fail");
//                            }
//                        }
//                    });
//        }
//    }


    public static Context getContext() {
        return instance;
    }

}