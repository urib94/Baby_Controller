package com.baby_controller.src;

import com.google.firebase.database.DatabaseReference;

public class MealList {
    DatabaseReference reference;
    private Meal _head = null;
    private Meal _curr = null;
    private int count = 0;
    private int recommendedAmount = 0;

    public MealList(){}

    public MealList(int recommendedAmountPerMeal) {
        this.recommendedAmount = recommendedAmountPerMeal;
    }

    public Meal get(int index){
        Meal tmp = _head;
//        if(tmp != null) {
//            for (int i = 0; i < getAmountOfMeals(); i++) {
//                if(i == index){
//                    return tmp;
//                }
//                tmp = tmp.getNext();
//            }
//        }
        return  null;
    }


    public void add(int recommendedAmount){
//        Meal tmp = new Meal(recommendedAmount);
//        _curr.setNext(tmp);
//        _curr = _curr.getNext();
//        count++;
    }

    public void addMeal(Meal newMeal){
//        _curr.setNext(newMeal);
//        _curr = _curr.getNext();
//        count++;

    }

    public int getAmountOfMeals(){
//        Meal tmp = _head;
//        if(tmp != null){
//            count++;
//            while (tmp.getNext() != null){
//                tmp = tmp.getNext();
//                count++;
//            }
//        }
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
//        Meal tmp = _head;
//        while (tmp.getNext() != null){
//            str.append(tmp.toString());
//            tmp = tmp.getNext();
//        }
        return str.toString();
    }

    public Meal get_head() {
        return _head;
    }


    public Meal getLast() {
        Meal tmp = _head;
//        if (tmp != null) {
//            while (tmp.getNext() != null) {
//                tmp = tmp.getNext();
//            }
//        }
        return tmp;
    }
}
