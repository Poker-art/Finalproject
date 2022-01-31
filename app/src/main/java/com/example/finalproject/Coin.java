package com.example.finalproject;

public class Coin {

    protected String ticker;
    protected String icon;
    protected String quantity;
    protected String date;
    protected String time;
    protected String price;

    public Coin(String ticker, String icon, String quantity, String date, String time, String price) {
        this.ticker = ticker;
        this.icon = icon;
        this.quantity = quantity;
        this.date = date;
        this.time = time;
        this.price = price;
    }

    public Coin(String ticker) {
        this.ticker = ticker;
    }

    public Coin() {
    }

    public String getTicker() {
        return ticker;
    }

    public String getIcon() {
        return icon;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getPrice() {
        return price;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
