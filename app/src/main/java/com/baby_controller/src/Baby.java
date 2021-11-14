package com.baby_controller.src;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public  class Baby {
    protected DatabaseReference reference;
    private String parentName;
    private String parentUid;
    private String name = "";
    private String institutionName;
    private int indexInInstitute = 0;
    //MealList history;
    List<Meal> history = new LinkedList<Meal>();
    private int ageInMonths = 0;
    Meal meal = new Meal(60);
//    private Parent[] parents = new Parent[2];

    private double weight = 0;
    private int recommendedAmountOfMeals = 0;
    private int recommendedAmountPerMeal = 0;
    private int indexInParent;

    public Baby(){}


    public Baby(String name) {
        this.name = name;
        //history = new MealList();
    }

    public Baby(String name, double weight){
        this.weight = weight;
        this.name = name;
        calcRecommendedAmountPerMeal(weight);
       // history = new MealList(recommendedAmountPerMeal);
    }

    public Baby(double weight){
        this.weight = weight;
        calcRecommendedAmountPerMeal(weight);
        //history = new MealList(recommendedAmountPerMeal);
    }

    public void calcRecommendedAmountPerMeal(double weight) {
        this.recommendedAmountPerMeal =  (int) ((double)(weight * 150 / 8));;
    }

    public void set_recommendedAmountOfMeals(int ageInMonths) {
        if (ageInMonths <= 1) {
            recommendedAmountOfMeals = 12;
        } else if (ageInMonths <= 6) {
            recommendedAmountOfMeals = 15;
        } else if (ageInMonths <= 9) {
            recommendedAmountOfMeals = 5;
        }
        recommendedAmountOfMeals = 4;
        // TODO: 10/21/2021 dasdasdasda
    }

    public void eatingNextMeal(int amount) {
        if(history.size() == 0){
            history.add(new Meal(amount));
        }
        history.get(history.size() - 1).setEaten(1);
        history.get(history.size() - 1).setReceivedAmount(amount);
        history.get(history.size() - 1).setWhenEaten(new Time(System.currentTimeMillis()));
        createNextMeal();
        int count = 0;

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Institutions")
                .child(institutionName).child("parents").child(String.valueOf(indexInInstitute));
        myRef.child("babies").child(name).setValue(this);

        //notify the parentName using firebase cloud messaging



        // TODO: 10/25/2021 notify parents
    }

    //upload this Baby to firebase database use transaction



    private void createNextMeal() {
        history.add(new Meal(recommendedAmountPerMeal));
        history.get(history.size() - 1).setEaten(-1);
        history.get(history.size() - 1).calcTimeToEat(history.get(history.size() - 2));
    }


    public void uploadToDb(){
        reference = FirebaseDatabase.getInstance().getReference().child("Institutions").child(institutionName)
        .child("parents").child(parentName).child("children").child(String.valueOf(indexInParent));
        Transaction.Handler tmp = new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                currentData.setValue(Baby.this);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                if (error != null) {
                    Log.d("Firebase", "error: " + error.getMessage());
                }

            }
        } ;
        reference.runTransaction(tmp);
        setListeners();

    }

    private void setListeners() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Baby tmp = dataSnapshot.child(name).getValue(Baby.class);
                weight = tmp.weight;
                ageInMonths = tmp.ageInMonths;
                indexInInstitute = tmp.indexInInstitute;
                name = tmp.name;
                recommendedAmountOfMeals = tmp.recommendedAmountOfMeals;
                parentName = tmp.parentName;
                recommendedAmountPerMeal = tmp.recommendedAmountPerMeal;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public String toString() {
        return "{name=" + name +
                ",indexInInstitute=" + indexInInstitute +
                ",ageInMonths=" + ageInMonths +
                ",weight=" + weight +
                ",recommendedAmountOfMeals=" + recommendedAmountOfMeals +
                ",recommendedAmountPerMeal=" + recommendedAmountPerMeal +
                "\nhistory=" + history + "><" ;
    }

    public DatabaseReference getReference() {
        return reference;
    }

    public void setReference(DatabaseReference reference) {
        this.reference = reference;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndexInInstitute() {
        return indexInInstitute;
    }

    public void setIndexInInstitute(int id) {
        this.indexInInstitute = id;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentUid() {
        return parentUid;
    }

    public void setParentUid(String parentUid) {
        this.parentUid = parentUid;
    }

    public int getAgeInMonths() {
        return ageInMonths;
    }

    public void setAgeInMonths(int ageInMonths) {
        this.ageInMonths = ageInMonths;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public int getRecommendedAmountOfMeals() {
        return recommendedAmountOfMeals;
    }

    public void setRecommendedAmountOfMeals(int recommendedAmountOfMeals) {
        this.recommendedAmountOfMeals = recommendedAmountOfMeals;
    }

    public int getRecommendedAmountPerMeal() {
        return recommendedAmountPerMeal;
    }

    public void setRecommendedAmountPerMeal(int recommendedAmountPerMeal) {
        this.recommendedAmountPerMeal = recommendedAmountPerMeal;
    }

    public void setNeedToEat(){
        if (history.size() == 0){
            history.add(new Meal(60));
        }
        history.get(history.size() -1).setTimeToEat(new Time(System.currentTimeMillis() - 1000));
    }


    //Baby to Json
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("name", name);
            json.put("id", indexInInstitute);
            json.put("ageInMonths", ageInMonths);
            json.put("_weight", weight);
            json.put("recommendedAmountOfMeals", recommendedAmountOfMeals);
            json.put("recommendedAmountPerMeal", recommendedAmountPerMeal);
            json.put("parentName", parentName);
            for (Meal meal : history) {
                json.put("history", meal.toJson());
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
        return json;
    }


    //get a date and return how many months old it is
    public static int calculateAgeInMonth(int year, int month, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return (int) ((System.currentTimeMillis() - calendar.getTimeInMillis()) / (1000L *60*60*24*30));
    }


    public List<Meal> getHistory() {
        return history;
    }

    public void setHistory(List<Meal> history) {
        this.history = history;
    }


    public String getParentName() {
        return parentName;
    }

    public int getIndexInParent() {
        return indexInParent;
    }

    public void setIndexInParent(int indexInParent) {
        this.indexInParent = indexInParent;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }
}