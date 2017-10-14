package com.genesis.hamlet.di;


import com.genesis.hamlet.data.DataRepository;
import com.genesis.hamlet.data.local.LocalDataSource;
import com.genesis.hamlet.data.remote.FirebaseCloudDataSource;
import com.genesis.hamlet.util.threading.MainUiThread;
import com.genesis.hamlet.util.threading.ThreadExecutor;

public class Injection {

    public static DataRepository provideDataRepository(MainUiThread mainUiThread,
                                                       ThreadExecutor threadExecutor) {
        return DataRepository.getInstance(
                FirebaseCloudDataSource.getInstance(mainUiThread, threadExecutor),
                LocalDataSource.getInstance(mainUiThread, threadExecutor));
    }
}
