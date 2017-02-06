package com.company;

import java.util.Date;

/**
 * Created by keyvan on 2/4/17.
 */
public class Token {
    private Date timeStamp;
    private String note;

    public Token(String _note) {
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
}
