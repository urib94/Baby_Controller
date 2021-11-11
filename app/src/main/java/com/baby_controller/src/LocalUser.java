package com.baby_controller.src;

import android.bluetooth.BluetoothDevice;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;


public class LocalUser {
    protected DatabaseReference reference;
    protected UserType userType;
    protected String institutionName;
    protected String userName;



    protected String email;
    protected String password;
    protected String uid;
    protected BluetoothDevice defaultDevice = null;



    public LocalUser(){}

    public LocalUser(String email ,String userName, String password, UserType userType){

        this.email = email;
        this.userName = userName;
        this.password = password;
        this.userType = userType;

    }

    //coppy other user
    public LocalUser(LocalUser other){
        this.userName = other.userName;
        this.password = other.password;
        this.email = other.email;
        this.institutionName = other.institutionName;
        this.userType = other.userType;
        this.uid = other.uid;
        this.reference = other.reference;
        this.defaultDevice = other.defaultDevice;
    }

    public LocalUser(String email, String userName, String password, UserType userType, String uid, String instituteName) {
        this.email = email;
        this.userName = userName;
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
                    newUser.userName = tmp[1];
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
        updateInDb();
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
        updateInDb();
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
        final Institution[] retVal = {null};
        if (institutionName != null){
            //get the Institution from the db by its name
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Institutions").child(institutionName);
            ValueEventListener valueEventListener = new ValueEventListener() {
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
        updateInDb();
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
        updateInDb();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        updateInDb();
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
        System.out.println("defaultDevice = " + defaultDevice.toString());
        updateInDb();
    }

    private void updateInDb() {
        if(Config.getCurrentUser() != null) {
            LocalUser tmp = this;
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    //update under "Users"
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if (tmp.getUid().equals(Objects.requireNonNull(ds.getValue(LocalUser.class)).getUid())) {
                            ds.getRef().setValue(tmp);
                            System.out.println("updating user");
                        }
                        if (Objects.equals(ds.getKey(), Config.getCurrentUser().getUid())) {
                            ds.getRef().setValue(LocalUser.this);
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            };
            ref.addValueEventListener(valueEventListener);

            //update under the Institution

            if (this.getUserType() == UserType.PARENT) {
                //make a new Parent object from the current user
                Parent parent = new Parent(this);

                System.out.println("parent defaultDevice = " + parent.getDefaultDevice().toString());
                ref.child("Institutions").child(institutionName).child("parents").setValue(parent);
            } else if (this.getUserType() == UserType.MANAGER) {
                //make a new Manager1 object from the current user
                Manager1 manager1 = new Manager1( this);
                Parent parent1= Parent.fromString("{reference=null, userType=PARENT, institutionName='uri', userName='uri', email='uri@uri.com', password='uriuri', uid='8lgwFdWbREgUt1Uu7pHYvmW17EA2', defaultDevice=null}");
                parent1.addNewChild(new Baby("newborn",75.0));
                System.out.println("parent1 amount of kids" + parent1.children.size());
                System.out.println("parent 1 TOSTRING()"+parent1.toString());
                Parent parent = Parent.fromString(parent1.toString());
                System.out.println("parent. tostring() = " + parent.toString());
                System.out.println("manger1 == " + manager1.toString());
                System.out.println(ref.getRoot().child("Users").child(uid).toString());

//                ref.getRoot().child("Users").child(uid).setValue(manager1);


//            Manager1 manager1 = new Manager1(((Manager1) this));
                if (manager1.getDefaultDevice() != null) {
                    System.out.println("manager defaultDevice = " + manager1.getDefaultDevice().toString());
                } else {
                    System.out.println("manager defaultDevice = null");
                }
                ref.child("Institutions").child(institutionName).child("management").child(uid).setValue(manager1.toString());

            }


//           ref.child("Institutions").child(institutionName).child("management").addValueEventListener(valueEventListener);
//        }else {
//            ref.child("Institutions").child(institutionName).child("parents").addValueEventListener(valueEventListener);
//        }
//        System.out.println("uuiidd = " + uid);
//        // update under "users"
//        if(userType == UserType.MANAGER) {
//            ref.getRoot().child("Users").child(uid).setValue((Manager1)LocalUser.this);
//        }else{
//            ref.getRoot().child("Users").child(uid).setValue((Parent)LocalUser.this);
//        }
        }
    }



    @NonNull
    @Override
    public String toString() {
        return "LocalUser{" +
                "reference=" + reference +
                ",userType=" + userType +
                ",institutionName=" + institutionName +
                ",userName=" + userName +
                ",email=" + email +
                ",password=" + password +
                ",uid=" + uid +
                ",defaultDevice=" + defaultDevice +
                '}';
    }
}
