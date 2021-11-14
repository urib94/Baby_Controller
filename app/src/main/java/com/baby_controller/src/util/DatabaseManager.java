//package com.baby_controller.src.util;
//
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.baby_controller.src.Baby;
//import com.baby_controller.src.Institution;
//import com.baby_controller.src.LocalUser;
//import com.baby_controller.src.Manager1;
//import com.baby_controller.src.Parent;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.ChildEventListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.LinkedList;
//
//
//public class DatabaseManager {
//    public static DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference(); ;
//    ValueEventListener postListener;
//
//    //firebase
//    public static FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();;
//    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();
//    public static FirebaseAuth.AuthStateListener mAuthListener;
//    public static DatabaseReference myRef = mFirebaseDatabase.getReference();;
//
//    private static final String TAG = "databaseManager";
//
//
//
//
//    public DatabaseManager(){
//        postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Get Post object and use the values to update the UI
//                // TODO: 10/21/2021 what to do hear
//                // ..
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Getting Post failed, log a message
//                // TODO: 10/21/2021 what to do hear
//
//            }
//        };
//        dbRef.addValueEventListener(postListener);
//    }
//
//
//
//    public void userToDb(DatabaseReference myRef, Class objectType,String instName, String uid){
//
//
//        if(mAuth.getCurrentUser() != null) {
//            uid = mAuth.getCurrentUser().getUid();
//        }
//        myRef.getRoot().addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                if(completed){
//                    return;
//                }
//
//                boolean notNew = false;
//                LinkedList<Institution> institutionLinkedList = new LinkedList<>();
//                System.out.println("snap before loop"+snapshot.toString());
//                System.out.println("completed = " + completed);
//                for (DataSnapshot snap : snapshot.getChildren()) {
//                    int i = 0;
//                    System.out.println("snap in loop" + snap.toString());
//                    System.out.println("i = " + i++);
//                    try {
//                        DatabaseReference reference = snap.getRef();
//                        Institution tmp = snap.getValue(Institution.class);
//                        if (tmp != null && tmp.getName() != null) {
//                            institutionLinkedList.add(snap.getValue(Institution.class));
//                        }
//                    } catch (Exception e) {
//                        Log.d(TAG, "onChildAdded: " + e.getMessage());
//                    }
//                    if (institutionLinkedList.size() >= 1) {
//                        if (newUser.getUserType() == LocalUser.UserType.MANAGER) {
//                            if (institutionLinkedList.get(institutionLinkedList.size() - 1).getName().equals(instName)) {
//                                institutionLinkedList.getLast().getManagement().add(newUser);
//                                snap.getRef().setValue(institutionLinkedList.getLast());
//                                System.out.println("FIANALY MADE IT");
//                                completed = true;
//                                notNew = true;
//                                break;
//
//                            }
//                        } else {
//                            if (institutionLinkedList.size() >= 1 && institutionLinkedList.
//                                    get(institutionLinkedList.size() - 1).getName().equals(instName)) {
//                                Parent newParent = (Parent) newUser;
//                                institutionLinkedList.getLast().getParents().add(newParent);
//                                snap.getRef().setValue(institutionLinkedList.getLast());
//                                System.out.println("FIANALY MADE IT");
//                                notNew = true;
//                                completed = true;
//                                break;
//                            }
//                        }
//                    }
//
//                }
//                if(!notNew){
//                    System.out.println("jobs don =" + completed);
//                    if(newUser.getUserType() == LocalUser.UserType.MANAGER) {
//                        Institution institution = new Institution((Manager1) newUser, instName);
//                        // make the user the admin of the Institution
//                        System.out.println("new institute");
//                        myRef.getRoot().child("Institutions").child(instName).setValue(institution);
//                        myRef.child("Users").child(uid).setValue((Manager1) newUser);
//                        completed = true;
//                    }else{
//                        Log.d(TAG, "attempt to register to unavailable institute");
//                        firebaseUser.delete();
//                        toastMessage("this institute is not register to our service.\n try a different" +
//                                "one or contact are support team");
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//            }
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//            }
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//        });
//    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    public static DatabaseReference addNewInstitution(Institution institution){
//        dbRef.getRoot().child(institution.getName()).setValue(institution.toString());
//        return dbRef;
//    }
//
//    public static DatabaseReference addNewManager(Institution institution, Manager1 manager){
//        DatabaseReference reference = dbRef.getRoot().child(institution.getName()).
//                child(LocalUser.UserType.MANAGER.toString()).child(manager.getUserName());
//        reference.setValue(manager);
//        return reference;
//    }
//
//
//    public static void addNewParent(Institution institution, Manager1 manager, Parent parent){
//        dbRef.getRoot().child(institution.getName()).child(LocalUser.UserType.PARENT.toString())
//        .child(parent.getUserName()).setValue(parent);
//    }
//
//    public static DatabaseReference addNewChild(Parent parent, Baby baby){
//        DatabaseReference ref = dbRef.getRoot().child(parent.getInstitutionName());
////                child(parent.getInstitutionName().getManger()).child(parent.get_userName());
////        ref.child(child.getName()).push().setValue(child);;
//        return ref;
//    }
//
////    public static DatabaseReference addNewMeal(Child child, Meal meal){
////        DatabaseReference ref = dbRef.getRoot().child(child.getName()).child(child.getName()).
////                child(String.valueOf(child.getHistory().get_curr().get_currDate().getDate()));
////        ref.child(String.valueOf(child.getHistory().get_curr().get_currDate().getDay())).child(String.valueOf(meal.getWhenEaten()) + String.valueOf(child.getHistory().get_curr().get_meals().getAmountOfMeals()))
////        .push().setValue(meal);
////        return ref;
////    }
//
////  public static DatabaseReference
//
//
//    //save a Institution to the database, with transactions
//
//
//
//    //save a Manager to the database
//
//
//    //save a Parent to the database
//
//
//    //save a Baby to the database
//
//
//
//
//
//}
