package com.genesis.hamlet.data;

import android.content.Context;

import com.genesis.hamlet.data.local.LocalDataSource;
import com.genesis.hamlet.data.remote.RemoteDataSource;
import com.genesis.hamlet.util.NetworkHelper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataRepositoryTest {

    @Mock
    LocalDataSource mockLocalDataSource;

    @Mock
    RemoteDataSource mockRemoteDataSource;

    @Mock
    NetworkHelper mockNetworkHelper;

    @Mock
    Context mockContext;
/*
    @Mock
    DataSource.GetPhotosCallback mockGetPhotosCallback;

    @Captor
    ArgumentCaptor<DataSource.GetPhotosCallback> getPhotosCallbackCaptor;

    private DataRepository dataRepository;

    @Before
    public void setup() {
        dataRepository = DataRepository.getInstance(mockRemoteDataSource, mockLocalDataSource,
                mockNetworkHelper);
    }

    @After
    public void tearDown() {
        dataRepository.destroyInstance();
    }
*/
    @Test
    public void getPhotos_shouldCallRemoteDataSourceAndStoreLocally() {
        int page = 1;
        when(mockNetworkHelper.isNetworkAvailable(mockContext)).thenReturn(true);

        /*dataRepository.getPhotos(mockContext, page, mockGetPhotosCallback);

        verify(mockRemoteDataSource).getPhotos(eq(page), getPhotosCallbackCaptor.capture());

        List<User> users = new ArrayList<>();
        getPhotosCallbackCaptor.getValue().onSuccess(users);

        verify(mockLocalDataSource).storePhotos(users);
        verify(mockGetPhotosCallback).onSuccess(users); */
    }

    @Test
    public void getPhotos_shouldCallLocalDataSource() {
        /*int page = 1;
        when(mockNetworkHelper.isNetworkAvailable(mockContext)).thenReturn(false);

        dataRepository.getPhotos(mockContext, page, mockGetPhotosCallback);

        verify(mockRemoteDataSource, never()).getPhotos(eq(page),
                getPhotosCallbackCaptor.capture());
        verify(mockLocalDataSource).getPhotos(page, mockGetPhotosCallback); */
    }

}
