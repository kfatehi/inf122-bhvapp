package com.company;

import java.util.ArrayList;

/**
 * Created by keyvan on 2/3/17.
 */
public class ChildData {
    private ArrayList<Mode> modes = new ArrayList<>();

    // XXX added this to be able to add a mode
    public void addMode(Mode mode) {
        modes.add(mode);
    }
}
