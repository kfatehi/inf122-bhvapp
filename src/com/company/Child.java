package com.company;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by keyvan on 2/3/17.
 */
public class Child extends User {
    private String username;
    private HashMap<String,Mode> modes = new HashMap<>();
    private ArrayList<Token> tokens = new ArrayList<>();

    public Child(String childName) {
        super(childName);
    }

    public Child(String childName, ArrayList<String> modeNames) {
        super(childName);
        modeNames.forEach(this::setMode);
    }

    public void setMode(String name) {
        if (name.equals("positive"))
            setMode(name, new Positive());
        else if (name.equals("negative"))
            setMode(name, new Negative());
        else
            throw new Error("unsupported mode "+name);
    }

    public void setMode(String modeName, Mode mode) {
        modes.put(modeName, mode);
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public HashMap<String, Mode> getModes() {
        return modes;
    }
}
