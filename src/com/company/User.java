package com.company;

/**
 * Created by keyvan on 2/3/17.
 */
public abstract class User {
    private String username;

    public User(String _username) {
        username = _username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public abstract void sync(KeyValueStore db);
}
