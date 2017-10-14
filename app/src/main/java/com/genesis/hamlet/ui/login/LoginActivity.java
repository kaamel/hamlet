package com.genesis.hamlet.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.genesis.hamlet.R;
import com.genesis.hamlet.HamletApplication;
import com.genesis.hamlet.data.DataRepository;
import com.genesis.hamlet.data.DataSource;
import com.genesis.hamlet.data.local.LocalDataSource;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.data.remote.RemoteDataSource;
import com.genesis.hamlet.ui.MainActivity;
import com.genesis.hamlet.util.threading.MainUiThread;
import com.genesis.hamlet.util.threading.ThreadExecutor;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        DataRepository dataRepository = DataRepository.getInstance(
                new RemoteDataSource(MainUiThread.getInstance(), ThreadExecutor.getInstance()),
                new LocalDataSource(MainUiThread.getInstance(), ThreadExecutor.getInstance()));

        if (HamletApplication.getMe() == null) {
            HamletApplication.setMe(dataRepository.getLoggedInUser());
        }
        if (HamletApplication.getMe() == null)
            loginUser(new DataSource.GetUserCallback() {

                @Override
                public void onSuccess(User user) {
                    HamletApplication.setMe(user);
                    continueToUsersList();
                }

                @Override
                public void onFailure(Throwable throwable) {
                    //// TODO: 10/13/17 report login failure and allow user to exit or take corrective action
                }

                @Override
                public void onNetworkFailure() {
                    //// TODO: 10/13/17 report the network failure and offer the user to exit or take corrective action
                }
            });
    }

    // When the login is successful we should get a callback to onSuccess
    void loginUser(DataSource.GetUserCallback getUserCallback) {
        //// TODO: 10/13/17 Start an activity which upon completion a callback to onSuccess or onFailure will be made
    }

    private void continueToUsersList() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //// TODO: 10/13/17 remove this & the related button after implementing the login activity;
    //// TODO: 10/13/17 for now it allows to move on for testing
    public void onContinueWithoutLogin(View view) {
        continueToUsersList();
    }
}
