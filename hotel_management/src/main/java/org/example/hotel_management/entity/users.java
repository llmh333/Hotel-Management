/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import java.time.LocalDateTime;

/**
 *
 * @author cuong
 */
public class users {
    private String id;
    private String userName;
    private String pass;
    private String email;
    private String role;
    private LocalDateTime create_At;
    private LocalDateTime update_At;

    public users(String id, String userName, String pass, String email, String role) {
        this.id = id;
        this.userName = userName;
        this.pass = pass;
        this.email = email;
        this.role = role;
        this.create_At = LocalDateTime.now();
        this.update_At = LocalDateTime.now();
    }
    
    //getter
    public String getId() {
        return id;
    }
    public String getUserName() {
        return userName;
    }
    public String getPass() {
        return pass;
    }
    public String getEmail() {
        return email;
    }
    public String getRole() {
        return role;
    }
    public LocalDateTime getCreate_At() {
        return create_At;
    }
    public LocalDateTime getUpdate_At() {
        return update_At;
    }
    
    //setter
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public void setUpdate_At() {
        this.update_At = LocalDateTime.now();
    }
}
