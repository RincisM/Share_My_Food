package com.sharemyfood.app;

import java.io.Serializable;

public class Model implements Serializable {
    private String imageUrl;
    private String name;
    private String description;
    private String quantity;
    private String expiry;
    private String location;
    private String phone;

    public Model() {}
    public Model(String imageUrl, String name, String description, String quantity, String expiry, String location, String phone) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.expiry = expiry;
        this.location = location;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLocation() { return location; }

    public void setLocation(String location) {this.location = location;}

    public String getPhone() {return phone;}

    public void setPhone(String phone) {this.phone = phone;}
}
