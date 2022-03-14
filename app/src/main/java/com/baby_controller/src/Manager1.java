package com.baby_controller.src;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Manager1 extends LocalUser {

    public Manager1(){
        super();
    }

    public Manager1(String userName,String email,  String password) {
        super(email, userName,password,UserType.MANAGER);
    }

    public Manager1(LocalUser localUser) {
        super(localUser);
    }

    public List<Baby> getChildren() {
        return null;
    }


    public LocalUser.UserType getUserType() {
        return userType;
    }

    public void setUserType(LocalUser.UserType userType) {
        this.userType = userType;
    }

    /*
       uses the LocalUser uploadToDb, start working in the "UserType" child
       returns the reference to the manger's username child
        */

//    public synchronized DatabaseReference uploadToDb() {
//        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(get);
//        dbRef.setValue(this);
//        return dbRef;
//    }


    public Baby getChild(String name, int id) {
        return null;
    }



    public void feedBaby(int amount, Baby babyToFeed){
        List<Parent> parents = getInstitute().getParents();
        Baby tmp = null;
        for(LocalUser parent :parents){
            for (Baby baby: ((Parent)parent).getChildren()){
                if(tmp == null){
                    if (baby.equals(babyToFeed)){
                        tmp = baby;
                        break;
                    }
                    if(tmp != null){
                        tmp.eatingNextMeal(amount);
//                        ((Parent)parent).notifyParent();
                        break;
                    }
                }
            }
        }

    }
    public void notifyBabyNeedToEat(Baby baby){

    }




//    @Override
//    public void run() {
//        int i = 0;
//        while (i != 1){
//            if((System.currentTimeMillis() % Config.TEN_MIN) == 0) {
//                Baby baby = getInstitute().needToFeed();
//                if (baby != null) {
//                    notifyBabyNeedToEat(baby);
//                }
//            }
//        }
//    }

    //Manager1 to Json
    public JSONObject toJson()  {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", name);
            jsonObject.put("password", password);
            jsonObject.put("email", email);
            jsonObject.put("userType", userType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    //Json to Manager1
    public void fromJson(JSONObject jsonObject)  {
        try {
            name = jsonObject.getString("userName");
            password = jsonObject.getString("password");
            email = jsonObject.getString("email");
            userType = UserType.MANAGER;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //coppy constructor
    public Manager1(Manager1 manager) {
        super(manager);
    }

    @NonNull
    @Override
    public String toString() {
        return "Manager1{" +
                ", userType=" + userType +
                ", institutionName='" + institutionName + '\'' +
                ", userName='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", uid='" + uid + '\'' +
                ", defaultDevice=" + defaultDeviceAddress +
                '}';
    }


}
