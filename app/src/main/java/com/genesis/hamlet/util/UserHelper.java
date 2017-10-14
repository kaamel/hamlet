package com.genesis.hamlet.util;

import com.genesis.hamlet.data.models.user.User;
import com.google.firebase.auth.FirebaseUser;

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
}
