/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.com.group.hotelmanagement.entities;

/**
 *
 * @author cuong
 */
public class services {
    private int id;
    private String name;
    private double price;
    private String category;
    private String desc;

    public services(int id, String name, double price, String category, String desc) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.desc = desc;
    }
    //getter
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public double getPrice() {
        return price;
    }
    public String getCategory() {
        return category;
    }
    public String getDesc() {
        return desc;
    }
    //setter
    public void setName(String name) {
        this.name = name;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
}
