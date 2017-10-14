
package com.genesis.hamlet.data.models.user;

public class User {
    private long id;
    private String displayName;
    private String firstName;
    private String lastName;
    private String phontId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhontId() {
        return phontId;
    }

    public void setPhontId(String phontId) {
        this.phontId = phontId;
    }
}
