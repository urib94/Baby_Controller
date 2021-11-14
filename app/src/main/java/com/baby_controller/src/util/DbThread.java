//package com.baby_controller.src.util;
//
//
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.baby_controller.src.Institution;
//import com.baby_controller.src.LocalUser;
//import com.baby_controller.src.Manager1;
//import com.baby_controller.src.Parent;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.ChildEventListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//
//import java.util.LinkedList;
//
////object that manege the interaction with the database
//public class DbThread extends Thread{
//
//    public FirebaseAuth mAuth;
//    public DatabaseReference myRef;
//    public LinkedList<int[]> tasks;
//    public static String TAG = "DbThread";
//
//    public LocalUser userToDb(LocalUser user,boolean updateLocally){
//
//        String uid = user.getUid();
//        if(mAuth.getCurrentUser() != null) {
//            uid = mAuth.getCurrentUser().getUid();
//        }
//
//        myRef.getRoot().addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                boolean notNew = false;
//                LinkedList<Institution> institutionLinkedList = new LinkedList<>();
//                System.out.println("snap before loop"+snapshot.toString());
//
//                for (DataSnapshot snap : snapshot.getChildren()) {
//                    int i = 0;
//                    try {
//                        Institution tmp = snap.getValue(Institution.class);
//                        if (tmp != null && tmp.getName() != null) {
//                            institutionLinkedList.add(snap.getValue(Institution.class));
//                        }
//                    } catch (Exception e) {
//                        Log.d(TAG, "onChildAdded: " + e.getMessage());
//                    }
//                    if (institutionLinkedList.size() >= 1) {
//                        if (user.getUserType() == LocalUser.UserType.MANAGER) {
//                            if (institutionLinkedList.get(institutionLinkedList.size() - 1).getName().equals(user.getInstitutionName())) {
//                                institutionLinkedList.getLast().getManagement().add(user);
//                                if(!updateLocally) {
//                                    {
//                                        snap.getRef().setValue(institutionLinkedList.getLast());
//                                    }
//                                notNew = true;
//                                break;
//
//                            }
//                        } else {
//                            if (institutionLinkedList.size() >= 1 && institutionLinkedList.
//                                    get(institutionLinkedList.size() - 1).getName().equals(user.getInstitutionName())) {
//                                Parent newParent = (Parent) user;
//                                institutionLinkedList.getLast().getParents().add(newParent);
//                                snap.getRef().setValue(institutionLinkedList.getLast());
//                                notNew = true;
//                                break;
//                            }
//                        }
//                    }
//
//                }
//                if(!notNew){
//                    if(user.getUserType() == LocalUser.UserType.MANAGER) {
//                        Institution institution = new Institution((Manager1) user, user.getInstitutionName());
//                        // make the user the admin of the Institution
//                        System.out.println("new institute");
//                        myRef.getRoot().child("Institutions").child(user.getInstitutionName()).setValue(institution);
//                        myRef.child("Users").child(user.getUid()).setValue((Manager1) user);
//
//                    }else{
//                        Log.d(TAG, "unable to upload user to db");
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
//
//        myRef.child("trigger").setValue("trigger");
//        myRef.child("trigger").setValue(null);
//        return user;
//    }
//
//
//
//}
