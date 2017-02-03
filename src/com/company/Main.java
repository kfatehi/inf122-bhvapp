package com.company;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class Main {

    static User currentUser;
    static ArrayList<User> children = new ArrayList<>(); // XXX changed this from "users" to "children" to make it more semantic
    static UserInterface userInterface; // XXX had to change this due to "interface" being a reserved word in the IDE

    public static void main(String[] args) {
        Properties props = new Properties();
        try {
            props.load(new FileReader(args[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentUser = new User(props.getProperty("user", "user"));
        loadUserInterface(props);
        loadChildren(props);
        while (String cmd = userInterface.getCommand()) {

        }
    }

    private static void loadChildren(Properties props) {
        new ArrayList<>(Arrays.asList(props.getProperty("children","").split(","))).forEach((childName)->{
            if (childName.length() > 0) {
                ArrayList<String> modes = new ArrayList<>(Arrays.asList(props.getProperty("child." + childName + ".modes", "").split(",")));
                System.out.println("loading child: " + childName);
                children.add(new Child(childName, modes));
            }
        });
    }

    private static void loadUserInterface(Properties props) {
        String uiMode = props.getProperty("ui", "text");
        if (uiMode.equals("text")) {
            userInterface = new TextUserInterface();
        } else {
            throw new Error("UI mode not implemented: "+uiMode);
        }
    }

    void parseCommand() {

    }
}
