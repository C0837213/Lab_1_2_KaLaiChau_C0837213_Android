package com.example.lab_1_2_kalaichau_c0837213_android.model;

public class Product {
    int id;
    String name, desc;
    double lat, lon, price;

    public int getId() { return id; }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public Product(int id, String name, String desc, double price, double lat, double lon) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.lat = lat;
        this.lon = lon;
    }

}
