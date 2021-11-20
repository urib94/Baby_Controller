package com.baby_controller;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.baby_controller.src.Institution;
import com.baby_controller.src.LocalUser;
import com.baby_controller.src.Manager1;
import com.baby_controller.src.Parent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "AddToDatabase";
    private String userUID;
    boolean completed = false;


    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private LocalUser newUser;


    //buttons
    private Button confirmRegisterButton;
    private TextView institutionName;
    private TextView password;
    private TextView email;
    private Switch isParent;
    private TextView userName;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        configButtons();
        //declare the database reference object. This is what we use to access the database.
        //NOTE: Unless you are signed in, this will not be useable.
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Successfully signed out.");
                }
                // ...
            }
        };


    }

    public void userToDb(){

        String instName = institutionName.getText().toString();
        if(mAuth.getCurrentUser() == null) {
            return;
        }
        myRef.getRoot().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(completed){
                    return;
                }
                userUID = mAuth.getCurrentUser().getUid();

                boolean notNew = false;
                LinkedList<Institution> institutionLinkedList = new LinkedList<>();
                System.out.println("snap before loop"+snapshot.toString());
                System.out.println("completed = " + completed);
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        int i = 0;
                        System.out.println("snap in loop" + snap.toString());
                        System.out.println("i = " + i++);
                        try {
                            DatabaseReference reference = snap.getRef();
                            Institution tmp = snap.getValue(Institution.class);
                            if (tmp != null && tmp.getName() != null) {
                                institutionLinkedList.add(snap.getValue(Institution.class));
                            }
                        } catch (Exception e) {
                            Log.d(TAG, "onChildAdded: " + e.getMessage());
                            continue;
                        }
                        if (institutionLinkedList.size() >= 1) {
                            if (newUser.getUserType() == LocalUser.UserType.MANAGER) {
                                if (institutionLinkedList.get(institutionLinkedList.size() - 1).getName().equals(instName)) {
                                    Institution institution = institutionLinkedList.getLast();
                                    setNewUserIndexInInstitue(institution);
                                    System.out.println(institution.getManagement().toString());
                                    newUser.setInstitutionName(institution.getName());
                                    institution.getManagement().add(newUser);
//                                    institutionLinkedList.getLast().getManagement().add(newUser);
//                                    snap.getRef().setValue(institutionLinkedList.getLast());
                                    snap.getRef().setValue(institution);
                                    snap.getRef().getRoot().child("Users").child(userUID).setValue(newUser);
                                    System.out.println("FIANALY MADE IT");
                                    completed = true;
                                    notNew = true;
                                    break;

                                }
                            } else {
                                if (institutionLinkedList.size() >= 1 && institutionLinkedList.
                                        get(institutionLinkedList.size() - 1).getName().equals(instName)) {
                                    Institution institution = institutionLinkedList.getLast();
                                    setNewUserIndexInInstitue(institution);
                                    Parent newParent = (Parent) newUser;
                                    System.out.println("parents list in the institute = " + institution.getParents().toString());
                                    institution.getParents().add(newParent);
                                    newParent.setInstitutionName(institution.getName());
                                    snap.getRef().setValue(institution);
                                    snap.getRef().getRoot().child("Users").child(userUID).setValue(newUser);
                                    System.out.println("FIANALY MADE IT");
                                    notNew = true;
                                    completed = true;
                                    break;
                                }
                            }
                        }

                }
                if(!notNew){
                    System.out.println("jobs don =" + completed);
                    if(newUser.getUserType() == LocalUser.UserType.MANAGER) {
                        Institution institution = new Institution((Manager1) newUser, instName);
                        // make the user the admin of the Institution
                        System.out.println("new institute");
                        myRef.getRoot().child("Institutions").child(instName).setValue(institution);
                        myRef.child("Users").child(userUID).setValue((Manager1) newUser);
                        completed = true;
                    }else{
                        Log.d(TAG, "attempt to register to unavailable institute");
                        mAuth.getCurrentUser().delete();
                        toastMessage("this institute is not register to our service.\n try a different" +
                                "one or contact are support team");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }
        });
    }

    private void setNewUserIndexInInstitue(Institution institution) {
        if(newUser.getUserType() == LocalUser.UserType.MANAGER){
            newUser.setIndexInInstitute(institution.getManagement().size());
        }else {
            newUser.setIndexInInstitute(institution.getParents().size());
        }

    }

    private void configButtons() {
        //configure the buttons
        confirmRegisterButton = (Button)  findViewById(R.id.confirm_register);
        institutionName = (TextView) findViewById(R.id.reg_institution);
        password = (TextView) findViewById(R.id.reg_password);
        email = (TextView) findViewById(R.id.reg_email_address);
        isParent = findViewById(R.id.is_parent);
        userName = (TextView) findViewById(R.id.user_name);

        confirmRegisterButton.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Submit pressed.");
                String user_name = userName.getText().toString();
                String mail = email.getText().toString();
                String pass = password.getText().toString();
                String instName = institutionName.getText().toString();
                boolean isParentBool = isParent.isChecked();

                Log.d(TAG, "onClick: Attempting to submit to database: \n" +
                        "userName: " + userName + "\n" +
                        "email: " + email + "\n" +
                        "password: " + pass + "\n"
                );

                //handle the exception if the EditText fields are null
                if(!userName.equals("") && !mail.equals("") && !pass.equals("")){
                    register(mail, pass);

                    if(isParentBool){
                        newUser = new Parent(user_name, mail, pass,mAuth.getUid(),instName);

                    }else {
                        newUser = new Manager1(user_name, mail, pass);
                    }

                }else{
                    toastMessage("Fill out all the fields");
                }
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    public void register(String mail, String pass) {


        mAuth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("success", "createUserWithEmail:success");
                            toastMessage("Successfully registered");
                            newUser.setUid(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                            userToDb();
                            myRef.getRoot().child("Listener Trigger").setValue(" ");
                            myRef.getRoot().child("Listener Trigger").setValue(null);

                            toastMessage("\tWelcome!\t");
                            userName.setText("");
                            email.setText("");
                            password.setText("");
                            institutionName.setText("");
                            if(newUser.getUserType() == LocalUser.UserType.MANAGER){
                                //todo make diffrent cases to ech user types
                                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                            }
                            releaseInstance();
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("failure", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}
