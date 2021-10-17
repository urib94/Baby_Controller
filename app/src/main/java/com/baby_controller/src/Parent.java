package com.baby_controller.src;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Parent extends User{
    protected List<Child> _children = new ArrayList<>();;

    public Parent(String userName, String password, UserType userType) {
        super(userName, password, userType);

    }

    @Override
    public List<Child> getChildren() {
        return _children;
    }

    @Override
    public Child getChild(String name, int id) {
        return null;
    }

    public void addNewChild(String name, int day, int month, int year, double wight){
        Child newChild = new Child(wight);
        Date dateOfBirth = new Date(year,month,day);
        Date today = new Date(System.currentTimeMillis());
        newChild.setAgeInMonths((int)((today.getTime() - dateOfBirth.getTime()) / (1000 * 60 +24 * 30)));
        _children.add(newChild);
    }

    public List<Child> get_children() {
        return _children;
    }

    public void set_children(List<Child> _children) {
        this._children = _children;
    }
}
