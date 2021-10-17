package com.baby_controller.src;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    String _userName;
    String _password;
    List<Child> _children;

    public User(String userName, String password){
        _children = new ArrayList<>();
        this._userName = userName;
        this._password = password;
    }

    public void changPassword(String newPassword){
        _password = newPassword;
    }

    public void addNewChild(String name, int day, int month, int year, double wight){
        Child newChild = new Child(wight);
        Date dateOfBirth = new Date(year,month,day);
        Date today = new Date(System.currentTimeMillis());
        newChild.set_ageInMonths((int)((today.getTime() - dateOfBirth.getTime()) / (1000 * 60 +24 * 30)));
       _children.add(newChild);
    }
}
