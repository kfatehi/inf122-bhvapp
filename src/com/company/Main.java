package com.company;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;

public class Main {

    static User currentUser;
    static UserInterface userInterface;
    private static String propsFile;
    private static Properties props = new Properties();

    public static void main(String[] args) {
        propsFile = args[0];
        loadState();
        userInterface.start();
    }

    private static void loadState() {
        try {
            loadProps();
            loadUserInterface(props);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveState() {
        try {
            String userType = currentUser.getClass().getSimpleName();
            if (userType.equals("Parent")) {
                Parent parent = (Parent) currentUser;
                ArrayList<Child> children = parent.getChildren();
                children.forEach(child -> {
                    String childName = child.getUsername();
                    props.setProperty("users." + childName + ".type", "Child");
                });
                String childNames = String.join(",", children.stream().map(User::getUsername).toArray(String[]::new));
                props.setProperty("users."+parent.getUsername()+".children", childNames);
            } else if (userType.equals("Child")) {

            }
            saveProps();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadProps() throws IOException {
        props.load(new FileReader(propsFile));
    }

    private static void saveProps() throws IOException {
        props.store(new FileOutputStream(propsFile), null);
    }

    private static void loadChildren(Parent parent) {
        getListFromProps("users."+currentUser.getUsername()+".children").forEach((childName)->{
            if (childName.length() > 0) {
                ArrayList<String> modes = getListFromProps("child." + childName + ".modes");
                System.out.println("loading child: " + childName);
                parent.addChild(new Child(childName, modes));
            }
        });
    }

    private static ArrayList<String> getListFromProps(String key) {
        return new ArrayList<>(Arrays.asList(props.getProperty(key, "").split(",")));
    }

    private static void loadUserInterface(Properties props) {
        String uiMode = props.getProperty("ui", "text");
        if (uiMode.equals("text")) {
            userInterface = new TextUserInterface();
        } else {
            throw new Error("UI mode not implemented: "+uiMode);
        }
    }

    public static String getUserType(String username) {
        return props.getProperty("users."+username+".type");
    }

    public static boolean login(String username) {
        String userType = getUserType(username);
        if (userType != null) {
            if (userType.equals("Parent")) {
                currentUser = new Parent(username);
                loadChildren((Parent) currentUser);
            } else if (userType.equals("Child")) {
                currentUser = new Child(username);
            } else {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }
}
