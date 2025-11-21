/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.com.group.hotelmanagement.entities;

import java.time.LocalDateTime;

/**
 *
 * @author cuong
 */
public class booking_services {
    private int id;
    private int bookingId;
    private int serviceId;
    private int quantity;
    private double totalPrice;

    public booking_services(int id, int bookingId, int serviceId, int quantity, double totalPrice) {
        this.id = id;
        this.bookingId = bookingId;
        this.serviceId = serviceId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
    //getter
    public int getId() {
        return id;
    }
    public int getBookingId() {
        return bookingId;
    }
    public int getServiceId() {
        return serviceId;
    }
    public int getQuantity() {
        return quantity;
    }
    public double getTotalPrice() {
        return totalPrice;
    }
}
