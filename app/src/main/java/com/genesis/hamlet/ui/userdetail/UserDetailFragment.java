package com.genesis.hamlet.ui.userdetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.genesis.hamlet.R;
import com.genesis.hamlet.data.models.interests.Interests;
import com.genesis.hamlet.ui.mmessages.MMessagesFragment;
import com.genesis.hamlet.ui.mmessages.MessagesActivity;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.ui.users.UsersFragment;
import com.genesis.hamlet.util.BaseFragmentInteractionListener;
import com.genesis.hamlet.util.Properties;
import com.genesis.hamlet.util.mvp.BaseView;

import org.parceler.Parcels;

/**
 * The {@link Fragment} that receives photo data from
 * {@link UsersFragment}
 * via a {@link Bundle} and comment data from its
 * {@link UserDetailContract.Presenter}. It then renders the photo and its list of comments.
 */
public class UserDetailFragment extends BaseView implements UserDetailContract.View {


    private BaseFragmentInteractionListener fragmentInteractionListener;
    private TextView userName;
    private TextView tagLine;
    private TextView currentInterest;
    private ImageView userProfile;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_detail, container, false);

        Parcelable parcelable = getArguments().getParcelable(Properties.BUNDLE_KEY_USER);

        final User user = Parcels.unwrap(parcelable);

        userProfile = (ImageView) view.findViewById(R.id.ivPhoto);

        tagLine = (TextView) view.findViewById(R.id.tvTagline);

        currentInterest = (TextView) view.findViewById(R.id.tvDetailMessage);

        userName = (TextView) view.findViewById(R.id.tvUserName);


        Button button = (Button) view.findViewById(R.id.btnConnected);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserChatFragment(user);
            }
        });

        displayUserInfo(user);

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

    private void displayUserInfo(@Nullable User user) {

        if (!TextUtils.isEmpty(user.getPhotoUrl())) {
            Glide.with(getActivity()).load(user.getPhotoUrl()).into(userProfile);
        }

        userName.setText(user.getDisplayName());
        tagLine.setText(user.getIntroTitle());
        currentInterest.setText(user.getUid());
    }

    private void showUserChatFragment(User user) {
        fragmentInteractionListener.getDataRepository().sendNotification(user, "request_to_connect", Interests.getInstance().getIntroTitle(), Interests.getInstance().getIntroDetail());
        Parcelable parcelable = Parcels.wrap(user);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Properties.BUNDLE_KEY_USER, parcelable);
        fragmentInteractionListener.showFragment(MMessagesFragment.class, bundle,true);

//        Intent intent = new Intent(getActivity(), MessagesActivity.class);
//        intent.putExtra(Properties.BUNDLE_KEY_USER, parcelable);
//        startActivity(intent);
    }

}
