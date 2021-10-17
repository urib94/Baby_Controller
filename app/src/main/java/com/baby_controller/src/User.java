package com.baby_controller.src;

import java.util.ArrayList;
import java.util.List;

public abstract class User {
    private UserType userType;
    private Institution institution;
    private String _userName;
    private String _password;


    public User(String userName, String password, UserType userType){

        this._userName = userName;
        this._password = password;
        this.userType = userType;
    }

    public void changPassword(String newPassword){
        _password = newPassword;
    }



    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String get_userName() {
        return _userName;
    }

    public void set_userName(String _userName) {
        this._userName = _userName;
    }

    public abstract List<Child> getChildren();

    public abstract Child getChild(String name, int id);

    public String get_password() {
        return _password;
    }

    public void set_password(String _password) {
        this._password = _password;
    }



    public enum UserType{
        MANAGER,
        PARENT,
        ADMINISTRATOR
    }
}
