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
public class invoices {
    private int id;
    private String employeeId;
    private int bookingId;
    private double totalRoomCharge;
    private double totalServiceCharge;
    private double totalAmount;
    private String payMethod;
    private LocalDateTime createAt;

    public invoices(int id, String employeeId, int bookingId, double totalRoomCharge, double totalServiceCharge, String payMethod) {
        this.id = id;
        this.employeeId = employeeId;
        this.bookingId = bookingId;
        this.totalRoomCharge = totalRoomCharge;
        this.totalServiceCharge = totalServiceCharge;
        this.totalAmount = this.totalRoomCharge + this.totalServiceCharge;
        this.payMethod = payMethod;
        this.createAt = LocalDateTime.now();
    }
    //getter
    public int getId() {
        return id;
    }
    public String getEmployeeId() {
        return employeeId;
    }
    public int getBookingId() {
        return bookingId;
    }
    public double getTotalRoomCharge() {
        return totalRoomCharge;
    }
    public double getTotalServiceCharge() {
        return totalServiceCharge;
    }
    public double getTotalAmount() {
        return totalAmount;
    }
    public String getPayMethod() {
        return payMethod;
    }
    public LocalDateTime getCreateAt() {
        return createAt;
    }
}
