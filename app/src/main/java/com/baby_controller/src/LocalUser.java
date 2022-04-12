package com.baby_controller.src;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.LinkedList;


public class LocalUser  {
    protected int indexInInstitute = 0;
    protected UserType userType;
    protected String institutionName;
    protected String name;

    protected String registrationToken;

    protected String email;
    protected String password;
    protected String uid;
    protected String defaultDeviceAddress = null;
    private String deviceName = "";



    public LocalUser(){}

    public LocalUser(String email , String name, String password, UserType userType){

        this.email = email;
        this.name = name;
        this.password = password;
        this.userType = userType;

    }

    //coppy other user
    public LocalUser(LocalUser other){
        this.name = other.name;
        this.password = other.password;
        this.email = other.email;
        this.institutionName = other.institutionName;
        this.userType = other.userType;
        this.uid = other.uid;
        this.defaultDeviceAddress = other.defaultDeviceAddress;
    }

    public LocalUser(String email, String name, String password, UserType userType, String uid, String instituteName) {
        this.email = email;
        this.name = name;
        this.userType = userType;
        this.password = password;
        this.uid = uid;
        this.institutionName = instituteName;
    }

    //parse a string to a user
    public static LocalUser LocalUserFromString(String userString){
        LocalUser newUser = null;
        //delete all the "{" and "}"
        int end = userString.indexOf("children=");
        if (end > 0 ) {
            userString = userString.substring(0, end);
        }
        userString = userString.replace("{","");
        userString = userString.replace("}","");
        LinkedList<String> user = new LinkedList<>(Arrays.asList(userString.split(",")));

        for (String str : user) {
            String[] tmp = str.split("=");
            tmp[0] = tmp[0].replace(" ", "");
            tmp[1] = tmp[1].replace(" ", "");
            if(newUser != null && newUser.getUserType() != null){
                break;
            }

            switch (tmp[0]) {

                case "userType":
                    System.out.println("user type in user from string = " + tmp[1]);
                    if(tmp[1].equals(UserType.MANAGER.toString())){
                        newUser = new Manager1();
                    }else if(tmp[1].equals(UserType.PARENT.toString())){
                        newUser = new Parent();
                    }
            }
        }

        for (String str : user){
            String[] tmp = str.split("=");
            tmp[0] = tmp[0].replace(" ","");
            switch (tmp[0]){
                case "uid":
                    newUser.uid = tmp[1];
                    break;
                case "password":
                    newUser.password = tmp[1];
                    break;
                case "institutionName":
                    newUser.institutionName = tmp[1];
                    break;
                case "userType":
                    if(tmp[1].equals(UserType.MANAGER.toString())){
                        newUser.userType = UserType.MANAGER;
                    }else if(tmp[1].equals(UserType.PARENT.toString())){
                        newUser.userType = UserType.PARENT;
                    }
                    break;
                case "userName":
                    newUser.name = tmp[1];
                    break;
                case "email":
                    newUser.email = tmp[1];
                    break;
            }
//            if (newUser.userType == UserType.PARENT){
//                newUser
//            }
        }

        System.out.println("local user from string = "+newUser.toString());
        return newUser;
    }

    public static UserType getUserTypeFromString(String userString){
        LocalUser newUser = null;
        //delete all the "{" and "}"
        userString = userString.replace("{","");
        userString = userString.replace("}","");
        LinkedList<String> user = new LinkedList<>();
        user.addAll(Arrays.asList(userString.split(",")));

        for (String str : user) {
            String[] tmp = str.split("=");
            tmp[0].replace(" ", "");
            switch (tmp[0]) {
                case " userType":
                case "userType":
                    if(tmp[1].equals(UserType.MANAGER.toString())){
                        newUser = new Manager1();
                    }else if(tmp[1].equals(UserType.PARENT.toString())){
                        newUser = new Parent();
                    }
            }
        }
        return newUser.userType;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




    public LocalUser getUserFromDb(String userName){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");


        LocalUser result = null;
        //get the user from the db
        ValueEventListener valueEventListener = new ValueEventListener() {
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
        reference.addValueEventListener(valueEventListener);
        return reference.child(userName).get().getResult().getValue(LocalUser.class);
    }


    public Institution getInstitute() {
        Institution[] retVal = {new Institution()};
        if (institutionName != null){
            //get the Institution from the db by its name
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Institutions").child(institutionName);
            reference.addListenerForSingleValueEvent(new ValueEventListener(){
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    Institution tmp = dataSnapshot.getValue(Institution.class);
                    retVal[0] = tmp;
                    System.out.println("getttt");

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });


            reference.child("trigger").setValue("tmp");
            reference.child("trigger").setValue(null);

        }
        System.out.println("retval" + retVal[0].toString());
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

    public void setBaseAddress(String deviceAddress) {
        this.defaultDeviceAddress = deviceAddress;
    }

    public void setBaseName(String deviceName) {
        this.deviceName = deviceName;
        updateInDb();
    }


    public enum UserType{
        MANAGER,
        PARENT,
        ADMINISTRATOR
    }

    public String getDefaultDeviceAddress() {
        return defaultDeviceAddress;
    }

    public void setDefaultDeviceAddress(String defaultDeviceAddress) {
        this.defaultDeviceAddress = defaultDeviceAddress;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        updateInDb();
    }

    public void updateInDb() {
        if(Config.getCurrentUser() != null) {
            if (uid == null || institutionName == null) {
                return;
            }
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            System.out.println("uid in update in db = " + uid);
            System.out.println("ref = " + ref.getRoot().child("Users").child(uid).toString());
            if (userType == UserType.MANAGER) {
                userToStringInDB(ref.getRoot().child("Users").child(uid));
                userToStringInDB(ref.getRoot().child("Institutions").child(institutionName).child("management").child(String.valueOf(indexInInstitute)));
            } else {
                userToStringInDB(ref.getRoot().child("Users").child(uid));
                userToStringInDB(ref.getRoot().child("Institutions").child(institutionName).child("parents").child(String.valueOf(indexInInstitute)));
            }
        }
   }

   public void userToStringInDB(DatabaseReference ref){
        ref.child("email").setValue(email);
        ref.child("institutionName").setValue(institutionName);
        ref.child("password").setValue(password);
        ref.child("name").setValue(name);
        ref.child("userType").setValue(userType);
        ref.child("indexInInstitute").setValue(indexInInstitute);
        ref.child("defaultDevice").setValue((Object) defaultDeviceAddress);
        ref.child("uid").setValue(uid);
        ref.child("registrationToken").setValue(registrationToken);
        ref.child("deviceName").setValue(deviceName);
        if(userType == UserType.PARENT){
            if(((Parent)this).getChildren().size() >= 1){
                ref.child("children").child("0").setValue(((Parent) this).children.get(0));
                for(int i = 1; i < ((Parent) this).children.size(); i++){
                    ref.child("children").child(String.valueOf(i)).setValue(((Parent) this).children.get(i));
                }
                return;
            }
            ref.child("children").setValue(((Parent) this).children);
        }



   }



    @NonNull
    @Override
    public String toString() {
        return "LocalUser{" +
                ",userType=" + userType +
                ",institutionName=" + institutionName +
                ",userName=" + name +
                ",email=" + email +
                ",password=" + password +
                ",uid=" + uid +
                ",defaultDevice=" + defaultDeviceAddress +
                '}';
    }

    public int getIndexInInstitute() {
        return indexInInstitute;
    }

    public void setIndexInInstitute(int indexInInstitute) {
        this.indexInInstitute = indexInInstitute;
    }

    public String getRegistrationToken() {
        return registrationToken;
    }

    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
