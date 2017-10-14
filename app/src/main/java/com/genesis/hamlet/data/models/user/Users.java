
package com.genesis.hamlet.data.models.user;

import java.util.List;

public class Users {

    List<User> users = null;
    private long usersId;

    public Users() {
    }

    public Users(List<User> users, long id ) {
        usersId = id;
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public long getUsersId() {
        return usersId;
    }

    public void setUsersId(long usersId) {
        this.usersId = usersId;
    }
}
