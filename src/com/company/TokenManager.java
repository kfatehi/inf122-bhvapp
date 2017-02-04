package com.company;

import java.util.ArrayList;

/**
 * Created by keyvan on 2/4/17.
 */
public class TokenManager extends User {
    private ArrayList<ChildData> children = new ArrayList<>();

    public TokenManager(String username) {
        super(username);
    }

    public void addChildren(ArrayList<Child> childrenToAdd) {
        childrenToAdd.forEach(this::addChild);
    }

    // XXX added this method because you normally add a single child
    public void addChild(Child child) {
        children.add(child.getData());
    }
}
