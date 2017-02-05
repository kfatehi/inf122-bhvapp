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
}
