package com.baby_controller.src;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class User implements Serializable {
    protected DatabaseReference reference;
    protected Institution institute;
    protected UserType userType;
    protected String institutionName;
    protected String userName;
    protected String password;
    protected String name;
    protected String uid;

    public User(){};

    public User(String email, String password, UserType userType){

        this.userName = email;
        this.password = password;
        this.userType = userType;
        name = "";
    }

    public User(String name1, String userName, String password, UserType userType){
        name = name1;
        this.userName = userName;
        this.password = password;
        this.userType = userType;
    }

    public void changPassword(String newPassword){
        password = newPassword;
    }

    public DatabaseReference uploadToDb(){

        return  reference = FirebaseDatabase.getInstance().getReference().child(getInstitutionName()).child(userType.toString());
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(Institution institution) {
        this.institute = institution;
        this.institutionName = institution.getName();
    }

    public Institution getInstitution(){
       return institute;
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

    public DatabaseReference getReference() {
        return reference;
    }

    public void setReference(DatabaseReference reference) {
        this.reference = reference;
    }

    @Override
    public String toString() {
        return  "\nuserType=" + userType +
                "\ninstitutionName=" + institutionName +
                "\n_userName='" + userName + '\'' +
                "\n_password='" + password;
    }


    public User getUserFromDb(String userName){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        User result = null;
        //get the user from the db
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                User tmp = dataSnapshot.getValue(User.class);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

            }
        };
        reference.addValueEventListener(postListener);
        return reference.child(userName).get().getResult().getValue(User.class);
    }




    public String get_password() {
        return password;
    }

    public void set_password(String password) {
        this.password = password;
    }

    public void setInstitution(Institution institution){
        this.institute = institution;
    }

    public Institution getInstitute() {
        return institute;
    }

    public void setInstitute(Institution institute) {
        this.institute = institute;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public enum UserType{
        MANAGER,
        PARENT,
        ADMINISTRATOR
    }
}
