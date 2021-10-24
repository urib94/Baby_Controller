package com.baby_controller.src;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class Institution {
    private String name;
    private DatabaseReference reference;
    List<Manager1> management = new ArrayList<>();
//    public Institution(){
//        name = "test";
//        management.add((Manager) new Manager());
//    }



    public Institution (Manager1 manager, String name){
        this.name = name;
//        manager.setInstitutionName(this);
        this.management.add(manager);
    }



    public Manager1 getManger(String userName){
        for(int i = 0; i < management.size(); i++){
            if(management.get(i).get_userName().equals(userName)){
                if(management.get(i).getUserType() == User.UserType.PARENT) {
                    return (Manager1) management.get(i);
                }
            }
        }
        return null;
    }


    public boolean addManager(Manager1 manager){
        if(getManger(manager.get_userName()) == null){
            this.management.add(manager);
//            DatabaseManager.
   //         DatabaseManager.addNewManager(this,manager);
            return true;
        }
        return  false;
    }


    public String getName() {
        return name;
    }

    public List<Manager1> getManagement() {
        return management;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DatabaseReference getReference() {
        return reference;
    }

    public void setReference(DatabaseReference reference) {
        this.reference = reference;
    }
}
