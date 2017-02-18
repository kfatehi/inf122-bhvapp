package com.company;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static java.util.Comparator.comparing;

/**
 * Created by keyvan on 2/3/17.
 */
public class Child extends User {
    private HashMap<String,Mode> modes = new HashMap<>();
    private HashMap<UUID,Token> tokens = new HashMap<>();
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

    public HashMap<UUID, Token> getTokens() {
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

    public void addToken(String note) {
        addToken(new Date(), note);
    }

    public void addToken(Date date,  String note) {
        addToken(UUID.randomUUID(), date, note);
    }

    public void addToken(UUID uuid, Date date, String note) {
        Token token = new Token(uuid, note);
        token.setTimeStamp(date);
        tokens.put(uuid, token);
    }

    public boolean canRedeem() {
        return tokens.size() >= redemptionAmount;
    }

    public ArrayList<UUID> redeemTokens() {
        ArrayList<UUID> uuids = new ArrayList<>();
        if (canRedeem()) {
            for (int i = 0; i < redemptionAmount; i++) {
                Token token = getOldestToken();
                UUID uuid = token.getUUID();
                uuids.add(uuid);
                tokens.remove(uuid);
            }
        }
        return uuids;
    }

    private Token getOldestToken() {
        return tokens.values().stream().sorted(
                comparing((Token t)->t.getDate().getTime()).reversed()
        ).findFirst().get();
    }

    public void sync(KeyValueStore db) {
        String name = this.getUsername();
        db.set("users." + name + ".type", "Child");

        String modeNames = db.join(this.getModes().keySet().stream());
        db.set("child."+name+".modes", modeNames);
        db.set("child."+name+".redemptionAmount", String.valueOf(this.getRedemptionAmount()));

        HashMap<UUID,Token> tokens = this.getTokens();
        String tokenIds = db.join(tokens.keySet().stream().map(UUID::toString));
        db.set("tokens."+name, tokenIds);

        tokens.forEach((id, token) -> {
            String time = String.valueOf(token.getDate().getTime());
            db.set("token."+name+"."+id+".note", token.viewNote());
            db.set("token."+name+"."+id+".time", time);
        });
    }

    public static Child load(KeyValueStore db, String name) {
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
        int foo = 1;
        return child;
    }

    public long countTokensOnDay(LocalDate localDate) {
        Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return getTokens().values().stream().map(Token::getDate).filter((date)->{
            return fmt.format(date).equals(fmt.format(Date.from(instant)));
        }).count();
    }
}
