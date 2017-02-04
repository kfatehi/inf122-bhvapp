package com.company;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

/**
 * Created by keyvan on 2/3/17.
 */
public class TextUserInterface implements UserInterface {
    private Scanner scanner = new Scanner(System.in);
    private String userType;

    @Override
    public void start() {
        System.out.println("Welcome to Behave! Enter ? at any time for help");
        while (true) {
            prompt();
            String cmd = scanner.next();
            if (cmd.equals("?")) {
                showHelp(userType);
            } else if (cmd.equals("quit")) {
                System.exit(0);
            } else if (Main.currentUser != null) {
                if (cmd.equals("logout")) {
                    Main.currentUser = null;
                    System.out.println("you have been logged out");
                } else if (userType.equals("parent")) {
                    if (cmd.equals("add-child")) {
                        parentAddChild();
                    }
                } else if (userType.equals("child")) {
                    if (cmd.equals("tokens")) {
                        childViewTokens();
                    }
                }
            } else {
                if (cmd.equals("login")) {
                    login();
                }
            }
        }
    }

    private void prompt() {
        if (Main.currentUser != null) {
            System.out.print("["+Main.currentUser.getUsername()+"|"+userType+"]$ ");
        } else {
            System.out.print("$ ");
        }
    }

    private void login() {
        System.out.print("Enter your username: ");
        String username = scanner.next();

        userType = Main.props.getProperty("users."+username+".type");
        if (userType != null) {
            if (userType.equals("parent")){
                Main.currentUser = new Parent(username);
            } else if (userType.equals("child")) {
                Main.currentUser = new Child(username);
            }
        } else {
            System.out.println("no such user");
        }
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

    private void parentAddChild() {
        System.out.print("Enter child's name: ");
        String newChildsName = scanner.next();
        Child newChild = new Child(newChildsName);
        Parent parent = (Parent) Main.currentUser;
        parent.addChild(newChild);
        Main.props.setProperty("users." + newChildsName + ".type", "child");
        try {
            Main.saveProps();
            System.out.println("Added child: " + newChildsName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showHelp(String userType) {
        System.out.println("?                         show this information");
        if (Main.currentUser != null) {
            if (userType.equals("parent")) {
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
            } else if (userType.equals("child")) {
                System.out.println("tokens                    view the status of your tokens (number of tokens and info about each token)");
                System.out.println("redeem                    redeem your tokens for a reward (positive mode)");
            }
            System.out.println("logout                    logout");
        } else {
            System.out.println("login                     login");
        }
        System.out.println("quit                      quit the program");
    }
}
