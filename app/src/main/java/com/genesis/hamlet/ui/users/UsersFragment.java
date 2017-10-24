package com.genesis.hamlet.ui.users;

import android.content.Context;
import android.content.Intent;
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

import com.genesis.hamlet.R;
import com.genesis.hamlet.data.DataRepository;
import com.genesis.hamlet.data.models.interests.Interests;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.ui.interests.InterestsFragment;
import com.genesis.hamlet.ui.login.LoginActivity;
import com.genesis.hamlet.ui.userdetail.UserDetailFragment;
import com.genesis.hamlet.util.BaseFragmentInteractionListener;
import com.genesis.hamlet.util.EndlessRecyclerViewScrollListener;
import com.genesis.hamlet.util.ItemClickSupport;
import com.genesis.hamlet.util.Properties;
import com.genesis.hamlet.util.mvp.BaseView;
import com.genesis.hamlet.util.threading.MainUiThread;
import com.genesis.hamlet.util.threading.ThreadExecutor;
import com.google.firebase.auth.FirebaseAuth;

import org.parceler.Parcels;

import java.util.List;


/**
 * The {@link Fragment} that receives photo data from its {@link UsersContract.Presenter} and
 * renders a list of photos and also handles user actions, such as clicks on photos,
 * and passes it to its {@link UsersContract.Presenter}.
 */
public class UsersFragment extends BaseView implements UsersContract.View {

    private UsersRecyclerAdapter recyclerAdapter;
    private EndlessRecyclerViewScrollListener endlessScrollListener;
    private UsersContract.Presenter presenter;
    private BaseFragmentInteractionListener fragmentInteractionListener;
    private boolean shouldRefreshUsers;

    DataRepository dataRepository;

    RecyclerView rvUsers;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupRepository();
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    private void setupRepository() {
        ThreadExecutor threadExecutor = ThreadExecutor.getInstance();
        MainUiThread mainUiThread = MainUiThread.getInstance();
        dataRepository = fragmentInteractionListener.getDataRepository();
        presenter = new UsersPresenter(this, dataRepository, threadExecutor, mainUiThread);
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

        recyclerAdapter = new UsersRecyclerAdapter();
        rvUsers.setAdapter(recyclerAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvUsers.setLayoutManager(linearLayoutManager);

        endlessScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager,
                0) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                presenter.loadMoreUsers(getContext(), page);
            }
        };

        rvUsers.addOnScrollListener(endlessScrollListener);

        ItemClickSupport.addTo(rvUsers).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        showDetailFragment(recyclerAdapter.getItem(position));
                    }
                });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshUsers();
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary);

        //goOnline(0);

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
        Interests interests = Interests.getInstance();
        if (interests == null || !interests.isIncomplete()) {
            setUpInterests();
        }
        else if (presenter.isConnected()) {
            if (interests.isChanged()) {
                interests.setChanged(false);
                refreshUsers();
            }
        }
        else {
            presenter.connect(getContext());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        dataRepository.disconnect(getContext());
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_users, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("Sign Out")) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
            return true;
        }

        if (item.getTitle().equals("Clear Authentication")) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.putExtra("action", "revoke");
            startActivity(intent);
            getActivity().finish();
            return true;
        }

        if (item.getTitle().equals("Interests")) {
            setUpInterests();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpInterests() {
        fragmentInteractionListener.showFragment(InterestsFragment.class, new Bundle(), true);
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
    public void addUser(User user) {
        if (shouldRefreshUsers) {
            recyclerAdapter.clear();
            endlessScrollListener.resetState();
            shouldRefreshUsers = false;
        }
        if (user != null)
            recyclerAdapter.add(user);
    }

    @Override
    public void updateUser(User user) {
        if (shouldRefreshUsers) {
            recyclerAdapter.clear();
            endlessScrollListener.resetState();
            shouldRefreshUsers = false;
        }
        if (user != null)
            recyclerAdapter.update(user);
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

    private void refreshUsers() {
        shouldRefreshUsers = false;
        recyclerAdapter.clear();
        dataRepository.destroyInstance(getContext());
        setupRepository();
        presenter.connect(getContext());
    }

    @Override
    public void setProgressBar(boolean show) {
        swipeRefreshLayout.setRefreshing(show);
    }

    private void showDetailFragment(User user) {
        Parcelable parcelable = Parcels.wrap(user);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Properties.BUNDLE_KEY_USER, parcelable);
        fragmentInteractionListener.showFragment(UserDetailFragment.class, bundle,
                true);
    }

}