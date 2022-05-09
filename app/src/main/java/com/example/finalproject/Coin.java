package com.example.finalproject;

public class Coin {

    protected String name;
    protected String icon;
    protected String quantity;
    protected String date;
    protected String time;
    protected String price;
    protected String profit;

    public Coin(String name, String icon, String quantity, String date, String time, String price) {
        this.name = name;
        this.icon = icon;
        this.quantity = quantity;
        this.date = date;
        this.time = time;
        this.price = price;
        this.profit = profit;
    }

    public Coin(String name) {
        this.name = name;
    }

    public Coin() {
    }

    public String getName() {
        return name;
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

    public String getProfit() {
        return profit;
    }

    public String getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
