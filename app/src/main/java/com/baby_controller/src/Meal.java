package com.baby_controller.src;



import java.sql.Time;

public class Meal {
    private int _recommendedAmount;
    private int _receivedAmount = 0;
    private Time _whenEaten = new Time(0);
    private Time _timeToEat = new Time(0);
    private Child child;
    /*
     * -1 for un eaten meals, 1 for fully eaten meals, 0 for partially eaten
     */
    private int _eaten;

    private Meal _next = null;
    private Meal _prev = null;

    public Meal(){}

    public Meal(int recommendedAmount){
        this._recommendedAmount = recommendedAmount;
    }

    public void setTimeToEat(Time time){
        _timeToEat.setTime(time.getTime());
    }

    public void setTimeToEat(Meal prevMealNode){
        if(prevMealNode == null) {
            this._timeToEat = Config.DEAFULT_BREAKFAST_TIME;
            return;
        }
        if(this.isEaten() != 1){
            if(prevMealNode.isEaten() == 1) {
                this._timeToEat.setTime(prevMealNode._whenEaten.getTime() + Config.TIME_BETWEEN_MEALS);
            }
            else this._timeToEat.setTime(prevMealNode._timeToEat.getTime() + Config.TIME_BETWEEN_MEALS);
        }
    }

    /*
    *  -1 for un eaten meals, 1 for fully eaten meals, 0 for partially eaten
    */
    public void setEaten(int eaten){
        _eaten = eaten;
    }

    public void mealWasEaten(int amount){
        if(amount >= get_recommendedAmount()) {
            setEaten(1);
        }else if(amount == 0){
            setEaten(-1);
        }else setEaten(0);
        _receivedAmount = amount;
    }

    public void setMealRecommendedAmount(int amount) {
        this._recommendedAmount = amount;
    }

    public int isEaten(){
        if(_receivedAmount == 0){
            return -1;
        }
        if(_recommendedAmount - _receivedAmount <= 0) {
            return  1;
        }
        // if the child have eaten, but not enough
        return 0;
    }


    public int get_receivedAmount() {
        return _receivedAmount;
    }

    public int get_recommendedAmount() {
        return _recommendedAmount;
    }

    public void set_recommendedAmount(int _recommendedAmount) {
        this._recommendedAmount = _recommendedAmount;
    }

    public void set_receivedAmount(int _receivedAmount) {
        this._receivedAmount = _receivedAmount;
    }

    public void set_whenEaten(Time _whenEaten) {
        this._whenEaten = _whenEaten;
    }

    public void set_timeToEat(Time _timeToEat) {
        this._timeToEat = _timeToEat;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public void set_eaten(int _eaten) {
        this._eaten = _eaten;
    }

    public Time get_whenEaten() {
        return _whenEaten;
    }

    public Time get_timeToEat() {
        return _timeToEat;
    }

    public Child getChild() {
        return child;
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

