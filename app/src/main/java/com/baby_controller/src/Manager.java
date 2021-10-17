package com.baby_controller.src;

import java.util.ArrayList;
import java.util.List;

public class Manager extends User{
    List<Parent> parents = new ArrayList<>();

    public Manager(String userName, String password) {
        super(userName, password, UserType.MANAGER);
    }

    @Override
    public List<Child> getChildren() {
        List<Child> tmp = new ArrayList<>();
        for (int i = 0; i < parents.size(); i++){
            tmp.addAll(parents.get(i).getChildren());
        }
        return tmp;
    }


    public boolean addParent(User parent){
        if(getParent(parent.get_userName()) == null) {
            parents.add((Parent) parent);
            return true;
        }
        return false;
    }

    public Parent getParent(String userName){
        for(int i = 0; i < parents.size(); i++){
            if(parents.get(i).get_userName().equals(userName)){
                if(parents.get(i).getUserType() == User.UserType.PARENT) {
                    return (Parent) parents.get(i);
                }
            }
        }
        return null;
    }

    public List<Parent> getParents(){
        return parents;
    }


    @Override
    public Child getChild(String name, int id) {
        return null;
    }
}
