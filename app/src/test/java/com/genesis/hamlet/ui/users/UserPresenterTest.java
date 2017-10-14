package com.genesis.hamlet.ui.users;


import android.content.Context;

import com.genesis.hamlet.data.DataRepository;
import com.genesis.hamlet.util.threading.MainUiThread;
import com.genesis.hamlet.util.threading.ThreadExecutor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserPresenterTest {

    @Mock
    private UsersContract.View mockView;

    @Mock
    private DataRepository mockDataRepository;

    @Mock
    private ThreadExecutor mockThreadExecutor;

    @Mock
    private MainUiThread mockMainUiThread;

    @Mock
    Context mockContext;
/*
    @Captor
    private ArgumentCaptor<DataSource.GetPhotosCallback> getPhotosCallbackCaptor;

    private PhotosPresenter photosPresenter;

    @Before
    public void setup() {

        photosPresenter = new PhotosPresenter(mockView, mockDataRepository, mockThreadExecutor,
                mockMainUiThread);

    }
*/
    @Test
    public void getPhotos_testWithActiveView() {
        int page = 2;
/*        photosPresenter.getPhotos(mockContext, page);

        verify(mockView).setProgressBar(true);
        verify(mockDataRepository).getPhotos(eq(mockContext), eq(page),
                getPhotosCallbackCaptor.capture());

        DataSource.GetPhotosCallback getPhotosCallback = getPhotosCallbackCaptor.getValue();
        List<User> users = new ArrayList<>();

        photosPresenter.onViewActive(mockView);
        getPhotosCallback.onSuccess(users);
        verify(mockView).showPhotos(users); */
        verify(mockView).setProgressBar(false);
    }

    @Test
    public void getPhotos_testWithNonActiveView() {
        int page = 2;
/*        photosPresenter.getPhotos(mockContext, page);

        verify(mockView).setProgressBar(true);
        verify(mockDataRepository).getPhotos(eq(mockContext), eq(page),
                getPhotosCallbackCaptor.capture());

        DataSource.GetPhotosCallback getPhotosCallback = getPhotosCallbackCaptor.getValue();
        List<User> users = new ArrayList<>();

        photosPresenter.onViewInactive();
        getPhotosCallback.onSuccess(users);
        verify(mockView, never()).showPhotos(users); */
        verify(mockView, never()).setProgressBar(false);
    }


}
