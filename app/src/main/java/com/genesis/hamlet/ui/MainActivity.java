package com.genesis.hamlet.ui;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.genesis.hamlet.R;
import com.genesis.hamlet.data.DataRepository;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.di.Injection;
import com.genesis.hamlet.ui.mmessages.MMessagesFragment;
import com.genesis.hamlet.ui.userdetail.UserDetailFragment;
import com.genesis.hamlet.ui.users.UsersFragment;
import com.genesis.hamlet.util.BaseFragmentInteractionListener;
import com.genesis.hamlet.util.FoaBaseActivity;
import com.genesis.hamlet.util.NetworkHelper;
import com.genesis.hamlet.util.Properties;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.parceler.Parcels;

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
    private DataRepository dataRepository;

    static String action = null;
    static User otherUser = null;
    private static String myUid = null;
    private static String chatRoom = null;

    BroadcastReceiver br;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataRepository = Injection.provideDataRepository();
        setUpNotificationReceiver();
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

                if (action != null && action.equals("request_to_connect")) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(Properties.BUNDLE_KEY_USER, Parcels.wrap(otherUser));
                    showFragment(UserDetailFragment.class, bundle, true);
                    otherUser = null;
                    action = null;
                }
                else
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
    protected void onDestroy() {
        unregisterReceiver(br);
        super.onDestroy();
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
                //Toast.makeText(getApplicationContext(), "Network is available", Toast.LENGTH_SHORT).show();
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

    @Override
    public DataRepository getDataRepository() {
        return dataRepository;
    }

    @Override
    public void setTitle(String s) {
        ActionBar stb = getSupportActionBar();
        if (stb != null)
            stb.setTitle(s);
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
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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

                        showTheFragment();
                        //showFragment(UsersFragment.class);

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
            }
        }
    }

    void setUpNotificationReceiver() {
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                String myUid = intent.getStringExtra("receiverUid");
                String senderUid = intent.getStringExtra("senderUid");
                FirebaseUser me = FirebaseAuth.getInstance().getCurrentUser();
                /*
                if (myUid == null ||  me == null || !(myUid.equals(me.getUid()))) {
                    FirebaseHelper.deleteFirebaseNode(myUid, senderUid);
                    return;
                }
                */
                String chatRoom = intent.getStringExtra("chatRoom");
                String name = intent.getStringExtra("name");
                String action = intent.getStringExtra("action");
                String title = intent.getStringExtra("title");
                String message = intent.getStringExtra("message");
                User user = new User();
                user.setUid(senderUid);
                user.setDisplayName(name);
                user.setIntroTitle(title);
                user.setIntroDetail(message);
                //FirebaseHelper.deleteFirebaseNode("/notifications/" +myUid, senderUid);
                if (action.equals("request_to_connect_accepted")) {
                    UserDetailFragment.onConnectAccepted(chatRoom, user);
                }
                else {

                    Parcelable parcelable = Parcels.wrap(user);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(Properties.BUNDLE_KEY_USER, parcelable);

                    bundle.putString("chatRoom", chatRoom);
                    bundle.putString("myUid", myUid);

                    showFragment(UserDetailFragment.class, bundle, true);
                }
            }
        };

        IntentFilter filter = new IntentFilter(Properties.FCN_RECEIVED_NOTIFICATION);
        registerReceiver(br, filter);
    }

    //// TODO: 10/27/17 I have move this here from Detail User Fragment - for now just a placeholder
    private void switchToChatView(String chatRoom, User otherUser) {
        Parcelable parcelable = Parcels.wrap(otherUser);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Properties.BUNDLE_KEY_USER, parcelable);
        bundle.putString("chatRoom", chatRoom);
        showFragment(MMessagesFragment.class, bundle,
                true);
    }

    private void showTheFragment() {
        if (action != null && (action.equals("request_to_connect") || action.equals("request_to_connect_accepted"))) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Properties.BUNDLE_KEY_USER, Parcels.wrap(otherUser));
            bundle.putString("chatRoom", chatRoom);
            bundle.putString("myUid", myUid);
            otherUser = null;
            action = null;
            chatRoom = null;
            myUid = null;

            FirebaseUser me = FirebaseAuth.getInstance().getCurrentUser();
            if (me != null
                    && me.getUid().equals(myUid)) {
                if (action.equals("request_to_connect_accepted")) {
                    UserDetailFragment.onConnectAccepted(chatRoom, otherUser);
                }
                else
                    showFragment(UserDetailFragment.class, bundle, true);
            }
            else {
                //this is an response and we are no longer interested
                finish();
            }
        }
        else
            showFragment(UsersFragment.class);
    }

    public static void setRemoteAction(String action, String myUid, User user, String chatRoom) {
        MainActivity.otherUser = user;
        MainActivity.action = action;
        MainActivity.myUid = myUid;
        MainActivity.chatRoom = chatRoom;
    }
}
