package com.baby_controller.src;

public class MealList {
    private Meal _head = null;
    private Meal _curr = null;
    private int count = 0;
    private int recommendedAmount = 0;

    public MealList(){
        _head = new Meal();
        _curr = _head;
    }

    public MealList(int recommendedAmountPerMeal) {
        this.recommendedAmount = recommendedAmountPerMeal;
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

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        Meal tmp = _head;
        while (tmp.get_next() != null){
            str.append(tmp.toString());
            tmp = tmp.get_next();
        }
        return str.toString();
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
