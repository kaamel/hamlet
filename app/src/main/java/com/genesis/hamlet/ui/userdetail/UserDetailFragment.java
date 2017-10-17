package com.genesis.hamlet.ui.userdetail;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.genesis.hamlet.R;
import com.genesis.hamlet.UserChatFragment;
import com.genesis.hamlet.data.DataRepository;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.di.Injection;
import com.genesis.hamlet.ui.users.UsersFragment;
import com.genesis.hamlet.ui.users.UsersPresenter;
import com.genesis.hamlet.ui.users.UsersRecyclerAdapter;
import com.genesis.hamlet.util.BaseFragmentInteractionListener;
import com.genesis.hamlet.util.EndlessRecyclerViewScrollListener;
import com.genesis.hamlet.util.ItemClickSupport;
import com.genesis.hamlet.util.Properties;
import com.genesis.hamlet.util.mvp.BaseView;
import com.genesis.hamlet.util.threading.MainUiThread;
import com.genesis.hamlet.util.threading.ThreadExecutor;

import org.parceler.Parcels;

import java.util.ArrayList;

import static com.genesis.hamlet.R.id.rvUsers;
import static com.genesis.hamlet.R.id.swipeRefreshLayout;

/**
 * The {@link Fragment} that receives photo data from
 * {@link UsersFragment}
 * via a {@link Bundle} and comment data from its
 * {@link UserDetailContract.Presenter}. It then renders the photo and its list of comments.
 */
public class UserDetailFragment extends BaseView implements UserDetailContract.View {


    private BaseFragmentInteractionListener fragmentInteractionListener;
    private User user;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = Parcels.unwrap(getActivity().getIntent().getParcelableExtra(Properties.BUNDLE_KEY_PHOTO));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_detail, container, false);


        Button button = (Button) view.findViewById(R.id.btnConnected);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserChatFragment(user);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


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

    private void showUserChatFragment(User user) {
        Parcelable parcelable = Parcels.wrap(user);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Properties.BUNDLE_KEY_PHOTO, parcelable);
        fragmentInteractionListener.showFragment(UserChatFragment.class, bundle,
                true);
    }

}
