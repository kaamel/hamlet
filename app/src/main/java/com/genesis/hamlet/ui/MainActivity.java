package com.genesis.hamlet.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
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

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;


/**
 * The container responsible for showing and destroying relevant {@link Fragment}, handling
 * back and up navigation using the Fragment back stack and maintaining global state
 * and event subscriptions. This is based on the Fragment Oriented Architecture explained here
 * http://vinsol.com/blog/2014/09/15/advocating-fragment-oriented-applications-in-android/
 */
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
        //dataRepository.init(this);
        remoteIntent = getIntent();
    }

    boolean isNotificationSetup = false;

    protected void continueSetup() {
        if (remoteIntent != null) {
            final String senderUid = remoteIntent.getStringExtra("senderUid");
            final String action = remoteIntent.getStringExtra("action");
            final String chatRoom = remoteIntent.getStringExtra("chatRoom");
            if (action != null && action.equals("request_to_connect")) {
                final Bundle bundle = UserHelper.bundleNotification(remoteIntent);

                dataRepository.findUserById(senderUid, new DataSource.OnUserCallback() {

                    @Override
                    public void onSuccess(User user) {
                        if (user != null) {
                            bundle.putParcelable(Properties.BUNDLE_KEY_USER, Parcels.wrap(user));
                            if (action.equals("request_to_connect_accepted")) {
                                Fragment fragment = ((BaseView) getFragment(UserDetailFragment.class, senderUid));
                                if (fragment != null) {
                                    ((BaseView) fragment).onConnectionAccepted(user, chatRoom);
                                }
                                showFragment(MMessagesFragment.class, chatRoom, bundle, false);
                            }
                            else {
                                showFragment(UserDetailFragment.class, senderUid, bundle, false);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        //showFragment(MMessagesFragment.class, bundle, true);
                        Toast.makeText(MainActivity.this, "Could not find the user. Maybe they left?", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNetworkFailure() {
                        //showFragment(MMessagesFragment.class, bundle, true);
                        Toast.makeText(MainActivity.this, "Network failure ...", Toast.LENGTH_LONG).show();
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
                            showFragment(MMessagesFragment.class, bundle, false);
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        showFragment(MMessagesFragment.class, bundle, false);
                    }

                    @Override
                    public void onNetworkFailure() {
                        showFragment(MMessagesFragment.class, bundle, false);
                    }
                });
            }
            showFragment(UsersFragment.class, null, false);
        }
        else
            showFragment(UsersFragment.class, null, false);
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(connectivityBroadcastReceiver, new IntentFilter(CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStart() {
        super.onStart();
        setUpNotificationReceiver();
        continueSetup();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(connectivityBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //dataRepository.disconnect(this);
        if (br != null)
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
    void setUpNotificationReceiver() {
        if (br == null || !isNotificationSetup) {
            br = new BroadcastReceiver() {
                @Override
                public void onReceive(final Context context, final Intent intent) {
                    String myUid = intent.getStringExtra("receiverUid");
                    final String senderUid = intent.getStringExtra("senderUid");
                    FirebaseUser me = FirebaseAuth.getInstance().getCurrentUser();
                    final String action = intent.getStringExtra("action");
                    final String chatRoom = intent.getStringExtra("chatRoom");
                    final Bundle bundle = UserHelper.bundleNotification(intent);
                    //FirebaseHelper.deleteFirebaseNode("/notifications/" +myUid, senderUid);
                    if (action.equals("request_to_connect") || action.equals("request_to_connect_accepted")) {
                        dataRepository.findUserById(senderUid, new DataSource.OnUserCallback() {
                            @Override
                            public void onSuccess(User user) {
                                if (user != null) {
                                    bundle.putParcelable(Properties.BUNDLE_KEY_USER, Parcels.wrap(user));
                                    if (action.equals("request_to_connect_accepted")) {
                                        Fragment fragment = ((BaseView) getFragment(UserDetailFragment.class, senderUid));
                                        if (fragment != null) {
                                            ((BaseView) fragment).onConnectionAccepted(user, chatRoom);
                                        }
                                        showFragment(MMessagesFragment.class, chatRoom, bundle, true);
                                    } else {
                                        showFragment(UserDetailFragment.class, senderUid, bundle, true);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                Fragment fragment = ((BaseView) getFragment(UserDetailFragment.class, senderUid));
                                if (fragment != null) {
                                    showFragment(UserDetailFragment.class, senderUid, bundle, true);
                                }

                            }

                            @Override
                            public void onNetworkFailure() {
                                Fragment fragment = ((BaseView) getFragment(UserDetailFragment.class, senderUid));
                                if (fragment != null) {
                                    showFragment(UserDetailFragment.class, senderUid, bundle, true);
                                }
                            }
                        });
                    } else {
                        //other actions
                    }
                }
            };

            IntentFilter filter = new IntentFilter(Properties.FCN_RECEIVED_NOTIFICATION);
            registerReceiver(br, filter);
            isNotificationSetup = true;
        }
    }
}
