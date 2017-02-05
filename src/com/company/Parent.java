package com.company;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by keyvan on 2/4/17.
 */
public class Parent extends TokenManager {
    public Parent(String username) {
        super(username);
    }

    public void addChild(Child child) {
        children.put(child.getUsername(), child);
    }

    public ArrayList<Child> getChildren() {
        return new ArrayList<Child>(children.values());
    }
}
