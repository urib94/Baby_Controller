package com.baby_controller.src;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseApp;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class Parent extends LocalUser {
    protected ArrayList<Baby> children = new ArrayList<>();

    public Parent(){
        super();
    }

    public Parent(String email,String userName, String password, UserType userType) {
        super(email, userName, password, userType);

    }

    public Parent(String userName,String email, String password) {
        super(email,userName, password, UserType.PARENT);
    }

    public Parent(String userName,String email, String password, String uid, String instituteName) {
        super(email,userName, password, UserType.PARENT,uid,instituteName);
    }

    //copy constructor
    public Parent(Parent parent){
        super(parent);
        this.children = parent.children;

    }

    public Parent(LocalUser localUser) {
        super(localUser);
        if(localUser instanceof Parent){
            this.children = ((Parent) localUser).getChildren();
        }
    }



    public ArrayList<Baby> getChildren() {
        return children;
    }


    public Baby getChild(String name) {
        for (Baby child : children) {
            if (child.getName().equals(name)) {
                return child;
            }
        }
        return null;
    }

    public void addNewChild(String babyName, int day, int month, int year, double wight){
        Baby newBaby = new Baby(babyName,wight);
        Date dateOfBirth = new Date(year,month,day);
        Date today = new Date(System.currentTimeMillis());
        newBaby.setAgeInMonths((int)((today.getTime() - dateOfBirth.getTime()) / (1000 * 60 +24 * 30)));
        newBaby.setParentName(name);
        newBaby.setParentUid(uid);
        newBaby.setIndexInInstitute(indexInInstitute);
        newBaby.setInstitutionName(institutionName);
        newBaby.setIndexInParent(children.size());
        children.add(newBaby);
        updateInDb();
    }




    //notify the parent that the child is hungry with firebase cloud messaging
    public void notifyParent(){
        //get app instance id from firebase
        FirebaseApp.getInstance().getOptions().getApplicationId()
        ;

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

    public void setChildren(ArrayList<Baby> children) {
        this.children = children;
    }

    //get all the Babies that need to be fed
    public LinkedList<Baby> getBabiesNeedToFeed() {
        LinkedList<Baby> babiesNeedToFeed = new LinkedList<>();
        for(Baby baby: children){
            if(baby.history.size() == 0){
                Meal tmpMeal = new Meal(baby.getRecommendedAmountPerMeal());
                tmpMeal.setTimeToEat(new Time(System.currentTimeMillis() -1000));
                baby.getHistory().add(tmpMeal);

            }
            Meal last = baby.history.get(baby.history.size() -1);
            Time now = new Time(System.currentTimeMillis());
            if (last.getTimeToEat() == null || now.after(last.getTimeToEat())){
                babiesNeedToFeed.add(baby);
            }
        }
        return babiesNeedToFeed;
    }

    public LinkedList<Baby> getBabiesNotNeedToFeed() {
        LinkedList<Baby> babiesDontNeedToFeed = new LinkedList<>();
        for(Baby baby: children){
            Meal last = baby.history.get(baby.history.size() -1);
            Time now = new Time(System.currentTimeMillis());
            if (last.getTimeToEat() == null || now.before(last.getTimeToEat())){
                babiesDontNeedToFeed.add(baby);
            }
        }
        return babiesDontNeedToFeed;
    }






    @NonNull
    @Override
    public String toString() {
        return  ",userType=" + userType +
                ",institutionName=" + institutionName +
                ",userName=" + name +
                ",indexInInstitute=" + indexInInstitute +
                ",email=" + email +
                ",password=" + password +
                ",uid=" + uid +
                ",defaultDevice=" + defaultDeviceAddress + ",children=" + children +
                '}';
    }

}
