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

import com.baby_controller.src.Config;
import com.baby_controller.src.Institution;
import com.baby_controller.src.Manager1;
import com.baby_controller.src.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button confirmRegisterButton;
    private TextView institutionName;
    private TextView password;
    private TextView email;
    private Switch isParent;
    private TextView name;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        mAuth = FirebaseAuth.getInstance();
        configButtons();
    }
    private void configButtons() {
        //configure the buttons
        confirmRegisterButton = (Button)  findViewById(R.id.confirm_register);
        institutionName = (TextView) findViewById(R.id.reg_institution);
        password = (TextView) findViewById(R.id.reg_password);
        email = (TextView) findViewById(R.id.reg_email_address);
        isParent = findViewById(R.id.is_parent);
        name = (TextView) findViewById(R.id.reg_name);

        confirmRegisterButton.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(v);
            }
        });


    }


    public void register(View view) {

        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("success", "createUserWithEmail:success");
                            newUser();
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
    }

    private void newUser() {

        Institution institution;
        FirebaseUser user = mAuth.getCurrentUser();
        User.UserType userType;
        if(isParent.isChecked()){
            userType = User.UserType.PARENT;
        }else userType = User.UserType.MANAGER;
        User curr = new User(name.getText().toString(),email.getText().toString()
                ,password.getText().toString(),userType);
        if(isParent.isChecked()) {
            institution = Institution.findInstitution(institutionName.getText().toString());
        }else {
            institution = new Institution((Manager1) curr,institutionName.getText().toString());
        }

        curr.setInstitution(institution);
        Config.CUUR_USER = curr;


    }

    private void updateUI(FirebaseUser user) {
    }

}
