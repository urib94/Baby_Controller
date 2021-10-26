package com.baby_controller.src;

import androidx.annotation.NonNull;

import java.sql.Time;
import java.util.Date;


public class Day  {
    private MealList _meals = new MealList();
    boolean _firstMealIsEaten = false;
    private Date _carrDate;
    private int _recommendedAmountPerMeal;
//    private Child  _child;
    private Day _next;
    private Day _prev;

    public Day(Date date){
        this._carrDate = date;
//        this._child = new Child();

    }
    public Day(int recamount) {
////        this._child = child;
//        for (int i = 0; i <= 13; i++) {
//            _meals.add(recamount);
//            _meals.get_curr().setEaten(-1);
//            if (i != 0) {
//                _meals.get_curr().setEaten(-1);
//                _meals.get_curr().calcTimeToEat(_meals.getLast().getPrev());
//            } else _meals.get_curr().calcTimeToEat((Meal) null);
//        }
    }

//    public Meal addNewMeal(){
////        Meal tmp = new Meal(_child.getRecommendedAmountPerMeal());
//        if(_meals.getAmountOfMeals() >= 1) {
////            tmp.calcTimeToEat(_meals.get_curr());
//        }else {
////            tmp.calcTimeToEat(Config.DEAFULT_BREAKFAST_TIME);
//        }
//        tmp.setEaten(-1);
//        _meals.get_curr().setNext(tmp);
//        return tmp;
//    }

    public Meal addNewMeal(Meal meal){
//        if(_meals.getAmountOfMeals() >= 1) {
//            meal.calcTimeToEat(_meals.get_curr());
//        }else {
//            meal.setTimeToEat(Config.DEAFULT_BREAKFAST_TIME);
//        }
//        meal.setEaten(-1);
//        _meals.get_curr().setNext(meal);
        return meal;
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

    @NonNull
    @Override
    public String toString() {
        return  "meals=" + _meals +
                "\nfirstMealIsEaten=" + _firstMealIsEaten +
                "\ncarrDate=" + _carrDate.getDate() +
                "\nrecommendedAmountPerMeal=" + _recommendedAmountPerMeal +
//                "\nchild=" + _child +
                "\nnext=" + _next +
                "\nprev=" + _prev;
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
//        if (_firstMealIsEaten){
//            long firstMealTime = _meals.get_head().getWhenEaten().getTime();
//            Meal tmp = _meals.get_head();
//            while (tmp.getNext() != null){
//                if(tmp.isEaten() == -1){
//                    continue;
//                }
//                tmp.calcTimeToEat(tmp.getPrev());
//                tmp = tmp.getNext();
//            }
//        }
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

//    public void set_child(Child _child) {
//        this._child = _child;
//    }

    public void set_next(Day _next) {
        this._next = _next;
    }

    public void set_prev(Day _prev) {
        this._prev = _prev;
    }

    public Day get_next() {
        return _next;
    }

    public Day get_prev() {
        return _prev;
    }

}
