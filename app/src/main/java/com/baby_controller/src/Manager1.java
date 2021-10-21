package com.baby_controller.src;

import java.util.List;

public class Manager1 {
    //    List<Parent> parents = new ArrayList<>();
    Parent parents;
    String userName;
    String password;
    User.UserType userType = User.UserType.MANAGER;

    public Manager1(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public Parent getParents() {
        return parents;
    }

    public void setParents(Parent parents) {
        this.parents = parents;
    }

    public String get_userName() {
        return userName;
    }

    public void set_userName(String userName) {
        this.userName = userName;
    }


    public List<Child> getChildren() {
        return null;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User.UserType getUserType() {
        return userType;
    }

    public void setUserType(User.UserType userType) {
        this.userType = userType;
    }




    @Override
    public String toString() {
        return super.toString() + "\nparents=" + parents;
    }


    public Child getChild(String name, int id) {
        return null;
    }
}
