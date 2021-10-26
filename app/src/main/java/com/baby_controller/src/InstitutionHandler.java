package com.baby_controller.src;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;

public class InstitutionHandler extends Institution{

    public static LinkedList<Institution> institutions = new LinkedList<>();
    public static DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Institutions List");

    public InstitutionHandler(){}

    public static void addInstitution(Institution institution){
        if(getInstitution(institution.getName()) == null){
            institutions.add(institution);
        }

    }

    public static Institution getInstitution(String name){
        Institution tmp = institutions.get(0);
        if(tmp != null){
            for(int i = 1; i < institutions.size(); i++){
                if(institutions.get(i).getName().equals(name)){
                    institutions.get(i).setReference(reference.child(institutions.get(i).getName()));
                    return institutions.get(i);
                }
            }
        }
        return null;
    }

    public DatabaseReference uploadToDb(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Institutions List").setValue(this);
        return ref;
    }



}
