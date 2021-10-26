package com.baby_controller.src;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

public class Manager1 extends User implements Runnable {
    //    List<Parent> parents = new ArrayList<>();
    DatabaseReference reference;
    User.UserType userType = User.UserType.MANAGER;

    public Manager1(){}

    public Manager1(String userName, String password) {
        super(userName,password,UserType.MANAGER);
    }

    public Manager1(String name,String userName, String password) {
        super(name, userName,password,UserType.MANAGER);
    }


    public List<Baby> getChildren() {
        return null;
    }


    public User.UserType getUserType() {
        return userType;
    }

    public void setUserType(User.UserType userType) {
        this.userType = userType;
    }

    /*
       uses the User uploadToDb, start working in the "UserType" child
       returns the reference to the manger's username child
        */

//    public synchronized DatabaseReference uploadToDb() {
//        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(get);
//        dbRef.setValue(this);
//        return dbRef;
//    }


    public Baby getChild(String name, int id) {
        return null;
    }

    public DatabaseReference uploadToDb() {
        if(name != null) {
            reference = FirebaseDatabase.getInstance().getReference().child(getInstitution().getName())
                    .child("management").child(name);
            reference.child("name").setValue(name);
        }else {
            FirebaseDatabase.getInstance().getReference().child(getInstitution().getName())
                    .child("management").child(userName);
        }


        reference.child("user name").setValue(userName);
        reference.child("password").setValue(password);
        reference = FirebaseDatabase.getInstance().getReference().child(getInstitution().getName())
                .child("management").child(name);


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Manager1 tmp = dataSnapshot.getValue(Manager1.class);
                userType = tmp.userType;
                userName = tmp.userName;
                name = tmp.name;
                password = tmp.password;userName = tmp.userName;
                name = tmp.name;
                password = tmp.password;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

            }
        };
        return reference;
    }


    public void feedBaby(int amount, Baby babyToFeed){
        LinkedList<Parent> parents = getInstitution().getParents();
        Baby tmp = null;
        for(Parent parent :parents){
            for (Baby baby: parent.getChildren()){
                if(tmp == null){
                    if (baby.equals(babyToFeed)){
                        tmp = baby;
                        break;
                    }
                    if(tmp != null){
                        tmp.eatingNextMeal(amount);
                        tmp.uploadToDb();
                        parent.notifyParent();
                        break;
                    }
                }
            }
        }

    }
    public void notifyBabyNeedToEat(Baby baby){

    }


    @Override
    public void run() {
        int i = 0;
        while (i != 1){
            if((System.currentTimeMillis() % Config.TEN_MIN) == 0) {
                Baby baby = getInstitution().needToFeed();
                if (baby != null) {
                    notifyBabyNeedToEat(baby);
                }
            }
        }
    }
}
