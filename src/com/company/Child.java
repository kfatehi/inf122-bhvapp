package com.company;

import java.util.ArrayList;

/**
 * Created by keyvan on 2/3/17.
 */
public class Child extends User {
    private ChildData data = new ChildData();

    public Child(String childName, ArrayList<String> modeStrings) {
        super(childName);
        modeStrings.forEach((name)->{
            if (name.equals("positive"))
                data.addMode(new Positive());
            if (name.equals("negative"))
                data.addMode(new Negative());
        });
    }

    public Child(String childName) {
        super(childName);
    }

    public ChildData getData() {
        return data;
    }

    public ArrayList<Token> getTokens() {
        return data.getTokens();
    }
}
