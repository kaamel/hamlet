package com.genesis.hamlet.ui.userdetail;

import android.content.Context;

import com.genesis.hamlet.data.DataRepository;
import com.genesis.hamlet.util.threading.MainUiThread;
import com.genesis.hamlet.util.threading.ThreadExecutor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailPresenterTest {

    @Mock
    private UserDetailContract.View mockView;

    @Mock
    private DataRepository mockDataRepository;

    @Mock
    private ThreadExecutor mockThreadExecutor;

    @Mock
    private MainUiThread mockMainUiThread;

    @Mock
    private Context mockContext;
/*
    @Captor
    private ArgumentCaptor<DataSource.GetCommentsCallback> getCommentsCallbackCaptor;

    private PhotoDetailPresenter photoDetailPresenter;
    private User user;

    @Before
    public void setup() {

        photoDetailPresenter = new PhotoDetailPresenter(mockView, mockDataRepository,
                mockThreadExecutor, mockMainUiThread);
        user = new User();

    }
*/
    @Test
    public void getComments_testWithActiveView() {

/*        photoDetailPresenter.getComments(mockContext, user);

        verify(mockView).setProgressBar(true);
        verify(mockDataRepository).getComments(eq(mockContext), eq(user),
                getCommentsCallbackCaptor.capture());

        DataSource.GetCommentsCallback getCommentsCallback = getCommentsCallbackCaptor.getValue();

        List<Comment> comments = new ArrayList<>();
        photoDetailPresenter.onViewActive(mockView);
        getCommentsCallback.onSuccess(comments);
        verify(mockView).showComments(comments);
        verify(mockView).setProgressBar(false); */
    }

    @Test
    public void getComments_testWithNonActiveView() {

/*        photoDetailPresenter.getComments(mockContext, user);

        verify(mockView).setProgressBar(true);
        verify(mockDataRepository).getComments(eq(mockContext), eq(user),
                getCommentsCallbackCaptor.capture());

        DataSource.GetCommentsCallback getCommentsCallback = getCommentsCallbackCaptor.getValue();

        List<Comment> comments = new ArrayList<>();
        photoDetailPresenter.onViewInactive();
        getCommentsCallback.onSuccess(comments);
        verify(mockView, never()).showComments(comments);
        verify(mockView, never()).setProgressBar(false); */
    }
}
