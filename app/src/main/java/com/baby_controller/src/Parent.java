package com.baby_controller.src;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.baby_controller.src.util.DatabaseManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Parent extends LocalUser {
    protected LinkedList<Baby> children = new LinkedList<>();;

    public Parent(){}

    public Parent(String email,String userName, String password, UserType userType) {
        super(email, userName, password, userType);

    }

    public Parent(String userName,String email, String password) {
        super(email,userName, password, UserType.PARENT);
    }


    public List<Baby> getChildren() {
        return children;
    }


    public Baby getChild(String name, int id) {
        for (Baby child : children) {
            if (child.getName().equals(name) && child.getId() == id) {
                return child;
            }
        }
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
        reference = FirebaseDatabase.getInstance().getReference().child(this.getInstitute().getName()).
                child(LocalUser.UserType.PARENT.toString()).child(getUserName());
        reference.setValue(toJson());

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Parent tmp = Parent.fromJson(dataSnapshot.getValue(JSONObject.class));
                userName = tmp.userName;
                email = tmp.email;
                password = tmp.password;
                institutionName = tmp.institutionName;
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

    public void setChildren(LinkedList<Baby> children) {
        this.children = children;
    }

    //get all the Babies that need to be fed
    public LinkedList<Baby> getBabiesNeedToFeed() {
        LinkedList<Baby> babiesNeedToFeed = new LinkedList<>();
        for(Baby baby: children){
            Meal last = baby.history.get(baby.history.size() -1);
            Time now = new Time(System.currentTimeMillis());
            if (now.after(last.getTimeToEat())){
                babiesNeedToFeed.add(baby);
            }
        }
        return babiesNeedToFeed;
    }

    //upload the parent to the database as a transaction
    public void uploadToDb1(DatabaseReference ref){
        ref.child(this.getInstitute().getName()).
                child(LocalUser.UserType.PARENT.toString()).
                child(getUserName()).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Parent tmp = mutableData.getValue(Parent.class);
                if (tmp == null){
                    mutableData.setValue(Parent.this);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                if (databaseError != null){
                    Log.d("TAG", "onComplete: " + databaseError.getMessage());
                }
            }
        });
    }


    //set listners that update this Parent when its changes in the database
    public void setListners(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Parent tmp = dataSnapshot.getValue(Parent.class);
                userName = tmp.userName;
                email = tmp.email;
                password = tmp.password;
                institutionName = tmp.institutionName;
                children = tmp.children;
                userType = tmp.userType;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Parent to json
    public JSONObject toJson(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", userName);
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            jsonObject.put("institute", institutionName);
            jsonObject.put("children", children);
            jsonObject.put("userType", userType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    //json to Parent
    public static Parent fromJson(JSONObject jsonObject){
        Parent parent = new Parent();
        try {
            parent.userName = jsonObject.getString("userName");
            parent.email = jsonObject.getString("email");
            parent.password = jsonObject.getString("password");
//            parent.institutionName = Institution.fromJson(jsonObject.getJSONObject("institute"));

            for (int i = 0; i < jsonObject.getJSONArray("children").length(); i++){
                parent.children.add(Baby.fromJson(jsonObject.getJSONArray("children").getJSONObject(i)));
            }

            parent.userType = UserType.valueOf(jsonObject.getString("userType"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parent;
    }

    //save Parent to firebase as a transaction
    public void saveToDb(DatabaseReference ref){
        ref.child(this.getInstitute().getName()).
                child(LocalUser.UserType.PARENT.toString()).
                child(getUserName()).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Parent tmp = mutableData.getValue(Parent.class);

                if (tmp == null){
                    int a = mutableData.getValue(Parent.class).toString().length()/1024;
                    mutableData.setValue(Parent.this);
                    //print parent size in kb

                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                if (databaseError != null){
                    Log.d("TAG", "onComplete: " + databaseError.getMessage());
                }
            }
        });
    }
}
