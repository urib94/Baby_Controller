package com.baby_controller.src;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public abstract class User {
    protected DatabaseReference reference;
    private UserType userType;
    private Institution institution;
    private String userName;
    private String password;

    public User(){
        userName = "test";
        password = "888";
    };

    public User(String userName, String password, UserType userType){

        this.userName = userName;
        this.password = password;
        this.userType = userType;
    }

    public void changPassword(String newPassword){
        password = newPassword;
    }

    public DatabaseReference uploadToDb(){

        return  reference = FirebaseDatabase.getInstance().getReference().child(getInstitution().getName()).child(userType.toString());
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public abstract List<Child> getChildren();

    public abstract Child getChild(String name, int id);

    public DatabaseReference getReference() {
        return reference;
    }

    public void setReference(DatabaseReference reference) {
        this.reference = reference;
    }

    @Override
    public String toString() {
        return  "\nuserType=" + userType +
                "\ninstitution=" + institution +
                "\n_userName='" + userName + '\'' +
                "\n_password='" + password;
    }

    public String get_password() {
        return password;
    }

    public void set_password(String password) {
        this.password = password;
    }



    public enum UserType{
        MANAGER,
        PARENT,
        ADMINISTRATOR
    }
}
