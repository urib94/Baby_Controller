package com.baby_controller.src;

public class MealList {
    private Meal _head = null;
    private Meal _curr = null;
    private int count = 0;
    public MealList(){
        _head = new Meal();
        _curr = _head;
    }

    public void add(int recommendedAmount){
        Meal tmp = new Meal(recommendedAmount);
        _curr.set_next(tmp);
        _curr = _curr.get_next();
        count++;
    }

    public void addMeal(Meal newMeal){
        _curr.set_next(newMeal);
        _curr = _curr.get_next();
        count++;

    }

    public int getAmountOfMeals(){
        return count;
    }

    public void addLastMeal(int recommendedAmount){

    }

    public Meal get_curr() {
        return _curr;
    }

    public void set_curr(Meal _curr) {
        this._curr = _curr;
    }

    public void set_head(Meal _head) {
        this._head = _head;
    }



    public Meal get_head() {
        return _head;
    }


    public Meal getLast() {
        Meal tmp = _head;
        while (tmp.get_next() != null){
            tmp = tmp.get_next();
        }
        return tmp;
    }
}
