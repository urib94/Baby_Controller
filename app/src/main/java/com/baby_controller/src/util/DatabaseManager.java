package com.baby_controller.src.util;

import com.baby_controller.src.Baby;
import com.baby_controller.src.Institution;
import com.baby_controller.src.LocalUser;
import com.baby_controller.src.Manager1;
import com.baby_controller.src.Parent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DatabaseManager {
    public static DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference(); ;
    ValueEventListener postListener;

    //firebase
    public static FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();;
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static FirebaseAuth.AuthStateListener mAuthListener;
    public static DatabaseReference myRef = mFirebaseDatabase.getReference();;

    private static final String TAG = "databaseManager";



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
                child(LocalUser.UserType.MANAGER.toString()).child(manager.getUserName());
        reference.setValue(manager);
        return reference;
    }


    public static void addNewParent(Institution institution, Manager1 manager, Parent parent){
        dbRef.getRoot().child(institution.getName()).child(LocalUser.UserType.PARENT.toString())
        .child(parent.getUserName()).setValue(parent);
    }

    public static DatabaseReference addNewChild(Parent parent, Baby baby){
        DatabaseReference ref = dbRef.getRoot().child(parent.getInstitutionName());
//                child(parent.getInstitutionName().getManger()).child(parent.get_userName());
//        ref.child(child.getName()).push().setValue(child);;
        return ref;
    }

//    public static DatabaseReference addNewMeal(Child child, Meal meal){
//        DatabaseReference ref = dbRef.getRoot().child(child.getName()).child(child.getName()).
//                child(String.valueOf(child.getHistory().get_curr().get_currDate().getDate()));
//        ref.child(String.valueOf(child.getHistory().get_curr().get_currDate().getDay())).child(String.valueOf(meal.getWhenEaten()) + String.valueOf(child.getHistory().get_curr().get_meals().getAmountOfMeals()))
//        .push().setValue(meal);
//        return ref;
//    }

//  public static DatabaseReference


    //save a Institution to the database, with transactions
    


    //save a Manager to the database


    //save a Parent to the database


    //save a Baby to the database





}
