//package com.baby_controller.src;
//
//import com.baby_controller.src.util.DatabaseManager;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.sql.Time;
//import java.util.Date;
//
//public class Child {
//    protected DatabaseReference reference;
//    private Parent parent;
//    private String name = "";
//    private int id = 0;
//    private FeedingHistory history;
//    private int ageInMonths = 0;
////    private Parent[] parents = new Parent[2];
//
//    private double _weight = 0;
//    private int recommendedAmountOfMeals = 0;
//    private int recommendedAmountPerMeal = 0;
//
//    public Child(){};
//    public Child(double weight){
//        _weight = weight;
//        calcRecommendedAmountPerMeal(weight);
//        history = new FeedingHistory(this);
//        history.addNextDay(this);
//    }
//
//    public Child(String name, double weight){
//        this._weight = weight;
//        this.name = name;
//        calcRecommendedAmountPerMeal(weight);
//        history = new FeedingHistory(this);
//        history.addNextDay(this);
//    }
//
//    public Child(double weight, int ageInMonths){
//        this._weight = weight;
//        this.ageInMonths = ageInMonths;
//        calcRecommendedAmountPerMeal(weight);
//        history = new FeedingHistory(this);
//        history.addNextDay(this);
//    }
//
//
//
//    public String getName() {
//        return name;
//    }
//
//    public double getChildWeight() {
//        return _weight;
//    }
//
//    public void set_recommendedAmountOfMeals(int ageInMonths) {
//        if (ageInMonths <= 1) {
//            recommendedAmountOfMeals = 12;
//        } else if (ageInMonths <= 6) {
//            recommendedAmountOfMeals = 15;
//        } else if (ageInMonths <= 9) {
//            recommendedAmountOfMeals = 5;
//        }
//        recommendedAmountOfMeals = 4;
//        // TODO: 10/21/2021 dasdasdasda
//    }
//
//    public int getIndexInInstitute() {
//        return id;
//    }
//
//    public void setIndexInInstitute(int id) {
//        this.id = id;
//    }
//
//    public void eatingNextMeal(int amount) {
//        int dayNum = new Date(System.currentTimeMillis()).getDay();
//        Day currDay = history.getLast();
//        if (currDay == null){
//            System.out.println("fuck");
//            currDay = new Day(this);
//        }
//        if (currDay.get_meals().getAmountOfMeals() > recommendedAmountOfMeals) {
//            // TODO: 10/12/2021 send a message to the user indicating that the child hase exceeded the daily
//            //  recommended amount of meals, ask him if he wold like to give home another meal?
//        }
//        int foodEaten = 0;
//        Meal tmp = currDay.get_meals().get_head();
//
//        while (tmp.getNext() != null) {
//            foodEaten += tmp.getReceivedAmount();
//        }
//
//        if (foodEaten >= recommendedAmountOfMeals * recommendedAmountPerMeal) {
//            // TODO: 10/12/2021 send a message to the user indicating that the child hase exceeded the daily
//            //  recommended amount of food, ask him if he wold like to give home another meal?
//        }
//        // TODO: 10/12/2021  continue only if the user sed so
//
//        Meal newMealNode = new Meal(this.recommendedAmountOfMeals);
//        newMealNode.setWhenEaten(new Time(System.currentTimeMillis()));
//        newMealNode.mealWasEaten(amount);
////        currDay.addNewMeal();
//        currDay.get_meals().get_curr().setEaten(1);
//        currDay.get_meals().get_curr().setWhenEaten(new Time(System.currentTimeMillis()));
//        currDay.get_meals().add(recommendedAmountPerMeal);
//        currDay.updateFeedingTimes();
//
//        Date day = new java.sql.Date(currDay.get_meals().getLast().getTimeToEat().getTime());
//
//        if (day.after(currDay.get_currDate())) {
//            // adding the new meal to the new day & eras it from yesterday.
////            history.addNextDay(this);
//            history.getLast().get_meals().addMeal(history.getLast().getPrev().getLastMeal());
//            history.getLast().get_meals().getLast().getPrev().setNext(null);
//        }
////        DatabaseManager.addNewMeal(this,newMealNode);
//    }
//
//    public synchronized DatabaseReference uploadToDb(){
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(parent.getInstitutionName().getName()).
//                child(LocalUser.UserType.PARENT.toString()).child("Children").child(getName());
//        ref.setValue(this);
//        return ref;
//    }
//
//    public void upload(){
//        FirebaseDatabase.getInstance().getReference().child(parent.getInstitutionName().getName()).
//                child(LocalUser.UserType.PARENT.toString()).child("Children").child(getName()).setValue(this);
//    }
//
//    public void calcRecommendedAmountPerMeal(double weight) {
//        this.recommendedAmountPerMeal =  (int) ((double)(weight * 150 / 8));;
//    }
//
//    @Override
//    public String toString() {
//        return "name='" + name +
//                "\nid=" + id +
//                "\nhistory=" + history +
//                "\nageInMonths=" + ageInMonths +
//                "\n_weight=" + _weight +
//                "\nrecommendedAmountOfMeals=" + recommendedAmountOfMeals +
//                "\nrecommendedAmountPerMeal=" + recommendedAmountPerMeal ;
//    }
//
//    public int getRecommendedAmountPerMeal(){
//        return recommendedAmountPerMeal;
//    }
//
//    public int getRecommendedAmountOfMeals() {
//        return recommendedAmountOfMeals;
//    }
//
//    public void setRecommendedAmountOfMeals(int recommendedAmountOfMeals) {
//        this.recommendedAmountOfMeals = recommendedAmountOfMeals;
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
//
//
//    public void setAgeInMonths(int ageInMonths) {
//        this.ageInMonths = ageInMonths;
//    }
//
//    public Parent getParent() {
//        return parent;
//    }
//
//    public void setParentName(Parent parent) {
//        this.parent = parent;
//    }
//
//    public void setRecommendedAmountPerMeal(int recommendedAmountPerMeal) {
//        this.recommendedAmountPerMeal = recommendedAmountPerMeal;
//    }
//}