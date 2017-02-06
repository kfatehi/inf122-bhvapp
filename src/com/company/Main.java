package com.company;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
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
                currentUser = loadParent(username);
            } else if (userType.equals("Child")) {
                currentUser = loadChild(username);
            } else {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    private static Parent loadParent(String name) {
        Parent parent = new Parent(name);
        loadChildren(parent);
        return parent;
    }

    private static void loadChildren(Parent parent) {
        getListFromProps("users."+parent.getUsername()+".children").forEach((name)->{
            if (name.length() > 0) {
                Child child = loadChild(name);

                parent.addChild(child);
            }
        });
    }

    private static Child loadChild(String name) {
        Child child = new Child(name);

        getListFromProps("child."+name+".modes").forEach(child::setMode);
        child.setRedemption(props.getProperty("child."+name+".redemptionAmount", "0"));

        getListFromProps("tokens."+name).forEach(id->{
            String note = props.getProperty("token."+name+"."+id+".note");
            String time = props.getProperty("token."+name+"."+id+".time");
            Date date = new Date();
            date.setTime(Long.parseLong(time));
            child.addToken(date, note);
        });
        return child;
    }

    public static void saveState() {
        try {
            String userType = currentUser.getClass().getSimpleName();
            if (userType.equals("Parent")) {
                Parent parent = (Parent) currentUser;

                HashMap<String,Child> children = parent.getChildren();

                String childNames = join(children.keySet().stream());
                props.setProperty("users."+parent.getUsername()+".children", childNames);

                children.forEach((name, child) -> {
                    props.setProperty("users." + name + ".type", "Child");

                    String modeNames = join(child.getModes().keySet().stream());
                    props.setProperty("child."+name+".modes", modeNames);
                    props.setProperty("child."+name+".redemptionAmount", String.valueOf(child.getRedemptionAmount()));

                    HashMap<UUID,Token> tokens = child.getTokens();
                    String tokenIds = join(tokens.keySet().stream().map(id-> id.toString()));
                    props.setProperty("tokens."+name, tokenIds);

                    tokens.forEach((id, token) -> {
                        String time = String.valueOf(token.getDate().getTime());
                        props.setProperty("token."+name+"."+id+".note", token.viewNote());
                        props.setProperty("token."+name+"."+id+".time", time);
                    });
                });

            } else if (userType.equals("Child")) {

            }
            saveProps();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logout() {
        currentUser = null;
    }
}
