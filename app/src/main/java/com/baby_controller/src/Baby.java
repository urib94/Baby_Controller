package com.baby_controller.src;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

public  class Baby {
    private String parentName;
    private String parentUid;
    private String name = "";
    private String institutionName;
    private int indexInInstitute = 0;
    ArrayList<Meal> history = new ArrayList<>();
    private int ageInMonths = 0;


    private double weight = 0;
    private int recommendedAmountOfMeals = 0;
    private int recommendedAmountPerMeal = 0;
    private int indexInParent;

    public Baby(){}


    public Baby(String name) {
        this.name = name;

    }

    public Baby(String name, double weight){
        this.weight = weight;
        this.name = name;
        calcRecommendedAmountPerMeal(weight);
        history.add(new Meal(recommendedAmountPerMeal));

    }

    public Baby(double weight){
        this.weight = weight;
        calcRecommendedAmountPerMeal(weight);
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

    //get a date and return how many months old it is
    public static int calculateAgeInMonth(int year, int month, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return (int) ((System.currentTimeMillis() - calendar.getTimeInMillis()) / (1000L *60*60*24*30));
    }


    public ArrayList<Meal> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<Meal> history) {
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

}