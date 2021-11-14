package com.baby_controller.src;

import android.util.Log;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Keep
public class Institution{
    private String name;
    private DatabaseReference reference;
    protected List<LocalUser> management = new ArrayList<>();
    protected List<Parent> parents =     new ArrayList<>();


    public Institution(){
    }



    public Institution (Manager1 manager, String name){

        this.name = name;
        manager.setInstitutionName(name);
        this.management.add(manager);
    }




    public Manager1 getManger(String userName){
        for(int i = 0; i < management.size(); i++){
            if(management.get(i).getName().equals(userName)){
                if(management.get(i).getUserType() == LocalUser.UserType.PARENT) {
                    return (Manager1) management.get(i);
                }
            }
        }
        return null;
    }

    //set management by LinkedList
    public void setManagement(ArrayList<LocalUser> management) {
        this.management = management;
    }

    public Parent getParent(String name){
        for(int i = 0; i < parents.size(); i++){
            if(parents.get(i).getName().equals(name)){
                if(parents.get(i).getUserType() == LocalUser.UserType.PARENT) {
                    return (Parent) parents.get(i);
                }
            }
        }
        return null;
    }

    public boolean addManager(Manager1 manager){
        if(getManger(manager.getName()) == null){
            manager.getInstitute();
            management.add(manager);
            return true;
        }
        return  false;
    }

    public boolean addParent(Parent parent){
        if(getParent(parent.getName()) == null){
            parent.setInstitutionName(name);
            this.parents.add(parent);
            return true;
        }
        return  false;
    }
    
    public String getName() {
        return name;
    }

    public List<LocalUser> getManagement() {
        return management;
    }

    public List<Parent> getParents() {
        return parents;
    }

    public void setParents(ArrayList<Parent> parents) {
        this.parents = parents;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DatabaseReference getReference() {
        return reference;
    }

    public void setReference(DatabaseReference reference) {
        this.reference = reference;
    }



    public Baby needToFeed(){
        for (LocalUser parent : getParents()){
            Baby baby = ((Parent)parent).babyNeedToFeed();
            if(baby != null){
                return baby;
            }
        }
        return null;
    }

    // get the list of all the babies that need to be fed
    public LinkedList<Baby> getBabiesNeedToFeed(){
        LinkedList<Baby> babies = new LinkedList<>();
        for (LocalUser parent : getParents()){
            babies.addAll(((Parent)parent).getBabiesNeedToFeed());
        }
        return babies;
    }

    // get the list of all the babies that dont need to be fed
    public LinkedList<Baby> getBabiesNotNeedToFeed(){
        LinkedList<Baby> babies = new LinkedList<>();
        for (LocalUser parent : getParents()){
            babies.addAll(((Parent)parent).getBabiesNotNeedToFeed());
        }
        return babies;
    }


//    //get this from firebase
//    public void getInstitutionFromDb(DatabaseReference dbReference) {
//        ValueEventListener postListener = new ValueEventListener() {
//            Institution tmp;
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Get Post object and use the values to update the UI
//                tmp = dataSnapshot.getValue(Institution.class);
//                //update this Institution with the dataSnapshot
//                if (tmp != null) {
//                    setManagement(tmp.getManagement());
//                    setParents(tmp.getParents());
//                    setName(tmp.getName());
//                }
//            }
//
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // TODO: 11/5/2021  writ action option for the different error possibilities
//                // Getting Post failed, log a message
//            }
//        };
//        dbReference.addValueEventListener(postListener);
//    }

    //update the database if this local copy is changed

    public void updateDb(){
        reference = FirebaseDatabase.getInstance().getReference().child("Institutions").child(name);
        Transaction.Handler tmp = new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                currentData.setValue(Institution.this);
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
    }


    public static Institution findInstitution(String name){
        Institution newOne = new Institution();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Institutions").child(name);
        ValueEventListener postListener = new ValueEventListener() {

            Institution tmp;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                tmp = dataSnapshot.getValue(Institution.class);
                //update this Institution with the dataSnapshot
                if (tmp != null) {
                    newOne.coppy(tmp);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO: 11/5/2021  writ action option for the different error possibilities
                // Getting Post failed, log a message
            }
        };
            ref.addValueEventListener(postListener);
            return newOne;
        }


// clone
    public Institution clone(){
        Institution newOne = new Institution();
        newOne.management = this.management;
        newOne.parents = this.parents;
        newOne.setName(this.name);
        return newOne;
    }

    //coppy
    public void coppy(Institution institution){
        this.management = institution.management;
        this.parents = institution.parents;
        this.name = institution.name;
    }
}
