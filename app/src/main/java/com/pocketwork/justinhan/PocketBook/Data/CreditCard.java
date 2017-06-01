package com.pocketwork.justinhan.PocketBook.Data;

/**
 * Created by justinhan on 5/12/17.
 */

public class CreditCard{

    private String nameonCard;
    private String name;
    private int month;
    private int year;
    private String cardNum;
    private int securityCode;

    public CreditCard() {

    }
    public CreditCard(String nameonCard, String name, int month, int year, String cardNum, int securityCode) {
        this.nameonCard = nameonCard;
        this.name = name;
        this.month = month;
        this.year = year;
        this.cardNum = cardNum;
        this.securityCode = securityCode;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public int getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(int securityCode) {
        this.securityCode = securityCode;
    }
    public String getLast4Digit() {
        return cardNum.substring(cardNum.length()-4);
    }

    public String getNameonCard() {
        return nameonCard;
    }

    public void setNameonCard(String nameonCard) {
        this.nameonCard = nameonCard;
    }

}
