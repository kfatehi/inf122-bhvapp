package com.company;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by keyvan on 2/3/17.
 */
public class TextUserInterface implements UserInterface {
    private Scanner scanner = new Scanner(System.in);
    private boolean interactive = true;

    private void showHelp() {
        System.out.println("?                         show this information");
        if (isLoggedIn()) {
            if (getUserType().equals("Parent")) {
                System.out.println("add-child                 add a child");
                System.out.println("edit-child                edit a child");
                System.out.println("delete-child              delete child");
                System.out.println("add-token                 add token");
                System.out.println("edit-token                edit token");
                System.out.println("delete-token              delete token");
                System.out.println("status                    view the status of all children (number of tokens and info about each token)");
                System.out.println("redeem                    redeem a child’s tokens for a reward (positive mode) or a consequence (negative mode)");
                System.out.println("set-redemption            define, per child, the number of tokens required for redemption and (optionally) what the tokens will be redeemed for");
                System.out.println("set-mode                  define one mode per child");
                System.out.println("schedule                  schedule tokens to be automatically added periodically (e.g., one per day)");
            } else if (getUserType().equals("Child")) {
                System.out.println("tokens                    view the status of your tokens (number of tokens and info about each token)");
                System.out.println("redeem                    redeem your tokens for a reward (positive mode)");
            }
            System.out.println("logout                    logout");
        } else {
            System.out.println("login                     login");
        }
        System.out.println("quit                      quit the program");
    }


    @Override
    public void start() {
        String frontend = System.getenv("BEHAVE_FRONTEND");
        if (frontend != null && frontend.equals("noninteractive")) {
            interactive = false;
        }

        System.out.println("Welcome to Behave! Enter ? at any time for help");
        while (true) {
            prompt();
            try {
                String cmd = getToken();
                if (cmd.equals("?")) {
                    showHelp();
                } else if (cmd.equals("quit")) {
                    System.exit(0);
                } else if (isLoggedIn()) {
                    if (cmd.equals("logout")) {
                        logout();
                    } else if (getUserType().equals("Parent")) {
                        if (cmd.equals("add-child")) {
                            parentAddChild();
                        } else if (cmd.equals("status")) {
                            parentGetStatus();
                        } else if (cmd.equals("set-mode")) {
                            parentSetMode();
                        } else if (cmd.equals("set-redemption")) {
                            parentSetRedemption();
                        } else {
                            unrecognizedCommand(cmd);
                        }
                    } else if (getUserType().equals("Child")) {
                        if (cmd.equals("tokens")) {
                            childViewTokens();
                        } else {
                            unrecognizedCommand(cmd);
                        }
                    } else {
                        unrecognizedCommand(cmd);
                    }
                } else if (cmd.equals("login")) {
                    login();
                } else {
                    unrecognizedCommand(cmd);
                }
            } catch (java.util.NoSuchElementException e) {
                System.exit(0);
            }
        }
    }

    private void parentSetRedemption() {
        Parent parent = (Parent) Main.currentUser;
        System.out.print("Enter child name: ");
        String childsName = getToken();
        System.out.print("Enter redemption count: ");
        Integer num = Integer.parseInt(getToken());
        parent.setRedemption(childsName, num);
        Main.saveState();
        System.out.println(String.format("Set %s redemption to %d", childsName, num));
    }

    private void parentAddChild() {
        Parent parent = (Parent) Main.currentUser;
        System.out.print("Enter child's name: ");
        String newChildsName = getToken();
        parent.addChild(new Child(newChildsName));
        Main.saveState();
        System.out.println("Added child " + newChildsName);
    }

    // view the status of all children (number of tokens and info about each token)");
    private void parentGetStatus() {
        Parent parent = (Parent) Main.currentUser;
        parent.getChildren().forEach((childName, child)->{
            System.out.println(childName);
            int count = child.getTokens().size();
            System.out.println("  number of tokens: "+Integer.toString(count));
            child.getTokens().forEach(token->{
                System.out.println("  token, note: "+token.viewNote());
            });
        });
    }

    private void parentSetMode() {
        Parent parent = (Parent) Main.currentUser;
        System.out.print("Enter child name: ");
        String childsName = getToken();
        System.out.print("Enter mode: ");
        String modeName = getToken();
        parent.setMode(childsName, modeName);
        Main.saveState();
        System.out.println(String.format("Set %s to %s mode", childsName, modeName));
    }

    private void childViewTokens() {
        Child child = (Child) Main.currentUser;
        ArrayList<Token> tokens = child.getTokens();
        if (tokens.size() == 0) {
            System.out.println("you have no tokens");
        } else {
            tokens.forEach((token) -> {
                System.out.println("token, note: " + token.viewNote());
            });
        }
    }

    private void unrecognizedCommand(String cmd) {
        printlnRed("unrecognized command "+cmd);
    }

    private void printlnRed(String str) {
        System.out.println(red(str));
    }

    private String red(String str) {
        return (char)27 + "[31m"+str + (char)27 + "[0m";
    }

    private String getToken() {
        String cmd = scanner.next();
        if (!interactive) {
            System.out.println(cmd);
        }
        return cmd;
    }

    private void logout() {
        Main.currentUser = null;
        System.out.println("you have been logged out");
    }

    private boolean isLoggedIn() {
        return Main.currentUser != null;
    }

    private void prompt() {
        if (isLoggedIn()) {
            System.out.print("["+Main.currentUser.getUsername()+"|"+ getUserType() +"]$ ");
        } else {
            System.out.print("$ ");
        }
    }

    private void login() {
        System.out.print("Enter your username: ");
        String username = getToken();

        if (! Main.login(username)) {
            printlnRed("user not found");
        }
    }

    private String getUserType() {
        return Main.currentUser.getClass().getSimpleName();
    }
}
