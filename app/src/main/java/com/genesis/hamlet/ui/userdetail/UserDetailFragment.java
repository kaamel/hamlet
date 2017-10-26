package com.genesis.hamlet.ui.userdetail;

import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.genesis.hamlet.R;
import com.genesis.hamlet.data.models.interests.Interests;
import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.ui.mmessages.MMessagesFragment;
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


    private static BaseFragmentInteractionListener fragmentInteractionListener;
    private TextView tvUserName;
    private TextView tvIntroTitle;
    private TextView tvIntroDetail;
    private ImageView userProfile;

    Button connectButton;
    Button endtButton;
    Button acceptButton;
    Button ignoreButton;
    static LinearLayout normalLL;
    static LinearLayout remoteLL;

    User user;

    private static boolean setButtonsToNormal = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_detail, container, false);

        Parcelable parcelable = getArguments().getParcelable(Properties.BUNDLE_KEY_USER);
        final String chatRoom = getArguments().getString("chatRoom");
        String myId = getArguments().getString("myId");

        user = Parcels.unwrap(parcelable);

        userProfile = (ImageView) view.findViewById(R.id.ivPhoto);

        tvIntroTitle = (TextView) view.findViewById(R.id.tvIntroTitleDetail);

        tvIntroDetail = (TextView) view.findViewById(R.id.tvDetailMessage);

        tvUserName = (TextView) view.findViewById(R.id.tvUserName);


        connectButton = (Button) view.findViewById(R.id.btnConnected);
        endtButton = (Button) view.findViewById(R.id.btnEnd);
        acceptButton = (Button) view.findViewById(R.id.btnAccept);
        ignoreButton = (Button) view.findViewById(R.id.btnIgnore);

        normalLL = (LinearLayout) view.findViewById(R.id.normalDetail);
        remoteLL = (LinearLayout) view.findViewById(R.id.remoteDetail);

        if (chatRoom != null) {
            //show remote user
            setViewRemote(user);
        }
        else {
            //normal details
            setViewNormal(user);
        }

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestToConnet(user);
                connectButton.setClickable(false);
                endtButton.setVisibility(View.VISIBLE);
            }
        });

        endtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectButton.setClickable(true);
                endtButton.setVisibility(View.GONE);
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToChatView(chatRoom, user);
                connectButton.setClickable(true);
                endtButton.setVisibility(View.GONE);
                fragmentInteractionListener.getDataRepository().
                        sendNotification(user,
                                "request_to_connect_accepted",
                                Interests.getInstance().getIntroTitle(),
                                Interests.getInstance().getIntroDetail());
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
        if (setButtonsToNormal) {
            setButtonsToNormal = false;
            connectButton.setClickable(false);
            endtButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onResume() {
        super.onResume();
        fragmentInteractionListener.setTitle("User Details");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void displayUserInfo(@Nullable User user) {

        if (!TextUtils.isEmpty(user.getPhotoUrl())) {
            Glide.with(getActivity()).load(user.getPhotoUrl()).into(userProfile);
        }

        tvUserName.setText(user.getDisplayName());
        tvIntroTitle.setText(user.getIntroTitle());
        tvIntroDetail.setText(user.getIntroDetail());
    }

    private void requestToConnet(User user) {
        fragmentInteractionListener.getDataRepository().
                sendNotification(user,
                        "request_to_connect",
                        Interests.getInstance().getIntroTitle(),
                        Interests.getInstance().getIntroDetail());

    }

    public static void onConnectAccepted(String chatRoom, User otherUser) {
        setViewNormal(otherUser);
        setButtonsToNormal = true;

        switchToChatView(chatRoom, otherUser);
    }

    private static void switchToChatView(String chatRoom, User otherUser) {
        Parcelable parcelable = Parcels.wrap(otherUser);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Properties.BUNDLE_KEY_USER, parcelable);
        bundle.putString("chatRoom", chatRoom);
        fragmentInteractionListener.showFragment(MMessagesFragment.class, bundle,
                true);
    }

    private static void setViewNormal(User user) {
        remoteLL.setVisibility(View.GONE);
        normalLL.setVisibility(View.VISIBLE);
    }

    private static void setViewRemote(User user) {
        remoteLL.setVisibility(View.VISIBLE);
        normalLL.setVisibility(View.GONE);
    }
}
