package com.example.dresscollection;

public class Dress {
    private String name;
    private String type;
    private String size;
    private String color;
    private double price;
    private String details;
    private String gender;
    private int quantity;
    private String date;
    private String discount;
    private String boosting;
    private String imagePath;

    public Dress(String name, String type, String size, String color, double price, String details, String gender,
                 int quantity, String date, String discount, String boosting,String imagePath) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.color = color;
        this.price = price;
        this.details = details;
        this.gender = gender;
        this.quantity = quantity;
        this.date = date;
        this.discount = discount;
        this.boosting = boosting;
        this.imagePath = imagePath;
    }

    public String getName() { return name; }
    public String getType() { return type; }
    public String getSize() { return size; }
    public String getColor() { return color; }
    public double getPrice() { return price; }
    public String getDetails() { return details; }
    public String getGender() { return gender; }
    public int getQuantity() { return quantity; }
    public String getDate() { return date; }

    public String getBoosting() { return boosting; }

    public String getDiscountCode() {
        return discount;
    }
    public String getImagePath() {
        return imagePath;
    }

}
