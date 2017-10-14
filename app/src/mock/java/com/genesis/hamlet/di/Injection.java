package com.genesis.hamlet.di;

import com.genesis.hamlet.data.DataRepository;
import com.genesis.hamlet.data.local.LocalDataSource;
import com.genesis.hamlet.data.remote.FakeCloudStorageDataSource;
import com.genesis.hamlet.util.threading.MainUiThread;
import com.genesis.hamlet.util.threading.ThreadExecutor;


public class Injection {

    public static DataRepository provideDataRepository() {
        MainUiThread mainUiThread = MainUiThread.getInstance();
        ThreadExecutor threadExecutor = ThreadExecutor.getInstance();
        return DataRepository.getInstance(
                FakeCloudStorageDataSource.getInstance(mainUiThread, threadExecutor),
                LocalDataSource.getInstance(mainUiThread, threadExecutor));
    }
}
