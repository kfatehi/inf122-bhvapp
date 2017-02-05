package com.company;

import java.util.ArrayList;

/**
 * Created by keyvan on 2/3/17.
 */
public class Child extends User {
    private String username;
    private ArrayList<Mode> modes = new ArrayList<>();
    private ArrayList<Token> tokens = new ArrayList<>();

    public Child(String childName, ArrayList<String> modeStrings) {
        super(childName);
        setUsername(childName);
        modeStrings.forEach((name)->{
            if (name.equals("positive"))
                addMode(new Positive());
            if (name.equals("negative"))
                addMode(new Negative());
        });
    }

    public Child(String childName) {
        super(childName);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // XXX added this to be able to add a mode
    public void addMode(Mode mode) {
        modes.add(mode);
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }
}
