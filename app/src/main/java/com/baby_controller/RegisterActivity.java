package com.baby_controller;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "AddToDatabase";
    private String userUID;


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

                        newUser = new Parent(user_name, mail, pass);
                        newUser.setInstitutionName(instName);

                    }else {
                        newUser = new Manager1(user_name, mail, pass);
                    }
                        // if the institution name is not on the database, create one
                        myRef.getRoot().child("Institutions").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(newUser.getUserType()== LocalUser.UserType.MANAGER) {
                                    if (dataSnapshot.child(instName).getValue(Institution.class) == null) {
                                        //make a new Institution
                                        Institution institution = new Institution((Manager1) newUser, instName);
                                        // make the user the admin of the Institution
                                        myRef.getRoot().child("Institutions").child(instName).setValue(institution);
                                    } else {
                                        myRef.child("Users").child(userUID).setValue((Manager1)newUser);
                                        myRef.getRoot().child("Institutions").child(instName).setValue(dataSnapshot.child(instName)
                                                .getValue(Institution.class).addManager((Manager1) newUser));
                                        myRef.child("Users").child(userUID).setValue((Manager1)newUser);
                                    }
                                }else{
                                    if (dataSnapshot.child(instName).getValue(Institution.class) == null) {
                                        Log.d(TAG, "attempt to register to unavailable institute");
                                        toastMessage("this institute is not register to our service.\n try a different" +
                                                "one or contact are support team");
                                    } else {
                                        myRef.child("Users").child(userUID).setValue((Parent) newUser);
                                        myRef.getRoot().child("Institutions").child(instName).setValue(dataSnapshot.child(instName)
                                                .getValue(Institution.class).addParent((Parent) newUser));
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {}
                        });

                    toastMessage("\tWelcome!\t");
                    userName.setText("");
                    email.setText("");
                    password.setText("");
                    institutionName.setText("");

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

//    @Override
//    public void onStop() {
//        super.onStop();
//    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    public void register(String mail, String pass) {
        mAuth.createUserWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("success", "createUserWithEmail:success");
                            toastMessage("Successfully registered");
//                            newUser();
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("failure", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
        FirebaseUser user = mAuth.getCurrentUser();
        userUID = user.getUid();
    }

//    private void newUser() {
////        LocalUser curr = null;
//        Institution institution;
//        FirebaseUser user = mAuth.getCurrentUser();
//        LocalUser.UserType userType;
//        if(isParent.isChecked()){
//
//
//            curr = new Parent(userName.getText().toString(),email.getText().toString()
//                    ,password.getText().toString());
//        }else{
//            curr = new Manager1(userName.getText().toString(),email.getText().toString()
//                    ,password.getText().toString());
//        }
//        String mail = email.getText().toString();
//        String nameOfUser = userName.getText().toString();
//
//        if(isParent.isChecked()) {
//            institution = Institution.findInstitution(institutionName.getText().toString());
//        }else {
//            institution = new Institution((Manager1) curr,institutionName.getText().toString());
//        }
//        System.out.println("email =  " + email.getText().toString());
//        System.out.println("username =  "+ userName.getText().toString());
//        System.out.println("\n\n\n\n");
//
//
//
//        curr.setInstitutionName(institution.getName());
//        if(curr.getUserType() == LocalUser.UserType.PARENT){
//            assert curr instanceof Parent;
//            institution.addParent((Parent) curr);
//        }else {
//            assert curr instanceof Manager1;
//            institution.addManager((Manager1) curr);
//        }
//        Config.CUUR_USER = curr;
//
//        institution.setName(institutionName.getText().toString());
//        institution.uploadToDb();
//        curr.uploadNewUser();
//        System.out.println("\n\n\n\n\n\n "+ institution.toString());
//
//
//
//    }

    private void updateUI(FirebaseUser user) {
    }

}
