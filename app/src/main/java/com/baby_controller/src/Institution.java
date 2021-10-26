package com.baby_controller.src;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Institution {
    private String name;
    private DatabaseReference reference;
    LinkedList<Manager1> management = new LinkedList<>();
    LinkedList<Parent> parents = new LinkedList<>();

    public Institution(){
    }



    public Institution (Manager1 manager, String name){
        this.name = name;
//        manager.setInstitutionName(this);
        this.management.add(manager);
    }



    public Manager1 getManger(String userName){
        for(int i = 0; i < management.size(); i++){
            if(management.get(i).getUserName().equals(userName)){
                if(management.get(i).getUserType() == User.UserType.PARENT) {
                    return (Manager1) management.get(i);
                }
            }
        }
        return null;
    }

    public Parent getParent(String userName){
        for(int i = 0; i < parents.size(); i++){
            if(parents.get(i).getUserName().equals(userName)){
                if(parents.get(i).getUserType() == User.UserType.PARENT) {
                    return (Parent) parents.get(i);
                }
            }
        }
        return null;
    }


    public boolean addManager(Manager1 manager){
        if (management.size() == 0){
            manager.setInstitution(this);
            management.add(manager);
            manager.uploadToDb();
        }
        if(getManger(manager.getUserName()) == null){
            manager.setInstitution(this);
            this.management.add(manager);
            manager.uploadToDb();
            return true;
        }
        return  false;
    }

    public boolean addParent(Parent parent){
        if(getParent(parent.getUserName()) == null){
            this.parents.add(parent);
//
            //         DatabaseManager.addNewManager(this,manager);
            return true;
        }
        return  false;
    }
    public void getValueFromDb(Institution institution){
        this.parents = institution.parents;
        this.name = institution.name;
        this.management = institution.management;
    }

    public String getName() {
        return name;
    }

    public List<Manager1> getManagement() {
        return management;
    }

    public void setManagement(LinkedList<Manager1> management) {
        this.management = management;
    }

    public LinkedList<Parent> getParents() {
        return parents;
    }

    public void setParents(LinkedList<Parent> parents) {
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

    public DatabaseReference uploadToDb() {
        reference = FirebaseDatabase.getInstance().getReference().child(name);
        for(Parent parent: parents){
            parent.setInstitution(this);
            parent.uploadToDb();
        }
        for (Manager1 man: management){
            man.setInstitution(this);
            man.uploadToDb();
        }

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
               //Object tmp = dataSnapshot.getValue(Institution.class);
//               name = tmp.name;
//               parents = tmp.parents;
//               management = tmp.management;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

            }
        };
        reference.addValueEventListener(postListener);


        return reference;
    }

    public Baby needToFeed(){
        for (Parent parent : getParents()){
            Baby baby = parent.babyNeedToFeed();
            if(baby != null){
                return baby;
            }
        }
        return null;
    }
}
