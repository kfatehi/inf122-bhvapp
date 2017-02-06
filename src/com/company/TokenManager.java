package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by keyvan on 2/4/17.
 */
public class TokenManager extends User {
    protected HashMap<String,Child> children = new HashMap<>();

    public TokenManager(String username) {
        super(username);
    }

    public HashMap<String,Child> getChildren() {
        return children;
    }

    public void addToken(String childName) {
        children.get(childName).addToken();
    }

    public void addToken(String childName, String note) {
        children.get(childName).addToken(note);
    }
}
