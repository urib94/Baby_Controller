package com.baby_controller.src;


import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

public  class Baby {
    private String parentName;
    private String parentUid;
    private String name = "";
    private String institutionName;
    private int indexInInstitute = 0;
    ArrayList<Meal> history = new ArrayList<>();
    private int ageInMonths = 0;
    private String registrationToken;


    private double weight = 0;
    private int recommendedAmountOfMeals = 0;
    private int recommendedAmountPerMeal = 0;
    private int indexInParent;
    private long timeBetweenMeals = Config.TIME_BETWEEN_MEALS;

    public Baby(){}


    public Baby(String name, double weight){
        this.weight = weight;
        this.name = name;
        calcRecommendedAmountPerMeal(weight);
        history.clear();
        history.add(new Meal(recommendedAmountPerMeal));
        System.out.println(history.toString());
        System.out.println("history size = " + history.size());
        history.get(history.size() -1 ).setTimeToEat(System.currentTimeMillis());
    }

    public void calcRecommendedAmountPerMeal(double weight) {
        this.recommendedAmountPerMeal =  (int) ((double)(weight * 150 / 8));;
    }

    public void set_recommendedAmountOfMeals() {
        if (ageInMonths <= 1) {
            recommendedAmountOfMeals = 12;
        } else if (ageInMonths <= 6) {
            recommendedAmountOfMeals = 15;
        } else if (ageInMonths <= 9) {
            recommendedAmountOfMeals = 5;
        }else recommendedAmountOfMeals = 4;
    }

    public void eatingNextMeal(int amount) {
        System.out.println("adding new meal for " + parentName+"'s baby " + name );
        if(history.size() == 0){
            history.add(new Meal(amount));
        }
        history.get(history.size() - 1).setEaten(1);
        history.get(history.size() - 1).setReceivedAmount(amount);
        history.get(history.size() - 1).setWhenEaten(System.currentTimeMillis());
        createNextMeal();
        int count = 0;

        FirebaseDatabase.getInstance().getReference().child("Institutions")
                .child(institutionName).child("parents").child(String.valueOf(indexInInstitute)).child("children").child(String.valueOf(indexInParent)).
                child("history").setValue(history);
        FirebaseDatabase.getInstance().getReference().child("Users").child(parentUid).child("children")
                .child(String.valueOf(indexInParent)).child("history").setValue(history);


        //notify the parentName using firebase cloud messaging



        // TODO: 10/25/2021 notify parents
    }

    //upload this Baby to firebase database use transaction



    private void createNextMeal() {
        history.add(new Meal(recommendedAmountPerMeal));
        history.get(history.size() - 1).setEaten(-1);
        if(history.get(history.size() - 2).getWhenEaten() < history.get(history.size() - 2).getTimeToEat()) {
            history.get(history.size() - 1).calcTimeToEat(history.get(history.size() - 2), timeBetweenMeals);
        }
        else history.get(history.size() - 1).setTimeToEat(System.currentTimeMillis() + timeBetweenMeals);
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
        history.get(history.size() -1).setTimeToEat (System.currentTimeMillis() - 1000);
    }

    //get a date and return how many months old it is
    public void  calculateAgeInMonth(int year, int month){
        if(year < 2000){
            year += 2000;
        }
        System.out.println("year = " + year + " month = " + month);
        Date tmp = new Date(System.currentTimeMillis());
        int thisMonth = tmp.getMonth() + 1;
        int thisYear = tmp.getYear() + 1900;
        System.out.println("this - year = " + thisYear + "this - month = " + thisMonth);
        int months = 0;
        if (year == thisYear) {
            months = thisMonth - month;
            System.out.println("months = " + months);
        } else {
            months = (thisYear - year) * 12;
            months = months + thisMonth - month;
        }
        System.out.println("months = " + months);
        ageInMonths =  months;

        set_recommendedAmountOfMeals();
        calculateTimeBetweenMeals();
    }

    private void calculateTimeBetweenMeals() {
        long day = (1000 * 60 * 60 * 24);
        timeBetweenMeals = day / (long)recommendedAmountOfMeals;
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

    public String getRegistrationToken() {
        return registrationToken;
    }

    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }

    public long getTimeBetweenMeals() {
        return timeBetweenMeals;
    }

    public void setTimeBetweenMeals(long timeBetweenMeals) {
        this.timeBetweenMeals = timeBetweenMeals;
    }
}