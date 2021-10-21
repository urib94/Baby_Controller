package com.baby_controller.src.util;

import com.baby_controller.src.Child;
import com.baby_controller.src.Institution;
import com.baby_controller.src.Manager1;
import com.baby_controller.src.Meal;
import com.baby_controller.src.Parent;
import com.baby_controller.src.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DatabaseManager {
    public static DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference(); ;
    ValueEventListener postListener;

    public DatabaseManager(){

        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                // TODO: 10/21/2021 what to do hear
                // ..
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // TODO: 10/21/2021 what to do hear

            }
        };
        dbRef.addValueEventListener(postListener);
    }


    public static DatabaseReference addNewInstitution(Institution institution){
        dbRef.getRoot().child(institution.getName()).setValue(institution.toString());
        return dbRef;
    }

    public static DatabaseReference addNewManager(Institution institution, Manager1 manager){
        DatabaseReference reference = dbRef.getRoot().child(institution.getName()).
                child(User.UserType.MANAGER.toString()).child(manager.get_userName());
        reference.setValue(manager);
        return reference;
    }


    public static void addNewParent(Institution institution, Manager1 manager, Parent parent){
        dbRef.getRoot().child(institution.getName()).child(User.UserType.PARENT.toString())
        .child(parent.get_userName()).setValue(parent);
    }

    public static DatabaseReference addNewChild(Parent parent, Child child){
        DatabaseReference ref = dbRef.getRoot().child(parent.getInstitution().getName());
//                child(parent.getInstitution().getManger()).child(parent.get_userName());
//        ref.child(child.getName()).push().setValue(child);;
        return ref;
    }

    public static DatabaseReference addNewMeal(Child child, Meal meal){
        DatabaseReference ref = dbRef.getRoot().child(child.getName()).child(child.getName()).
                child(String.valueOf(child.getHistory().get_curr().get_currDate().getDate()));
        ref.child(String.valueOf(child.getHistory().get_curr().get_currDate().getDay())).child(String.valueOf(meal.get_whenEaten()) + String.valueOf(child.getHistory().get_curr().get_meals().getAmountOfMeals()))
        .push().setValue(meal);
        return ref;
    }

//    public static DatabaseReference




}