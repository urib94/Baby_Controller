package com.baby_controller.src;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public  class Baby {
    protected DatabaseReference reference;
    private Parent parent;
    private String name = "";
    private int id = 0;
    MealList history;
    private int ageInMonths = 0;
//    private Parent[] parents = new Parent[2];

    private double _weight = 0;
    private int recommendedAmountOfMeals = 0;
    private int recommendedAmountPerMeal = 0;

    public Baby(){}

    public Baby(String name) {
        this.name = name;
        //history = new MealList();
    }

    public Baby(String name, double weight){
        this._weight = weight;
        this.name = name;
        calcRecommendedAmountPerMeal(weight);
        history = new MealList(recommendedAmountPerMeal);
    }

    public Baby(double weight){
        _weight = weight;
        calcRecommendedAmountPerMeal(weight);
        history = new MealList(recommendedAmountPerMeal);
    }

    public void calcRecommendedAmountPerMeal(double weight) {
        this.recommendedAmountPerMeal =  (int) ((double)(weight * 150 / 8));;
    }

    public void set_recommendedAmountOfMeals(int ageInMonths) {
        if (ageInMonths <= 1) {
            recommendedAmountOfMeals = 12;
        } else if (ageInMonths <= 6) {
            recommendedAmountOfMeals = 15;
        } else if (ageInMonths <= 9) {
            recommendedAmountOfMeals = 5;
        }
        recommendedAmountOfMeals = 4;
        // TODO: 10/21/2021 dasdasdasda
    }

    public void eatingNextMeal(int amount) {
        history.add(amount);
        // TODO: 10/23/2021 redo this function





//        int dayNum = new Date(System.currentTimeMillis()).getDay();
//        Day currDay = history.getLast();
//        if (currDay == null){
//            System.out.println("fuck");
//            currDay = new Day(recommendedAmountOfMeals);
//        }
//        if (currDay.get_meals().getAmountOfMeals() > recommendedAmountOfMeals) {
//            // TODO: 10/12/2021 send a message to the user indicating that the child hase exceeded the daily
//            //  recommended amount of meals, ask him if he wold like to give home another meal?
//        }
//        int foodEaten = 0;
//        Meal tmp = currDay.get_meals().get_head();
//
//        while (tmp.get_next() != null) {
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
//            history.getLast().get_meals().addMeal(history.getLast().get_prev().getLastMeal());
//            history.getLast().get_meals().getLast().get_prev().set_next(null);
//        }
//        DatabaseManager.addNewMeal(this,newMealNode);



    }

    public void uploadToDb(){
        FirebaseDatabase.getInstance().getReference().child(parent.getInstitutionName())
                .child(User.UserType.PARENT.toString()).child(getParent().getUserName())
                .child("Children").child(getName()).setValue(this);

    }



    @Override
    public String toString() {
        return "name='" + name +
                "\nid=" + id +
                "\nhistory=" + history +
                "\nageInMonths=" + ageInMonths +
                "\n_weight=" + _weight +
                "\nrecommendedAmountOfMeals=" + recommendedAmountOfMeals +
                "\nrecommendedAmountPerMeal=" + recommendedAmountPerMeal ;
    }

    public DatabaseReference getReference() {
        return reference;
    }

    public void setReference(DatabaseReference reference) {
        this.reference = reference;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MealList getHistory() {
        return history;
    }

    public void setHistory(MealList history) {
        this.history = history;
    }

    public int getAgeInMonths() {
        return ageInMonths;
    }

    public void setAgeInMonths(int ageInMonths) {
        this.ageInMonths = ageInMonths;
    }

    public double get_weight() {
        return _weight;
    }

    public void set_weight(double _weight) {
        this._weight = _weight;
    }

    public int getRecommendedAmountOfMeals() {
        return recommendedAmountOfMeals;
    }

    public void setRecommendedAmountOfMeals(int recommendedAmountOfMeals) {
        this.recommendedAmountOfMeals = recommendedAmountOfMeals;
    }

    public int getRecommendedAmountPerMeal() {
        return recommendedAmountPerMeal;
    }

    public void setRecommendedAmountPerMeal(int recommendedAmountPerMeal) {
        this.recommendedAmountPerMeal = recommendedAmountPerMeal;
    }
}