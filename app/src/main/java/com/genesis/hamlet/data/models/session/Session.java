package com.genesis.hamlet.data.models.session;

import com.genesis.hamlet.data.models.user.User;
import com.genesis.hamlet.data.models.user.Users;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kaamel on 10/13/17.
 */

public class Session {
    Users users = new Users();
    Map<User, Long> joinedTimes = new HashMap<>();
}
