package com.baby_controller.src;

import java.util.List;

public class Manager extends User{
    //    List<Parent> parents = new ArrayList<>();
    Parent parents;
    String userName;
    String password;
    User.UserType userType = User.UserType.MANAGER;

    public Manager(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public Parent getParents() {
        return parents;
    }

    public void setParents(Parent parents) {
        this.parents = parents;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public List<Baby> getChildren() {
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


    public Baby getChild(String name, int id) {
        return null;
    }
}
