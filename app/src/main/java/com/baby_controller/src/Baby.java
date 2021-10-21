//package com.baby_controller.src;
//
//import java.sql.Time;
//import java.util.Date;
//
//public class Baby {
//    private String name;
//    private int id;
//    private FeedingHistory history;
//    private int ageInMonths;
////    private Parent[] parents = new Parent[2];
//
//    private  double _weight;
//    private int recommendedAmountOfMeals;
//    private int recommendedAmountPerMeal;
//
//public Baby(){};
//
//public Baby(double weight){
//    _weight = weight;
//    setRecommendedAmountPerMeal(weight);
//    history = new FeedingHistory(this);
//    history.addNextDay(this);
//}
//
//public Baby(String name, double weight){
//    this._weight = weight;
//    this.name = name;
//    setRecommendedAmountPerMeal(weight);
//    history = new FeedingHistory(this);
//    history.addNextDay(this);
//}
//
//public Baby(double weight, int ageInMonths){
//    this._weight = weight;
//    this.ageInMonths = ageInMonths;
//    setRecommendedAmountPerMeal(weight);
//    history = new FeedingHistory(this);
//    history.addNextDay(this);
//}
//
//    // TODO: 10/12/2021 add a method that parses the feeding history end returns the avg hour of the chile first meal of the day
//
//public double  getChildWeight(){
//    return _weight;
//}
//public void set_recommendedAmountOfMeals(int ageInMonths){
//    if(ageInMonths <= 1){
//        recommendedAmountOfMeals = 12;
//    }else if(ageInMonths <= 6) {
//        recommendedAmountOfMeals = 15;
//    } else if(ageInMonths <= 9){
//        recommendedAmountOfMeals = 5;
//    }
//    recommendedAmountOfMeals = 4;
//}
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public void eatingNextMeal(int amount){
//        int dayNum = new Date(System.currentTimeMillis()).getDay();
//        Day currDay = history.getLast();
//        if(currDay.get_meals().getAmountOfMeals() > recommendedAmountOfMeals){
//            // TODO: 10/12/2021 send a message to the user indicating that the child hase exceeded the daily
//           //  recommended amount of meals, ask him if he wold like to give home another meal?
//        }
//        int foodEaten = 0;
//        Meal tmp = currDay.get_meals().get_head();
//
//        while (tmp.get_next() != null){
//            foodEaten += tmp.get_receivedAmount();
//        }
//
//        if(foodEaten >= recommendedAmountOfMeals * recommendedAmountPerMeal)
//        {
//            // TODO: 10/12/2021 send a message to the user indicating that the child hase exceeded the daily
//            //  recommended amount of food, ask him if he wold like to give home another meal?
//        }
//        // TODO: 10/12/2021  continue only if the user sed so
//
//        Meal newMealNode = currDay.addNewMeal();
//        newMealNode.set_whenEaten(new Time(System.currentTimeMillis()));
//        newMealNode.mealWasEaten(amount);
//        currDay.addNewMeal();
//        currDay.get_meals().get_curr().setEaten(1);
//        currDay.get_meals().get_curr().set_whenEaten(new Time(System.currentTimeMillis()));
//        currDay.get_meals().add(recommendedAmountPerMeal);
//        currDay.updateFeedingTimes();
//
//        Date day = new java.sql.Date(currDay.get_meals().getLast().get_timeToEat().getTime());
//
//        if(day.after(currDay.get_currDate())){
//            // adding the new meal to the new day & eras it from yesterday.
//            history.addNextDay(this);
//            history.getLast().get_meals().addMeal(history.getLast().get_prev().getLastMeal());
//            history.getLast().get_meals().getLast().get_prev().set_next(null);
//        }
//
//    }
//
//    public void setRecommendedAmountPerMeal(double weight) {
//        this.recommendedAmountPerMeal =  (int) ((double)(weight * 150 / 8));;
//    }
//
//    public int getRecommendedAmountPerMeal(){
//        return recommendedAmountPerMeal;
//    }
//
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public FeedingHistory getHistory() {
//        return history;
//    }
//
//    public void setHistory(FeedingHistory history) {
//        this.history = history;
//    }
//
//    public int getAgeInMonths() {
//        return ageInMonths;
//    }
//
//    public double get_weight() {
//        return _weight;
//    }
//
//    public void set_weight(double _weight) {
//        this._weight = _weight;
//    }
//
//    public int get_recommendedAmountOfMeals() {
//        return recommendedAmountOfMeals;
//    }
//
//    public int get_recommendedAmountPerMeal() {
//        return recommendedAmountPerMeal;
//    }
//
//    public void set_recommendedAmountPerMeal(int _recommendedAmountPerMeal) {
//        this.recommendedAmountPerMeal = _recommendedAmountPerMeal;
//    }
//
//    public void setAgeInMonths(int ageInMonths) {
//        this.ageInMonths = ageInMonths;
//    }
//
////    public Parent[] getParents() {
////        return parents;
////    }
////
////    public void setParents(Parent[] parents) {
////        this.parents = parents;
////    }
//
//    public int getRecommendedAmountOfMeals() {
//        return recommendedAmountOfMeals;
//    }
//
//    public void setRecommendedAmountOfMeals(int recommendedAmountOfMeals) {
//        this.recommendedAmountOfMeals = recommendedAmountOfMeals;
//    }
//
//
//
//
//}
//
//
