/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

/**
 *
 * @author cuong
 */
public class employees {
    private String id;
    private String fullName;
    private String phoneNumb;
    private String userId;

    public employees(String id, String fullName, String phone_Numb, String user_Id) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumb = phone_Numb;
        this.userId = user_Id;
    }
    //getter
    public String getId() {
        return id;
    }
    public String getFullName() {
        return fullName;
    }

    public String getPhone_Numb() {
        return phoneNumb;
    }
    public String getUser_Id() {
        return userId;
    }
    //setter
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public void setPhone_Numb(String phone_Numb) {
        this.phoneNumb = phone_Numb;
    }   
}
