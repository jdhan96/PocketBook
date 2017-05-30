package com.pocketwork.justinhan.pocketbook.Data;

import android.util.Log;

/**
 * Created by justinhan on 5/25/17.
 */

public class Note{
    private String note;
    private String name;

    public Note(String name, String note) {
        this.name = name;
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
