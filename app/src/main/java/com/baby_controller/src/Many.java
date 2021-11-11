package com.baby_controller.src;


import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Many   {

    static Institution institution = new Institution();
    static LocalUser localUser = new LocalUser();
    static Baby baby = new Baby();
    static Meal meal = new Meal();
    static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public Many() {
    }

    //get a Institution object from the database, find it by the name
    public static Institution getInstitution(String name) {
        institution.setName(name);

        databaseReference.child("Institution");
        databaseReference.child(institution.getName());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    institution.setParents((ArrayList<Parent>) (snapshot.child("parents").getValue(List.class)));
                    institution.setManagement((ArrayList<LocalUser>) (snapshot.child("management").getValue(List.class)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return institution;
    }

    public static void main(String[] args) {
        Institution institution = new Institution();
        institution = getInstitution("uri");
        System.out.println(institution.toString());
    }

}

