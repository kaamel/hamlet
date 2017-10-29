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
import android.support.annotation.NonNull;
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
import com.genesis.hamlet.data.DataSource;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.di.Injection;
import com.genesis.hamlet.ui.mmessages.MMessagesFragment;
import com.genesis.hamlet.ui.userdetail.UserDetailFragment;
import com.genesis.hamlet.ui.users.UsersFragment;
import com.genesis.hamlet.util.BaseFragmentInteractionListener;
import com.genesis.hamlet.util.FoaBaseActivity;
import com.genesis.hamlet.util.NetworkHelper;
import com.genesis.hamlet.util.Properties;
import com.genesis.hamlet.util.UserHelper;
import com.genesis.hamlet.util.mvp.BaseView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.parceler.Parcels;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
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

    BroadcastReceiver br;
    Intent remoteIntent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataRepository = Injection.provideDataRepository();
        remoteIntent = getIntent();
        //MainActivityPermissionsDispatcher.setUpNotificationReceiverWithCheck(this);
    }

    boolean isNotificationSetup = false;

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    protected void continueIfPermitted() {
        if (isNotificationSetup)
            return;
        isNotificationSetup = true;
        setUpNotificationReceiver();
        if (remoteIntent != null) {
            String action = remoteIntent.getStringExtra("action");
            String senderUid = remoteIntent.getStringExtra("senderUid");
            if (action != null && action.equals("request_to_connect")) {
                final Bundle bundle = UserHelper.bundleNotification(remoteIntent);

                dataRepository.findUserById(senderUid, new DataSource.OnUserCallback() {

                    @Override
                    public void onSuccess(User user) {
                        if (user != null) {
                            bundle.putParcelable(Properties.BUNDLE_KEY_USER, Parcels.wrap(user));
                            showFragment(MMessagesFragment.class, bundle, true);
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        showFragment(MMessagesFragment.class, bundle, true);
                    }

                    @Override
                    public void onNetworkFailure() {
                        showFragment(MMessagesFragment.class, bundle, true);
                    }
                });

            }
            else if (action != null && action.equals("request_to_connect_accepted")) {
                final Bundle bundle = UserHelper.bundleNotification(remoteIntent);
                dataRepository.findUserById(senderUid, new DataSource.OnUserCallback() {

                    @Override
                    public void onSuccess(User user) {
                        if (user != null) {
                            bundle.putParcelable(Properties.BUNDLE_KEY_USER, Parcels.wrap(user));
                            showFragment(MMessagesFragment.class, bundle, true);
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        showFragment(MMessagesFragment.class, bundle, true);
                    }

                    @Override
                    public void onNetworkFailure() {
                        showFragment(MMessagesFragment.class, bundle, true);
                    }
                });
            }
            showFragment(UsersFragment.class, null, true);
        }
        else
            showFragment(UsersFragment.class, null, true);
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(connectivityBroadcastReceiver, new IntentFilter(CONNECTIVITY_ACTION));
        MainActivityPermissionsDispatcher.setUpNotificationReceiverWithCheck(this);
        MainActivityPermissionsDispatcher.continueIfPermittedWithCheck(this);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(connectivityBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //dataRepository.disconnect(this);
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

    /*
    @Override
    public void onBackPressed() {
        dataRepository.disconnect(this);
        MyInterests.getInstance().clearInterests();
        super.onBackPressed();
    }
    */

    @OnShowRationale({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void showRationaleForCamera(final PermissionRequest request) {
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
    }

    @OnNeverAskAgain({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void showNeverAskForLocation() {
        Toast.makeText(this, "You will need to manually grant location permission", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull
                                           String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if ((ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) ||
                            (ContextCompat.checkSelfPermission(this,
                                    Manifest.permission.ACCESS_COARSE_LOCATION)
                                    == PackageManager.PERMISSION_GRANTED)) {

                        //showTheFragment();
                        //showFragment(UsersFragment.class);

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
            }
        }
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
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
                final String action = intent.getStringExtra("action");
                final String chatRoom = intent.getStringExtra("chatRoom");
                final Bundle bundle = UserHelper.bundleNotification(intent);
                //FirebaseHelper.deleteFirebaseNode("/notifications/" +myUid, senderUid);
                if (action.equals("request_to_connect") || action.equals("request_to_connect_accepted")) {
                    dataRepository.findUserById(senderUid, new DataSource.OnUserCallback() {
                        @Override
                        public void onSuccess(User user) {
                            if (user != null) {
                                if (action.equals("request_to_connect_accepted")) {
                                    Fragment fragment = ((BaseView) getFragment(UserDetailFragment.class, chatRoom));
                                    if (fragment == null) {
                                        fragment = ((BaseView) getFragment(UserDetailFragment.class, null));
                                    }
                                    ((BaseView) fragment).onConnectionAccepted(user, chatRoom);
                                    showFragment(MMessagesFragment.class, chatRoom, bundle, true);
                                }
                                else {
                                    bundle.putParcelable(Properties.BUNDLE_KEY_USER, Parcels.wrap(user));
                                    showFragment(UserDetailFragment.class, chatRoom, bundle, true);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            showFragment(UserDetailFragment.class, chatRoom, bundle, true);
                        }

                        @Override
                        public void onNetworkFailure() {
                            showFragment(UserDetailFragment.class, chatRoom, bundle, true);
                        }
                    });
                }
                else {
                    //other actions
                }
            }
        };

        IntentFilter filter = new IntentFilter(Properties.FCN_RECEIVED_NOTIFICATION);
        registerReceiver(br, filter);
    }

    //// TODO: 10/27/17 I have moved this here from Detail User Fragment - for now just a placeholder
    private void switchToChatView(String chatRoom, User otherUser) {
        Parcelable parcelable = Parcels.wrap(otherUser);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Properties.BUNDLE_KEY_USER, parcelable);
        bundle.putString("chatRoom", chatRoom);
        showFragment(MMessagesFragment.class, chatRoom, bundle,
                true);
    }

    /*
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
                //// TODO: 10/28/17 this section needs to be reviewed and modified
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
            showFragment(UsersFragment.class, null, true);
    }
    */
}
