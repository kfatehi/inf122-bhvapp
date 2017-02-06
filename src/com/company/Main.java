package com.company;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Stream;

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

    private static String join(Stream<String> iter) {
        return String.join(",", iter.toArray(String[]::new));
    }

    private static void loadProps() throws IOException {
        props.load(new FileReader(propsFile));
    }

    private static void saveProps() throws IOException {
        props.store(new FileOutputStream(propsFile), null);
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

    private static void loadChildren(Parent parent) {
        getListFromProps("users."+currentUser.getUsername()+".children").forEach((name)->{
            if (name.length() > 0) {
                Child child = new Child(name);

                getListFromProps("child."+name+".modes").forEach(child::setMode);
                child.setRedemption(props.getProperty("child."+name+".redemptionAmount", "0"));

                String tokAmount = props.getProperty("child."+name+".tokenCount", "0");
                for (int i = 0; i < Integer.parseInt(tokAmount); i++) child.addToken();

                parent.addChild(child);
            }
        });
    }

    public static void saveState() {
        try {
            String userType = currentUser.getClass().getSimpleName();
            if (userType.equals("Parent")) {
                Parent parent = (Parent) currentUser;

                HashMap<String,Child> children = parent.getChildren();
                children.forEach((name, child) -> {
                    props.setProperty("users." + name + ".type", "Child");

                    String modeNames = join(child.getModes().keySet().stream());
                    props.setProperty("child."+name+".modes", modeNames);
                    props.setProperty("child."+name+".redemptionAmount", String.valueOf(child.getRedemptionAmount()));
                    props.setProperty("child."+name+".tokenCount", String.valueOf(child.getTokens().size()));
                });

                String childNames = join(children.keySet().stream());
                props.setProperty("users."+parent.getUsername()+".children", childNames);

            } else if (userType.equals("Child")) {

            }
            saveProps();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
