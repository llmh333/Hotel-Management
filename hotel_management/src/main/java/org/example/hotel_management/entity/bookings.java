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
public class bookings {
    private int id;
    private int customerId;
    private String roomId;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private double totalAmount;
    private String status;

    public bookings(int id, int customer_id, String room_id, LocalDateTime check_in, LocalDateTime check_out, double total_amount, String status) {
        this.id = id;
        this.customerId = customer_id;
        this.roomId = room_id;
        this.checkIn = check_in;
        this.checkOut = check_out;
        this.totalAmount = total_amount;
        this.status = status;
    }
    //getter
    public int getId() {
        return id;
    }
    public int getCustomerId() {
        return customerId;
    }
    public String getRoomId() {
        return roomId;
    }
    public LocalDateTime getCheckIn() {
        return checkIn;
    }
    public LocalDateTime getCheckOut() {
        return checkOut;
    }
    public double getTotalAmount() {
        return totalAmount;
    }
    public String getStatus() {
        return status;
    }
    //setter
    public void setStatus(String status) {
        this.status = status;
    }
}
