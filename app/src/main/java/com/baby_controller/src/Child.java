package com.baby_controller.src;

import com.baby_controller.src.FeedingHistory;

import java.sql.Time;
import java.util.Date;

public class Child {
    private String _name;
    private FeedingHistory _history;
    private int _ageInMonths;


    private  double _weight;
    private int _recommendedAmountOfMeals;
    private int _recommendedAmountPerMeal;

public Child(){};

public Child(double weight){
    _weight = weight;
    setRecommendedAmountPerMeal(weight);
    _history = new FeedingHistory(this);
    _history.addNextDay(this);
}

public Child(String name,double weight){
    this._weight = weight;
    this._name = name;
    setRecommendedAmountPerMeal(weight);
    _history = new FeedingHistory(this);
    _history.addNextDay(this);
}

public Child(double weight, int ageInMonths){
    this._weight = weight;
    this._ageInMonths = ageInMonths;
    setRecommendedAmountPerMeal(weight);
    _history = new FeedingHistory(this);
    _history.addNextDay(this);
}

    // TODO: 10/12/2021 add a method that parses the feeding history end returns the avg hour of the chile first meal of the day

public double  getChildWeight(){
    return _weight;
}
public void set_recommendedAmountOfMeals(int ageInMonths){
    if(ageInMonths <= 1){
        _recommendedAmountOfMeals = 12;
    }else if(ageInMonths <= 6) {
        _recommendedAmountOfMeals = 15;
    } else if(ageInMonths <= 9){
        _recommendedAmountOfMeals = 5;
    }
    _recommendedAmountOfMeals = 4;
}

    public void eatingNextMeal(int amount){
        int dayNum = new Date(System.currentTimeMillis()).getDay();
        Day currDay = _history.getLast();
        if(currDay.get_meals().getAmountOfMeals() > _recommendedAmountOfMeals){
            // TODO: 10/12/2021 send a message to the user indicating that the child hase exceeded the daily
           //  recommended amount of meals, ask him if he wold like to give home another meal?
        }
        int foodEaten = 0;
        Meal tmp = currDay.get_meals().get_head();

        while (tmp.get_next() != null){
            foodEaten += tmp.get_receivedAmount();
        }

        if(foodEaten >= _recommendedAmountOfMeals * _recommendedAmountPerMeal)
        {
            // TODO: 10/12/2021 send a message to the user indicating that the child hase exceeded the daily
            //  recommended amount of food, ask him if he wold like to give home another meal?
        }
        // TODO: 10/12/2021  continue only if the user sed so

        Meal newMealNode = currDay.addNewMeal();
        newMealNode.set_whenEaten(new Time(System.currentTimeMillis()));
        newMealNode.mealWasEaten(amount);
        currDay.addNewMeal();
        currDay.get_meals().get_curr().setEaten(1);
        currDay.get_meals().get_curr().set_whenEaten(new Time(System.currentTimeMillis()));
        currDay.get_meals().add(_recommendedAmountPerMeal);
        currDay.updateFeedingTimes();

        Date day = new java.sql.Date(currDay.get_meals().getLast().get_timeToEat().getTime());

        if(day.after(currDay.get_currDate())){
            // adding the new meal to the new day & eras it from yesterday.
            _history.addNextDay(this);
            _history.getLast().get_meals().addMeal(_history.getLast().get_prev().getLastMeal());
            _history.getLast().get_meals().getLast().get_prev().set_next(null);
        }

    }

    public void setRecommendedAmountPerMeal(double weight) {
        this._recommendedAmountPerMeal =  (int) ((double)(weight * 150 / 8));;
    }

    public int getRecommendedAmountPerMeal(){
        return _recommendedAmountPerMeal;
    }


    public void set_ageInMonths(int _ageInMonths) {
        this._ageInMonths = _ageInMonths;
    }

}


