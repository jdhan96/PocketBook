package com.pocketwork.justinhan.pocketbook.Data;

import java.util.List;

/**
 * Created by justinhan on 5/30/17.
 */

public class Cloud {
    private String date;
    private String name;
    private List<CreditCard> cards;
    private List<Note> notes;
    private List<Login> logins;

    public Cloud() {

    }

    public Cloud(String date, String name, List<CreditCard> cards, List<Note> notes, List<Login> logins) {
        this.date = date;
        this.name = name;
        this.cards = cards;
        this.notes = notes;
        this.logins = logins;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<CreditCard> getCards() {
        return cards;
    }

    public void setCards(List<CreditCard> cards) {
        this.cards = cards;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public List<Login> getLogins() {
        return logins;
    }

    public void setLogins(List<Login> logins) {
        this.logins = logins;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
