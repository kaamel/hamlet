package com.genesis.hamlet.ui.users;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.genesis.hamlet.R;
import com.genesis.hamlet.data.DataRepository;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.di.Injection;
import com.genesis.hamlet.ui.userdetail.UserDetailFragment;
import com.genesis.hamlet.util.BaseFragmentInteractionListener;
import com.genesis.hamlet.util.EndlessRecyclerViewScrollListener;
import com.genesis.hamlet.util.ItemClickSupport;
import com.genesis.hamlet.util.Properties;
import com.genesis.hamlet.util.mvp.BaseView;
import com.genesis.hamlet.util.threading.MainUiThread;
import com.genesis.hamlet.util.threading.ThreadExecutor;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;


/**
 * The {@link Fragment} that receives photo data from its {@link UsersContract.Presenter} and
 * renders a list of photos and also handles user actions, such as clicks on photos,
 * and passes it to its {@link UsersContract.Presenter}.
 */
public class UsersFragment extends BaseView implements UsersContract.View {

    private UsersRecyclerAdapter recyclerAdapter;
    private List<User> users;
    private EndlessRecyclerViewScrollListener endlessScrollListener;
    private UsersContract.Presenter presenter;
    private BaseFragmentInteractionListener fragmentInteractionListener;
    private boolean shouldRefreshUsers;

    RecyclerView rvUsers;
    TextView tvPlaceholder;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        users = new ArrayList<>();
        ThreadExecutor threadExecutor = ThreadExecutor.getInstance();
        MainUiThread mainUiThread = MainUiThread.getInstance();
        DataRepository dataRepository = Injection.provideDataRepository();
        presenter = new UsersPresenter(this, dataRepository, threadExecutor, mainUiThread);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        rvUsers = (RecyclerView) view.findViewById(R.id.rvUsers);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        recyclerAdapter = new UsersRecyclerAdapter(this, users);
        rvUsers.setAdapter(recyclerAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvUsers.setLayoutManager(linearLayoutManager);

        endlessScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager,
                0) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                getUsers(page);
            }
        };

        rvUsers.addOnScrollListener(endlessScrollListener);

        ItemClickSupport.addTo(rvUsers).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        showDetailFragment(position);
                    }
                });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshUsers();
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary);

        getUsers(0);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentInteractionListener = (BaseFragmentInteractionListener) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showUsers(List<User> users) {
        if (shouldRefreshUsers) {
            recyclerAdapter.clear();
            endlessScrollListener.resetState();
            shouldRefreshUsers = false;
        }
        recyclerAdapter.addAll(users);
    }

    @Override
    public void showUser(User user) {
        if (shouldRefreshUsers) {
            recyclerAdapter.clear();
            endlessScrollListener.resetState();
            shouldRefreshUsers = false;
        }
        if (user != null)
            recyclerAdapter.add(user);
    }

    @Override
    public void shouldShowPlaceholderText() {

    }

    @Override
    public void remove(User user) {
        if (shouldRefreshUsers) {
            recyclerAdapter.clear();
            endlessScrollListener.resetState();
            shouldRefreshUsers = false;
        }
        recyclerAdapter.remove(user);
    }

    private void getUsers(int page) {
        presenter.getUsers(getContext().getApplicationContext(), page);
    }

    private void refreshUsers() {
        shouldRefreshUsers = true;
        recyclerAdapter.clear();
        getUsers(0);
    }

    @Override
    public void setProgressBar(boolean show) {
        swipeRefreshLayout.setRefreshing(show);
    }

    private void showDetailFragment(int userPosition) {
        User user = users.get(userPosition);
        Parcelable parcelable = Parcels.wrap(user);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Properties.BUNDLE_KEY_PHOTO, parcelable);
        fragmentInteractionListener.showFragment(UserDetailFragment.class, bundle,
                true);
    }

}