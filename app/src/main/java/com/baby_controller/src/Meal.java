package com.baby_controller.src;



import java.sql.Time;
import java.util.Date;

public class Meal {
    private int recommendedAmount;
    private int receivedAmount = 0;
    private Time whenEaten = new Time(0);
    private Time timeToEat = new Time(0);
    private Date currDate = new Date(System.currentTimeMillis());
    /*
     * -1 for un eaten meals, 1 for fully eaten meals, 0 for partially eaten
     */
    private int _eaten;

    private Meal _next = null;
    private Meal _prev = null;

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

    /*
    *  -1 for un eaten meals, 1 for fully eaten meals, 0 for partially eaten
    */
    public void setEaten(int eaten){
        _eaten = eaten;
    }

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
                "\neaten=" + _eaten +
                "\nnext=" + _next +
                "\nprev=" + _prev;
    }

    public void setReceivedAmount(int receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public void setWhenEaten(Time whenEaten) {
        this.whenEaten = whenEaten;
    }

    public void set_eaten(int _eaten) {
        this._eaten = _eaten;
    }

    public Time getWhenEaten() {
        return whenEaten;
    }

    public Time getTimeToEat() {
        return timeToEat;
    }


    public int get_eaten() {
        return _eaten;
    }
    public void set_next(Meal _next) {
        this._next = _next;
    }

    public void set_prev(Meal _prev) {
        this._prev = _prev;
    }

    public Meal get_next() {
        return _next;
    }

    public Meal get_prev() {
        return _prev;
    }
}

