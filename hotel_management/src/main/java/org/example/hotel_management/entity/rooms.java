/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

/**
 *
 * @author cuong
 */
public class rooms {
    private String id;
    private String number;
    private double price;
    private String desc;
    private String type;
    private String status;

    public rooms(String id, String number, double price, String desc, String type, String status) {
        this.id = id;
        this.number = number;
        this.price = price;
        this.desc = desc;
        this.type = type;
        this.status = status;
    }
    //getter
    public String getId() {
        return id;
    }
    public String getNumber() {
        return number;
    }
    public double getPrice() {
        return price;
    }
    public String getDesc() {
        return desc;
    }
    public String getType() {
        return type;
    }
    public String getStatus() {
        return status;
    }
    //setter
    public void setNumber(String number) {
        this.number = number;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    
}
