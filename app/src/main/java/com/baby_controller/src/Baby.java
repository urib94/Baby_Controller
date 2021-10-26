package com.baby_controller.src;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.Date;
import java.util.LinkedList;

public  class Baby {
    protected DatabaseReference reference;
    private Parent parent;
    private String name = "";
    private int id = 0;
    //MealList history;
    LinkedList<Meal> history = new LinkedList<Meal>();
    private int ageInMonths = 0;
    Meal meal = new Meal(60);
//    private Parent[] parents = new Parent[2];

    private double _weight = 0;
    private int recommendedAmountOfMeals = 0;
    private int recommendedAmountPerMeal = 0;

    public Baby(){}

    public Baby copyForParent(){
        Baby tmp = new Baby();
        tmp.recommendedAmountPerMeal = this.recommendedAmountPerMeal;
        tmp.recommendedAmountOfMeals = this.recommendedAmountOfMeals;
        tmp._weight= this._weight;
        tmp.ageInMonths = ageInMonths;
        tmp.history = this.history;
        tmp.parent = parent;
        tmp.name = this.name;
        return tmp;
    }

    public Baby(String name) {
        this.name = name;
        //history = new MealList();
    }

    public Baby(String name, double weight){
        this._weight = weight;
        this.name = name;
        calcRecommendedAmountPerMeal(weight);
       // history = new MealList(recommendedAmountPerMeal);
    }

    public Baby(double weight){
        _weight = weight;
        calcRecommendedAmountPerMeal(weight);
        //history = new MealList(recommendedAmountPerMeal);
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
        createNextMeal(amount);
        if(reference == null){
            if(parent.getReference() != null){
                reference = parent.getReference().child("children").child(name);
            } else {
                reference = FirebaseDatabase.getInstance().getReference().child(parent.getInstitution()
                        .getName()).child("parents").child(parent.getUserName()).child("children").child(name);
            }
        }

        for (int i = 0; i < history.size(); i++){
            history.get(i).uploadToDb(reference,i + 1);
        }
        // TODO: 10/25/2021 notify parents
    }

    private void createNextMeal(int amount) {
        if(history.size() == 0){
            history.add(new Meal(amount));
        }
        history.get(history.size() - 1).setReceivedAmount(amount);
        history.get(history.size() - 1).setEaten(1);
        history.get(history.size() - 1).setWhenEaten(new Time(System.currentTimeMillis()));
        history.get(history.size() - 1).setEaten(1);

        history.add(new Meal(recommendedAmountPerMeal));
        history.get(history.size() - 1).setEaten(-1);
        history.get(history.size() - 1).calcTimeToEat(history.get(history.size() - 2));
    }


    public void uploadToDb(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(parent.getInstitution().getName())
                .child(User.UserType.PARENT.toString()).child(getParent().getUserName())
                .child("children").child(getName());
        ref.child("weight").setValue(this._weight);
        ref.child("age in months").setValue(this.ageInMonths);
        ref.child("id").setValue(this.id);
        ref.child("name").setValue(this.name);
        ref.child("recommended amount of meals").setValue(this.recommendedAmountOfMeals);
        ref.child("parent user name").setValue(this.parent.getUserName());
        ref.child("recommended amount per meal").setValue(this.recommendedAmountPerMeal);
//        if(reference != null) {
//            ref.child("reference");//.setValue(this.reference);
//        }
        if(history.size() != 0) {
            if(ref.child("Meals") != null) {
                for (int i = 0; i < history.size(); i++){
                    history.get(i).uploadToDb(ref.child("Meals").getRef(),i + 1);
                }
            }
        }

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Baby tmp = dataSnapshot.getValue(Baby.class);
                _weight = tmp._weight;
                ageInMonths = tmp.ageInMonths;
                id = tmp.id;
                name = tmp.name;
                recommendedAmountOfMeals = tmp.recommendedAmountOfMeals;
                parent = tmp.parent;
                recommendedAmountPerMeal = tmp.recommendedAmountPerMeal;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

            }
        };
        reference.addValueEventListener(postListener);

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
        parent.getChildren().add(copyForParent());

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

    //public MealList getHistory() {
//        return history;
//    }

//    public void setHistory(MealList history) {
//        this.history = history;
//    }

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