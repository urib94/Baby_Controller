package com.baby_controller.src;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;


public class LocalUser {
    protected DatabaseReference reference;
    protected UserType userType;
    protected String institutionName;
    protected String userName;



    protected String email;
    protected String password;
    protected String uid;
    private BluetoothDevice defaultDevice = null;
    private ValueEventListener postListener;


    public LocalUser(){}

    public LocalUser(String email ,String userName, String password, UserType userType){
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.userType = userType;

    }


    public void changPassword(String newPassword){
        password = newPassword;
    }

    public DatabaseReference uploadToDb(){

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userName);
        Transaction.Handler tmp = new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                currentData.setValue(LocalUser.this);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                if (error != null) {
                    Log.d("Firebase", "error: " + error.getMessage());
                }

            }
        } ;
        reference.runTransaction(tmp);

        return reference;
    }

//    //upload to this LocalUser to the database as a transaction
//    public void uploadToDb(){
//        //update the reference to the users section of the database
//        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userName);
//
//        Transaction.Handler tmp = new Transaction.Handler() {
//            @NonNull
//            @Override
//            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
//                currentData.setValue(LocalUser.this);
//                return Transaction.success(currentData);
//            }
//
//            @Override
//            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
//                if (error != null) {
//                    Log.d("Firebase", "error: " + error.getMessage());
//                }
//
//            }
//        } ;
//        reference.runTransaction(tmp);
//    }

    //set listeners that updates the LocalUser when it changes in the database
    public void setListener(){
        //use the methods of the Parent or Manger1 to set the listener depending on the user type
        switch (userType){

            case MANAGER:
                ((Manager1)LocalUser.this).setListener();
                break;
            case PARENT:
                ((Parent)LocalUser.this).setListener();
                break;
            case ADMINISTRATOR:
                break;
        }
    }
    // method that get a localUser and set this to ist values
    public void setLocalUser(LocalUser localUser){
        if(localUser.institutionName != null)   this.institutionName = localUser.institutionName;
        if(localUser.userName != null)          this.userName = localUser.userName;
        if(localUser.email != null)             this.email = localUser.email;
        if(localUser.password != null)          this.password = localUser.password;
        if(localUser.userType != null)          this.userType = localUser.userType;
    }

    public void uploadNewUser(){
        if(getInstitute().getReference() == null)
            getInstitute().uploadToDb();
        switch (userType){

            case MANAGER:
                ((Manager1)this).uploadToDb();
                break;
            case PARENT:
                ((Parent)this).uploadToDb();
                break;

        }

        DatabaseReference tmpRef = FirebaseDatabase.getInstance().getReference().child("users").child(getUserName());

        tmpRef.child("reference").setValue("");
        tmpRef.child("reference").setValue(reference.toString());
    }

    public String getInstitutionName() {
        return institutionName;
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


    public LocalUser getUserFromDb(String userName){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");


        LocalUser result = null;
        //get the user from the db
        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                LocalUser tmp = dataSnapshot.getValue(LocalUser.class);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

            }
        };
        reference.addValueEventListener(postListener);
        return reference.child(userName).get().getResult().getValue(LocalUser.class);
    }


    public Institution getInstitute() {
        final Institution[] retVal = {null};
        if (institutionName != null){
            //get the Institution from the db by its name
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Institutions").child(institutionName);
            postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    Institution tmp = dataSnapshot.getValue(Institution.class);
                    retVal[0] = tmp;

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            };
        }
        return retVal[0];
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }



    public enum UserType{
        MANAGER,
        PARENT,
        ADMINISTRATOR
    }

    public BluetoothDevice getDefaultDevice() {
        return defaultDevice;
    }

    public void setDefaultDevice(BluetoothDevice defaultDevice) {
        this.defaultDevice = defaultDevice;
        reference.addValueEventListener(postListener);

    }
}
