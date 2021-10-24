package com.baby_controller.src;

import com.baby_controller.src.util.DatabaseManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Parent extends User{
    protected List<Baby> _children = new ArrayList<>();;

    public Parent(String userName, String password, UserType userType) {
        super(userName, password, userType);

    }

    @Override
    public List<Baby> getChildren() {
        return _children;
    }

    @Override
    public Baby getChild(String name, int id) {
        return null;
    }

    public void addNewChild(String name, int day, int month, int year, double wight){
        Baby newBaby = new Baby(wight);
        Date dateOfBirth = new Date(year,month,day);
        Date today = new Date(System.currentTimeMillis());
        newBaby.setAgeInMonths((int)((today.getTime() - dateOfBirth.getTime()) / (1000 * 60 +24 * 30)));
        _children.add(newBaby);
        DatabaseManager.addNewChild(this,newBaby);
    }
    public void addNewChild(Baby child){
        child.setParent(this);
        _children.add(child);

        //DatabaseManager.addNewChild(this,child);
    }
    public synchronized DatabaseReference uploadToDb(){
        FirebaseDatabase.getInstance().getReference().child(this.getInstitutionName()).
                child(User.UserType.PARENT.toString()).child(getUserName()).setValue(this);

        return FirebaseDatabase.getInstance().getReference();
//        DatabaseReference dbRef = super.uploadToDb().child(getUserName());
//        DatabaseReference tmpRef =dbRef;
//        reference = tmpRef;
//
//        for(int i = 0; i < _children.size(); i++){
//            tmpRef.child(_children.get(i).getName());
//            tmpRef.setValue(_children.get(i).uploadToDb());
//            tmpRef = dbRef;
//        }
//        return reference;
    }


    @Override
    public String toString() {
        return super.toString() + "\n_children=" + _children;
    }

    public List<Baby> get_children() {
        return _children;
    }

    public void set_children(List<Baby> _children) {
        this._children = _children;
    }
}
