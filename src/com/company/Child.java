package com.company;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by keyvan on 2/3/17.
 */
public class Child extends User {
    private HashMap<String,Mode> modes = new HashMap<>();
    private HashMap<Date,Token> tokens = new HashMap<>();
    private Integer redemptionAmount;

    public Child(String childName) {
        super(childName);
    }

    public void setMode(String name) {
        if (name.equals("positive"))
            setMode(name, new Positive());
        else if (name.equals("negative"))
            setMode(name, new Negative());
        else
            throw new Error("unsupported mode "+name);
    }

    public void setMode(String modeName, Mode mode) {
        modes.put(modeName, mode);
    }

    public HashMap<Date, Token> getTokens() {
        return tokens;
    }

    public HashMap<String, Mode> getModes() {
        return modes;
    }

    public void setRedemption(Integer redemption) {
        redemptionAmount = redemption;
    }

    public Integer getRedemptionAmount() {
        return redemptionAmount;
    }

    public void setRedemption(String redemption) {
        redemptionAmount = Integer.parseInt(redemption);
    }

    public void addToken() {
        addToken(new Date(), "<none>");
    }

    public void addToken(String note) {
        addToken(new Date(), note);
    }

    public void addToken(Date date, String note) {
        Token token = new Token(note);
        token.setTimeStamp(date);
        tokens.put(date, token);
    }

    public void addToken(String timeStamp, String note) {
    }
}
