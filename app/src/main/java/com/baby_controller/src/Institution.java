package com.baby_controller.src;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Institution {
    List<Manager> management = new ArrayList<>();


    public Institution (User manager, View view){
        if(manager.getUserType() == User.UserType.MANAGER || manager.getUserType() == User.UserType.ADMINISTRATOR){
            this.management.add((Manager) manager);
        }
        else{
            // TODO: 10/17/2021 print to the user that only a manger can create new institution
        }
    }



    public Manager getManger(String userName){
        for(int i = 0; i < management.size(); i++){
            if(management.get(i).get_userName().equals(userName)){
                if(management.get(i).getUserType() == User.UserType.PARENT) {
                    return (Manager) management.get(i);
                }
            }
        }
        return null;
    }


    public boolean addManager(Manager manager){
        if(getManger(manager.get_userName()) == null){
            this.management.add(manager);
            return true;
        }
        return  false;
    }



    public List<Manager> getManagement() {
        return management;
    }

    public void setManagement(List<Manager> management) {
        this.management = management;
    }


}
