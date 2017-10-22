package com.genesis.hamlet.ui;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.genesis.hamlet.R;
import com.genesis.hamlet.ui.users.UsersFragment;
import com.genesis.hamlet.util.BaseFragmentInteractionListener;
import com.genesis.hamlet.util.FoaBaseActivity;
import com.genesis.hamlet.util.NetworkHelper;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;


/**
 * The container responsible for showing and destroying relevant {@link Fragment}, handling
 * back and up navigation using the Fragment back stack and maintaining global state
 * and event subscriptions. This is based on the Fragment Oriented Architecture explained here
 * http://vinsol.com/blog/2014/09/15/advocating-fragment-oriented-applications-in-android/
 */
@RuntimePermissions
public class MainActivity extends FoaBaseActivity implements BaseFragmentInteractionListener {

    AppBarLayout appBarLayout;

    private IntentFilter connectivityIntentFilter;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        checkPermision();
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    public void checkPermision() {
        showFragment(UsersFragment.class);
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(connectivityBroadcastReceiver, new IntentFilter(CONNECTIVITY_ACTION));

        if (checkLocationPermission()) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {

                showFragment(UsersFragment.class);


            }
        }
    }

    @Override
    protected void onPause() {
        unregisterReceiver(connectivityBroadcastReceiver);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    BroadcastReceiver connectivityBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!NetworkHelper.getInstance().isNetworkAvailable(context)) {
                Toast.makeText(getApplicationContext(), "Network is not available", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Network is available", Toast.LENGTH_SHORT).show();
            }
        }
    };

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
//    }

    /*
        @Override
        public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            showFragment(UsersFragment.class);
        }
    */
    @Override
    public void resetToolBarScroll() {
        appBarLayout.setExpanded(true, true);
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
//                        locationManager.requestLocationUpdates(provider, 400, 1, this);
                        showFragment(UsersFragment.class);

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }
}
