package com.baby_controller.src;

import java.sql.Time;
import java.util.Date;


public class Day  {
    private MealList _meals = new MealList();
    boolean _firstMealIsEaten = false;
    private Date _carrDate;
    private int _recommendedAmountPerMeal;
    private Child _child;
    private Day _next;
    private Day _prev;

    public Day(Child child) {
        this._child = child;
        for (int i = 0; i <= 13; i++) {
            _meals.add(child.getRecommendedAmountPerMeal());
            _meals.get_curr().setEaten(-1);
            if (i != 0) {
                _meals.get_curr().setEaten(-1);
                _meals.get_curr().setTimeToEat(_meals.getLast().get_prev());
            } else _meals.get_curr().setTimeToEat((Meal) null);
        }
    }

    public Meal addNewMeal(){
        Meal tmp = new Meal(_child.getRecommendedAmountPerMeal());
        if(_meals.getAmountOfMeals() >= 1) {
            tmp.setTimeToEat(_meals.get_curr());
        }else {
            tmp.setTimeToEat(Config.DEAFULT_BREAKFAST_TIME);
        }
        tmp.setEaten(-1);
        _meals.get_curr().set_next(tmp);
        return tmp;
    }

    public Meal getLastMeal(){
        Meal tmp = _meals.get_head();
        return _meals.getLast();
    }

    public void set_firstMealIsEaten(boolean eaten) {
        this._firstMealIsEaten = eaten;
    }

    public boolean is_firstMealIsEaten() {

        return _firstMealIsEaten;
    }

    public void set_carrDate(Date _carrDate) {
        this._carrDate = _carrDate;
    }

    public Date get_currDate() {
        return _carrDate;
    }



    public void setFirstMealTime(Time time){
        _meals.get_head().setTimeToEat(time);
    }

    /*
     * sets the timeToEat for each meal of the day, can be used to initialize a new Day or for update the next meal time
     * if the current meal wasn't on time.
     */
    public void updateFeedingTimes(){
        if (_firstMealIsEaten){
            long firstMealTime = _meals.get_head().get_whenEaten().getTime();
            Meal tmp = _meals.get_head();
            while (tmp.get_next() != null){
                if(tmp.isEaten() == -1){
                    continue;
                }
                tmp.setTimeToEat(tmp.get_prev());
                tmp = tmp.get_next();
            }
        }
    }


    public MealList get_meals() {
        return _meals;
    }

    public int get_recommendedAmountPerMeal() {
        return _recommendedAmountPerMeal;
    }

    public void set_meals(MealList _meals) {
        this._meals = _meals;
    }

    public void set_recommendedAmountPerMeal(int _recommendedAmountPerMeal) {
        this._recommendedAmountPerMeal = _recommendedAmountPerMeal;
    }

    public void set_child(Child _child) {
        this._child = _child;
    }

    public void set_next(Day _next) {
        this._next = _next;
    }

    public void set_prev(Day _prev) {
        this._prev = _prev;
    }

    public Child get_child() {
        return _child;
    }

    public Day get_next() {
        return _next;
    }

    public Day get_prev() {
        return _prev;
    }



}
