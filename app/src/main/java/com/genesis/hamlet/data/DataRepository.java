package com.genesis.hamlet.data;

import android.content.Context;

import com.genesis.hamlet.data.local.LocalDataSource;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.data.remote.RemoteDataSource;
import com.genesis.hamlet.util.mvp.BasePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * The primary class for the presenters that extend
 * {@link BasePresenter} to interact with
 * for fetching and storing data.
 * It is the middleman in front of all data sources such as
 * {@link RemoteDataSource}
 * and {@link LocalDataSource} and delegates the work to
 * them depending on conditions such as network availability, etc.
 */
public class DataRepository {

    private DataSource remoteDataSource;
    private DataSource localDataSource;

    boolean streaming = false;

    private static DataRepository dataRepository;

    public DataRepository(DataSource remoteDataSource, DataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public static synchronized DataRepository getInstance(DataSource remoteDataSource,
            DataSource localDataSource) {
        if (dataRepository == null) {
            dataRepository = new DataRepository(remoteDataSource, localDataSource);
        }
        return dataRepository;
    }

    public void destroyInstance() {
        dataRepository = null;
        stopUsersStream();
        streaming = false;
    }

    public User getLoggedInUser() {
        return localDataSource.getLoggedInUser();
    }

    public void getUsers(Context context, final DataSource.GetUsersCallback getUsersCallback, int page) {

        if (!streaming) {
            streaming = true;
            List<User> users = new ArrayList<>();
            getUsersCallback.onSuccess(users);
            localDataSource.storeUsers(users);
            startUsersStream(context, getUsersCallback);
        }
        else
            localDataSource.getUsers(context, getUsersCallback, page);
    }

    private void startUsersStream(Context context, DataSource.GetUsersCallback getUsersCallback) {
        remoteDataSource.getUsers(context, getUsersCallback, 0);
    }

    public void stopUsersStream() {
        //// TODO: 10/15/17 close down the firebase and do any clean up on the server side
    }

}
