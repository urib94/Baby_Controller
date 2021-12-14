package com.baby_controller.src;


import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Meal {


    private int recommendedAmount;
    private int receivedAmount = 0;
    private Long whenEaten = 0L;
    private Long timeToEat = 0L;
    /*
     * -1 for uneaten meals, 1 for fully eaten meals, 0 for partially eaten
     */
    private int eaten;


    public Meal(){}

    public Meal(int recommendedAmount){
        this.recommendedAmount = recommendedAmount;
        timeToEat = 0L;
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
                        newMeal.setWhenEaten(Long.parseLong(tmp[1]));
                        break;
                    case "timeToEat":
                        newMeal.setTimeToEat(Long.parseLong(tmp[1]));
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

    public void setTimeToEat(Long time){
        timeToEat = time;
    }

    public void calcTimeToEat(Meal prevMealNode, long timeBetween){
        if(prevMealNode == null) {
            this.timeToEat = Config.DEFAULT_BREAKFAST_TIME;
            return;
        }
        if(this.isEaten() != 1){
            if(prevMealNode.isEaten() == 1) {
                this.timeToEat = (prevMealNode.whenEaten + timeBetween);
            }
            else this.timeToEat = (prevMealNode.timeToEat + timeBetween);
        }
    }

    public void calcTimeToEat(){
        this.timeToEat = Config.DEFAULT_BREAKFAST_TIME;
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
        return  "recommendedAmount=" + recommendedAmount +
                ",receivedAmount=" + receivedAmount +
                ",whenEaten=" + whenEaten +
                ",timeToEat=" + timeToEat +
                ",eaten=" + eaten + "\n" ;

    }


    public static ArrayList<Meal> hashMapToArrayList(HashMap<String,Meal> hashMap){
        return new ArrayList<Meal>(hashMap.values());
    }


    public void setReceivedAmount(int receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public void setWhenEaten(long whenEaten) {
        this.whenEaten = whenEaten;
    }

    public void setEaten(int eaten) {
        this.eaten = eaten;
    }

    public long getWhenEaten() {
        return whenEaten;
    }

    public long getTimeToEat() {
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

