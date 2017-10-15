package com.genesis.hamlet.data;

import android.content.Context;

import com.genesis.hamlet.data.local.LocalDataSource;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.data.remote.RemoteDataSource;
import com.genesis.hamlet.util.mvp.BasePresenter;

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
    }

    public User getLoggedInUser() {
        return localDataSource.getLoggedInUser();
    }

    public void getUsers(Context context, final DataSource.GetUsersCallback getUsersCallback) {

        localDataSource.getUsers(context, getUsersCallback, 0);
        /*
        if (NetworkHelper.getInstance().isNetworkAvailable(context)) {
            remoteDataSource.getUsers(context, new DataSource.GetUsersCallback() {
                @Override
                public void onSuccess(List<User> users) {
                    getUsersCallback.onSuccess(users);
                    ((LocalDataSource) localDataSource).storeUsers(users);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    getUsersCallback.onFailure(throwable);
                }

                @Override
                public void onNetworkFailure() {
                    getUsersCallback.onNetworkFailure();
                }
            }, 0);
        } else {
            localDataSource.getUsers(context, getUsersCallback, 0);
        }
        */

        //// TODO: 10/14/17 the above for now just the users from local - this is not the final implementation
    }

}
