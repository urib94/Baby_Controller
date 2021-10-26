package com.baby_controller.src;



import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.Date;

public class Meal {

    private DatabaseReference reference;
    private int recommendedAmount;
    private int receivedAmount = 0;
    private Time whenEaten = new Time(0);
    private Time timeToEat = new Time(0);
    private Date currDate = new Date(System.currentTimeMillis());
    /*
     * -1 for un eaten meals, 1 for fully eaten meals, 0 for partially eaten
     */
    private int eaten;

    public int getEaten() {
        return eaten;
    }


    public Meal(){}

    public Meal(int recommendedAmount){
        this.recommendedAmount = recommendedAmount;
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

    public void uploadToDb(DatabaseReference mealsRef,int count) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getCurrDate().getDate());
        stringBuilder.append("\\");
        stringBuilder.append(getCurrDate().getMonth());

        reference = mealsRef.child(stringBuilder.toString()).getRef();

        reference.child(String.valueOf(count)).child("recommended amount").setValue(recommendedAmount);
        reference.child(String.valueOf(count)).child("received amount").setValue(receivedAmount);
        reference.child(String.valueOf(count)).child("when eaten").setValue(whenEaten.getTime());
        reference.child(String.valueOf(count)).child("time to eat").setValue(timeToEat.getTime());
        reference.child(String.valueOf(count)).child("curr date").setValue(currDate.getTime());

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Meal tmp = dataSnapshot.getValue(Meal.class);
               currDate = tmp.currDate;
               eaten = tmp.eaten;
               receivedAmount = tmp.receivedAmount;
               timeToEat = tmp.timeToEat;
               whenEaten = tmp.whenEaten;


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

            }
        };
    }
    public Date getCurrDate() {
        return currDate;
    }

    public void setCurrDate(Date currDate) {
        this.currDate = currDate;
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

    @Override
    public String toString() {
        return  "recommendedAmount=" + recommendedAmount +
                "\nreceivedAmount=" + receivedAmount +
                "\nwhenEaten=" + whenEaten +
                "\ntimeToEat=" + timeToEat +
                "\neaten=" + eaten ;

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


    public DatabaseReference getReference() {
        return reference;
    }

    public void setReference(DatabaseReference reference) {
        this.reference = reference;
    }
}

