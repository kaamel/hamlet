
package com.genesis.hamlet.data.models.user;


import java.util.List;

public class Response {
    private List<User> users;
    private User user;
    private String status;

    public Response(String status, User user, List<User> users) {
        this.status = status;
        this.user = user;
        this.users = users;
    }

    public User getUser() {
        return user;
    }
    public List<User> getUsers() {
        return users;
    }
    public String getStat() {
        return status;
    }
}
