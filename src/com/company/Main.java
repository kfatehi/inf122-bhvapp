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
        loadUserInterface();
        userInterface.start();
    }

    private static void loadState() {
        try {
            db.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadUserInterface() {
        String uiMode = db.get("ui", "text");
        if (uiMode.equals("text")) {
            userInterface = new TextUserInterface();
        } else if (uiMode.equals("web")) {
            userInterface = new WebInterface();
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
                currentUser = Child.load(db, username);
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
                Child child = Child.load(db, name);

                parent.addChild(child);
            }
        });
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

    public static KeyValueStore getDatabase() {
        return db;
    }
}
