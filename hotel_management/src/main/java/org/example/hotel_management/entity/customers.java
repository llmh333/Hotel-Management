/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

/**
 *
 * @author cuong
 */
public class customers {
    private int id;
    private String fullName;
    private String phoneNumb;

    public customers(int id, String full_name, String phone_numb) {
        this.id = id;
        this.fullName = full_name;
        this.phoneNumb = phone_numb;
    }
    //getter
    public int getId() {
        return id;
    }
    public String getFull_name() {
        return fullName;
    }
    public String getPhone_numb() {
        return phoneNumb;
    }
    //setter
    public void setFull_name(String full_name) {
        this.fullName = full_name;
    }
    public void setPhone_numb(String phone_numb) {
        this.phoneNumb = phone_numb;
    }
    
}
