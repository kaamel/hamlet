package com.genesis.hamlet.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.genesis.hamlet.R;
import com.genesis.hamlet.ui.users.UsersFragment;
import com.genesis.hamlet.util.BaseFragmentInteractionListener;
import com.genesis.hamlet.util.FoaBaseActivity;
import com.genesis.hamlet.util.NetworkHelper;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        showFragment(UsersFragment.class);
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(connectivityBroadcastReceiver, new IntentFilter(CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        unregisterReceiver(connectivityBroadcastReceiver);
        super.onPause();
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


    @Override
    public void resetToolBarScroll() {
        appBarLayout.setExpanded(true, true);
    }
}
