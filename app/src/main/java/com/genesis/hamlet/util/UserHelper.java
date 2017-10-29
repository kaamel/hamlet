package com.genesis.hamlet.util;

import android.content.Intent;
import android.os.Bundle;

import com.genesis.hamlet.data.models.user.User;
import com.google.firebase.auth.FirebaseUser;

import org.parceler.Parcels;

/**
 * Created by kaamel on 10/14/17.
 */

public class UserHelper {

    public static User extractUser(FirebaseUser firebaseUser) {
        User user = new User();
        user.setDisplayName(firebaseUser.getDisplayName());
        user.setEmail(firebaseUser.getEmail());
        user.setPhoneNumber(firebaseUser.getPhoneNumber());
        user.setUid(firebaseUser.getUid());
        user.setPhotoUrl(firebaseUser.getPhotoUrl() != null? firebaseUser.getPhotoUrl().toString():null);
        return user;
    }

    public static Bundle bundleNotification(Intent remoteIntent) {
        String senderUid = remoteIntent.getStringExtra("senderUid");
        String action = remoteIntent.getStringExtra("action");
        String chatRoom = remoteIntent.getStringExtra("chatRoom");
        String name = remoteIntent.getStringExtra("name");
        String title = remoteIntent.getStringExtra("title");
        String message = remoteIntent.getStringExtra("message");
        String profileUrl = remoteIntent.getStringExtra("profileUrl");

        final Bundle bundle = new Bundle();
        bundle.putString("action", action);
        bundle.putString("name", name);
        bundle.putString("message", message);
        bundle.putString("profileUrl", profileUrl);
        bundle.putString("chatRoom", chatRoom);

        User user = new User();

        user.setUid(senderUid);
        user.setDisplayName(name);
        user.setIntroTitle(title);
        user.setIntroDetail(message);
        user.setPhotoUrl(profileUrl);
        bundle.putParcelable(Properties.BUNDLE_KEY_USER, Parcels.wrap(user));

        return bundle;
    }
}
