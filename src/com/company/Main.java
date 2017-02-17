package com.company;

import java.io.IOException;
import java.util.*;

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
        db.getList("users."+parent.getUsername()+".children").forEach((name)->{
            if (name.length() > 0) {
                Child child = loadChild(name);

                parent.addChild(child);
            }
        });
    }

    private static Child loadChild(String name) {
        Child child = new Child(name);

        db.getList("child."+name+".modes").forEach(child::setMode);
        child.setRedemption(db.get("child."+name+".redemptionAmount", "0"));

        db.getList("tokens."+name).forEach(id->{
            String note = db.get("token."+name+"."+id+".note");
            String time = db.get("token."+name+"."+id+".time");
            Date date = new Date();
            date.setTime(Long.parseLong(time));
            child.addToken(date, note);
        });
        return child;
    }

    public static void saveState() {
        currentUser.sync(db);
        try {
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
