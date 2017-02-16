package com.company;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class Main {

    static User currentUser;
    static UserInterface userInterface;
    private static KeyValueStore db;

    public static void main(String[] args) {
        db = new KeyValueStore(args[0]);
        loadState();
        userInterface.start();
    }

    private static void loadState() {
        try {
            db.load();
            loadUserInterface();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String join(Stream<String> iter) {
        return String.join(",", iter.toArray(String[]::new));
    }

    private static ArrayList<String> getListFromProps(String key) {
        return new ArrayList<>(Arrays.asList(db.get(key, "").split(",")));
    }

    private static void loadUserInterface() {
        String uiMode = db.get("ui", "text");
        if (uiMode.equals("text")) {
            userInterface = new TextUserInterface();
        } else {
            throw new Error("UI mode not implemented: "+uiMode);
        }
    }

    public static String getUserType(String username) {
        return db.get("users."+username+".type");
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
        child.setRedemption(db.get("child."+name+".redemptionAmount", "0"));

        getListFromProps("tokens."+name).forEach(id->{
            String note = db.get("token."+name+"."+id+".note");
            String time = db.get("token."+name+"."+id+".time");
            Date date = new Date();
            date.setTime(Long.parseLong(time));
            child.addToken(date, note);
        });
        return child;
    }

    private static void saveChild(Child child) {
        String name = child.getUsername();
        db.set("users." + name + ".type", "Child");

        String modeNames = join(child.getModes().keySet().stream());
        db.set("child."+name+".modes", modeNames);
        db.set("child."+name+".redemptionAmount", String.valueOf(child.getRedemptionAmount()));

        HashMap<UUID,Token> tokens = child.getTokens();
        String tokenIds = join(tokens.keySet().stream().map(UUID::toString));
        db.set("tokens."+name, tokenIds);

        tokens.forEach((id, token) -> {
            String time = String.valueOf(token.getDate().getTime());
            db.set("token."+name+"."+id+".note", token.viewNote());
            db.set("token."+name+"."+id+".time", time);
        });
    }

    public static void saveState() {
        try {
            String userType = currentUser.getClass().getSimpleName();
            if (userType.equals("Parent")) {
                Parent parent = (Parent) currentUser;

                HashMap<String,Child> children = parent.getChildren();

                String childNames = join(children.keySet().stream());
                db.set("users."+parent.getUsername()+".children", childNames);

                children.values().forEach(Main::saveChild);

            } else if (userType.equals("Child")) {
                saveChild((Child) currentUser);
            }
            db.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logout() {
        currentUser = null;
    }

    public static void redeemChildTokens(Child child) {
        String name = child.getUsername();
        child.redeemTokens().stream().map(UUID::toString).forEach(id->{
            db.delete("token."+name+"."+id+".time");
            db.delete("token."+name+"."+id+".note");
        });
    }
}
