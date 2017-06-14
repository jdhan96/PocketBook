package com.pocketwork.justinhan.PocketBook.Data;

/**
 * Created by justinhan on 5/12/17.
 */

public class CreditCard{

    private String nameonCard;
    private String name;
    private String month;
    private String year;
    private String cardNum;
    private String securityCode;

    public CreditCard() {

    }
    public CreditCard(String nameonCard, String name, String month, String year, String cardNum, String securityCode) {
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

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }
    public String getNameonCard() {
        return nameonCard;
    }

    public void setNameonCard(String nameonCard) {
        this.nameonCard = nameonCard;
    }

}
