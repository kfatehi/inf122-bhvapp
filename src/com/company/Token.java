package com.company;

import java.util.Date;
import java.util.UUID;

/**
 * Created by keyvan on 2/4/17.
 */
public class Token {
    private UUID uuid;
    private Date timeStamp;
    private String note;

    public Token(UUID _uuid, String _note) {
        uuid = _uuid;
        note = _note;
        timeStamp = new Date();
    }

    public String viewTimestamp() {
        return timeStamp.toString();
    }

    public String viewNote() {
        return note;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Date getDate() {
        return timeStamp;
    }

    public UUID getUUID() {
        return uuid;
    }
}
