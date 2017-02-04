package com.company;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class Main {

    static User currentUser;
    static ArrayList<User> children = new ArrayList<>(); // XXX changed this from "users" to "children" to make it more semantic
    static UserInterface userInterface; // XXX had to change this due to "interface" being a reserved word in the IDE
    private static String propsFile;
    static Properties props = new Properties();

    public static void main(String[] args) {
        try {
            loadProps(args[0]);
            loadUserInterface(props);
            loadChildren(props);
            userInterface.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadProps(String _propsFile) throws IOException {
        propsFile = _propsFile;
        props.load(new FileReader(propsFile));
    }

    public static void saveProps() throws IOException {
        props.store(new FileOutputStream(propsFile), null);
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
}
