package com.dipale.foodapp;

public class Food {
    int img;
    int food_ID;
    String name;
    int price;
    int shipCost;
    String linkMap;
    String linkDetail;

    public Food(int img, String name, int price, int shipCost,int food_ID, String linkDetail) {
        this.img = img;
        this.name = name;
        this.price = price;
        this.shipCost = shipCost;
        this.food_ID = food_ID;
        this.linkDetail = linkDetail;
        this.linkMap = "geo:10.76011735611393, 106.68224756799472?q=10.76011735611393, 106.68224756799472";
    }

    public Food(int img, String name, int price, int shipCost,int food_ID, String linkDetail, String linkMap) {
        this.img = img;
        this.name = name;
        this.price = price;
        this.shipCost = shipCost;
        this.food_ID = food_ID;
        this.linkDetail = linkDetail;
        this.linkMap = linkMap;
    }

    public int getFood_ID() {
        return food_ID;
    }

    public void setFood_ID(int food_ID) {
        this.food_ID = food_ID;
    }

    public int getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public double getShipCost() {
        return shipCost;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setShipCost(int shipCost) {
        this.shipCost = shipCost;
    }

    public String getLinkMap() {
        return linkMap;
    }

    public void setLinkMap(String linkMap) {
        this.linkMap = linkMap;
    }
}