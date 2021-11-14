package com.baby_controller;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.baby_controller.src.Config;
import com.baby_controller.src.Institution;
import com.baby_controller.src.LocalUser;
import com.baby_controller.src.Parent;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddBabyActivity extends AppCompatActivity {
   private EditText parentName;
   private EditText babyName;
   private EditText dateOfBirth;
   private EditText wight;
   private Button add;
   private String sParentName;
   private String sBabyName;
   private int[] iDateOfBirth;
   private double dWight;
   private Institution institution;
   public static String TAG = "AddBabyActivity";
   DatabaseReference instituteRef =  FirebaseDatabase.getInstance().getReference();


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_baby);
        configurButtons();
        getInstitute();
    }

    @Override
    protected void onStart() {
        super.onStart();
        triggerListener();
        getInstitute();
    }

    private void configurButtons() {
        parentName = (EditText) findViewById(R.id.parent_name);
        babyName = (EditText) findViewById(R.id.baby_name);
        dateOfBirth = (EditText) findViewById(R.id.date_of_birth);
        wight = (EditText) findViewById(R.id.baby_wight);
        add = (Button) findViewById(R.id.add_new_baby);
        Button back = (Button) findViewById(R.id.back);


        if(Config.getCurrentUser().getUserType() == LocalUser.UserType.PARENT){
            parentName.setVisibility(View.GONE);
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("pressed on click");


                if(sBabyName == ("") || sParentName == ("") || dWight ==0 || dateOfBirth.getText().toString().equals("")){
                    toastMessage("pleas fill out all the fields");
                    return;
                }
                updateValues();
                System.out.println( "institution == null ? " +(institution == null) );
                if(institution != null) {

                    if(addNewBaby(Config.getCurrentUser().getUserType() == LocalUser.UserType.MANAGER)){
                        parentName.setText("");
                        babyName.setText("");
                        dateOfBirth.setText("");
                        wight.setText("");
                        add .setText("");
                    }
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateValues() {
        sParentName = parentName.getText().toString();
        sBabyName = babyName.getText().toString();
        dWight = Double.parseDouble(wight.getText().toString());
        String[] tmpDate = dateOfBirth.getText().toString().split("/");
        for (String str:tmpDate) {
            str.replace(".","/");
        }
        iDateOfBirth = new int[]{Integer.parseInt(tmpDate[2]), Integer.parseInt(tmpDate[1]), Integer.parseInt(tmpDate[0])};

    }

    private void getInstitute() {
        FirebaseDatabase.getInstance().getReference().getRoot().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                System.out.println(Config.getCurrentUser().toString());
                System.out.println("getInstitute() method invoked");
                for (DataSnapshot snap:snapshot.getChildren()){
                    System.out.println("snap in get inst = " + snap.toString());
                    Institution tmpInst;
                    try {
                        tmpInst = snap.getValue(Institution.class);
                        if (tmpInst == null) {
                            System.out.println("tmp = null");
                            continue;
                        }else {
                            System.out.println("institution name = " + tmpInst.getName() + " curr user inst name = "+
                                    Config.getCurrentUser().getInstitutionName());
                            System.out.println("tmpInst = "+ tmpInst.toString());
                        }
                    }catch (Exception e){
                        Log.e(TAG,e.getMessage());
                        continue;
                    }
                    if(Config.getCurrentUser().getInstitutionName().equals(tmpInst.getName())){
                        institution = tmpInst;
                        System.out.println("inst found" +  institution.toString());
                        instituteRef = snap.getRef();
                        break;
                    }
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

    triggerListener();
    }

    private void triggerListener(){
        FirebaseDatabase.getInstance().getReference().getRoot().child("trigger").setValue(" ");
        FirebaseDatabase.getInstance().getReference().getRoot().child("trigger").setValue(null);
    }
    private boolean addNewBaby(boolean currIsParent){
        System.out.println( "institution == null ? " +(institution == null) );
        if(currIsParent){
            ((Parent)(Config.getCurrentUser())).addNewChild(sBabyName,iDateOfBirth[0],iDateOfBirth[1],iDateOfBirth[2]
            ,dWight);
            toastMessage(sBabyName + "was successfully added");
            return true;
        }else if(institution != null){
            System.out.println("institution = "+ institution.toString());
            institution.getParent(sParentName).addNewChild(sBabyName,iDateOfBirth[0],iDateOfBirth[1],iDateOfBirth[2]
                            ,dWight);
                    return true;
        }
        toastMessage("unable to add " + sBabyName + " to " + sParentName + " try again later");
        return false;
    }

    private void toastMessage(String s) {
        Toast.makeText(AddBabyActivity.this ,s,Toast.LENGTH_SHORT).show();
    }
}
