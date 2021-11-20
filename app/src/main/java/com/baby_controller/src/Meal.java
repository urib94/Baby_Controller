package com.baby_controller.src;


import androidx.annotation.NonNull;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Meal {


    private int recommendedAmount;
    private int receivedAmount = 0;
    private Time whenEaten;
    private Time timeToEat;
    /*
     * -1 for uneaten meals, 1 for fully eaten meals, 0 for partially eaten
     */
    private int eaten;


    public Meal(){}

    public Meal(int recommendedAmount){
        this.recommendedAmount = recommendedAmount;
        eaten = -1;
    }

    public static List<Meal> listFromString(String HistoryString) {
        List<Meal> mealList = new ArrayList<>();
        LinkedList<String> meals = new LinkedList<>(Arrays.asList(HistoryString.split("}")));

        for(String str: meals){
            LinkedList<String> meal = new LinkedList<>(Arrays.asList(str.split("\n")));
            Meal newMeal = new Meal();
            for(String a_meal: meal) {
                String[] tmp = a_meal.split(",");
                tmp[0] = tmp[0].replace(" ", "");
                tmp[1] = tmp[1].replace(" ", "");

                switch (tmp[0]){
                    case "recommendedAmount":
                        newMeal.setReceivedAmount(Integer.parseInt(tmp[1]));
                        break;
                    case "receivedAmount":
                        newMeal.setRecommendedAmount(Integer.parseInt(tmp[1]));
                        break;
                    case "whenEaten":
                        newMeal.setWhenEaten(new Time(Long.parseLong(tmp[1])));
                        break;
                    case "timeToEat":
                        newMeal.setTimeToEat(new Time(Long.parseLong(tmp[1])));
                        break;
                    case "eaten":
                        newMeal.setEaten(Integer.parseInt(tmp[1]));
                        break;
                }
            }
            mealList.add(newMeal);
        }
        return mealList;
    }

    public void setTimeToEat(Time time){
        timeToEat.setTime(time.getTime());
    }

    public void calcTimeToEat(Meal prevMealNode){
        if(prevMealNode == null) {
            this.timeToEat = Config.DEAFULT_BREAKFAST_TIME;
            return;
        }
        if(this.isEaten() != 1){
            if(prevMealNode.isEaten() == 1) {
                this.timeToEat.setTime(prevMealNode.whenEaten.getTime() + Config.TIME_BETWEEN_MEALS);
            }
            else this.timeToEat.setTime(prevMealNode.timeToEat.getTime() + Config.TIME_BETWEEN_MEALS);
        }
    }

    public void calcTimeToEat(){
        this.timeToEat = Config.DEAFULT_BREAKFAST_TIME;
        }

    /*
    *  -1 for un eaten meals, 1 for fully eaten meals, 0 for partially eaten
    */


    public void mealWasEaten(int amount){
        if(amount >= getRecommendedAmount()) {
            setEaten(1);
        }else if(amount == 0){
            setEaten(-1);
        }else setEaten(0);
        receivedAmount = amount;
    }

    public void setMealRecommendedAmount(int amount) {
        this.recommendedAmount = amount;
    }

    public int isEaten(){
        if(receivedAmount == 0){
            return -1;
        }
        if(recommendedAmount - receivedAmount <= 0) {
            return  1;
        }
        // if the child have eaten, but not enough
        return 0;
    }

    public int getReceivedAmount() {
        return receivedAmount;
    }

    public int getRecommendedAmount() {
        return recommendedAmount;
    }

    public void setRecommendedAmount(int recommendedAmount) {
        this.recommendedAmount = recommendedAmount;
    }

    @NonNull
    @Override
    public String toString() {
        return  "\nrecommendedAmount=" + recommendedAmount +
                ",receivedAmount=" + receivedAmount +
                ",whenEaten=" + whenEaten +
                ",timeToEat=" + timeToEat +
                ",eaten=" + eaten+ "/n" ;

    }




    public void setReceivedAmount(int receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public void setWhenEaten(Time whenEaten) {
        this.whenEaten = whenEaten;
    }

    public void setEaten(int eaten) {
        this.eaten = eaten;
    }

    public Time getWhenEaten() {
        return whenEaten;
    }

    public Time getTimeToEat() {
        return timeToEat;
    }

//    public void setNext(Meal next) {
//        this.next = next;
//    }

//    public void setPrev(Meal prev) {
//        this.prev = prev;
//    }

//    public Meal getNext() {
//        return next;
//    }

//    public Meal getPrev() {
//        return prev;
//    }



//    //Meal to Json
//    public JSONObject toJson(){
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("recommended amount", recommendedAmount);
//            jsonObject.put("received amount", receivedAmount);
//            jsonObject.put("when eaten", whenEaten.getTime());
//            jsonObject.put("time to eat", timeToEat.getTime());
//            jsonObject.put("curr date", currDate.getTime());
//            jsonObject.put("eaten", eaten);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return jsonObject;
//    }
//
//    //Json to Meal
//    public static Meal fromJson(JSONObject jsonObject){
//        Meal meal = new Meal();
//        try {
//            meal.setRecommendedAmount(jsonObject.getInt("recommended amount"));
//            meal.setReceivedAmount(jsonObject.getInt("received amount"));
//            meal.setWhenEaten(new Time(jsonObject.getLong("when eaten")));
//            meal.setTimeToEat(new Time(jsonObject.getLong("time to eat")));
//            meal.setCurrDate(new Date(jsonObject.getLong("curr date")));
//            meal.setEaten(jsonObject.getInt("eaten"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return meal;
//    }
}

