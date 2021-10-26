package com.baby_controller.src;

import com.baby_controller.src.util.DatabaseManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Parent extends User{
    protected List<Baby> children = new ArrayList<>();;

    public Parent(String userName, String password, UserType userType) {
        super(userName, password, userType);

    }

    public Parent(String name,String userName, String password, UserType userType) {
        super(name,userName, password, userType);
    }

    @Override
    public List<Baby> getChildren() {
        return children;
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
        children.add(newBaby);
        DatabaseManager.addNewChild(this,newBaby);
    }
    public void addNewChild(Baby child){
        child.setParent(this);
        children.add(child);

        //DatabaseManager.addNewChild(this,child);
    }
    public synchronized DatabaseReference uploadToDb(){
        reference = FirebaseDatabase.getInstance().getReference().child(this.getInstitutionName()).
                child(User.UserType.PARENT.toString()).child(getUserName());
        reference.child("User Name").setValue(getUserName());
        reference.child("Password").setValue(get_password());
        for(int i = 0; i < children.size(); i++){
            children.get(i).setReference(reference.child("children").child(children.get(i).getName()));
            children.get(i).uploadToDb();

        }

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Parent tmp = dataSnapshot.getValue(Parent.class);
                userName = tmp.userName;
                name = tmp.name;
                password = tmp.password;
                institute = tmp.institute;
                children = tmp.children;
                userType = tmp.userType;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

            }
        };

        return FirebaseDatabase.getInstance().getReference();

    }


    @Override
    public String toString() {
        return super.toString() + "\nchildren=" + children;
    }


    public void notifyParent(){
        // TODO: 10/26/2021  writ this function
    }

    public Baby babyNeedToFeed(){
        for (Baby baby: children){
            Meal last = baby.history.get(baby.history.size() -1);
            Time now = new Time(System.currentTimeMillis());
            if (now.after(last.getTimeToEat())){
                return baby;
            }
        }
        return null;
    }

    public void setChildren(List<Baby> children) {
        this.children = children;
    }
}
