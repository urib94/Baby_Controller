package com.baby_controller.src;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.LinkedList;
import java.util.List;

public  class Baby {
    protected DatabaseReference reference;
    private LocalUser parent;
    private String name = "";
    private int id = 0;
    //MealList history;
    List<Meal> history = new LinkedList<Meal>();
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
        if(history.size() == 0){
            history.add(new Meal(amount));
        }
        history.get(history.size() - 1).setEaten(1);
        history.get(history.size() - 1).setReceivedAmount(amount);
        history.get(history.size() - 1).setWhenEaten(new Time(System.currentTimeMillis()));
        createNextMeal();
        int count = 0;
        for(LocalUser parents :parent.getInstitute().getParents()){
            if(parent.getEmail().equals(parents.getEmail())){
                break;
            }
            count++;
        }

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Institutions")
                .child(getParent().getInstitutionName()).child("parents").child(String.valueOf(count));
        myRef.child("babies").child(name).setValue(this);

        //notify the parent using firebase cloud messaging



        // TODO: 10/25/2021 notify parents
    }

    //upload this Baby to firebase database use transaction



    private void createNextMeal() {
        history.add(new Meal(recommendedAmountPerMeal));
        history.get(history.size() - 1).setEaten(-1);
        history.get(history.size() - 1).calcTimeToEat(history.get(history.size() - 2));
    }


    public void uploadToDb(){
        reference = FirebaseDatabase.getInstance().getReference().child("Institutions").child(getParent().getInstitute().getName())
        .child("parents").child(getParent().getUserName()).child("children");
        Transaction.Handler tmp = new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                currentData.setValue(Baby.this);
                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                if (error != null) {
                    Log.d("Firebase", "error: " + error.getMessage());
                }

            }
        } ;
        reference.runTransaction(tmp);
        setListeners();




//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(parent.getInstitution().getName())
//                .child(LocalUser.UserType.PARENT.toString()).child(getParent().getUserName())
//                .child("children").child(getName());
//        ref.child("weight").setValue(this._weight);
//        ref.child("age in months").setValue(this.ageInMonths);
//        ref.child("id").setValue(this.id);
//        ref.child("name").setValue(this.name);
//        ref.child("recommended amount of meals").setValue(this.recommendedAmountOfMeals);
//        ref.child("parent user name").setValue(this.parent.getUserName());
//        ref.child("recommended amount per meal").setValue(this.recommendedAmountPerMeal);
////        if(reference != null) {
////            ref.child("reference");//.setValue(this.reference);
////        }
//        if(history.size() != 0) {
//            if(ref.child("Meals") != null) {
//                for (int i = 0; i < history.size(); i++){
//                    history.get(i).uploadToDb(ref.child("Meals").getRef(),i + 1);
//                }
//            }
//        }
//
//        ValueEventListener postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Get Post object and use the values to update the UI
//                Baby tmp = dataSnapshot.getValue(Baby.class);
//                _weight = tmp._weight;
//                ageInMonths = tmp.ageInMonths;
//                id = tmp.id;
//                name = tmp.name;
//                recommendedAmountOfMeals = tmp.recommendedAmountOfMeals;
//                parent = tmp.parent;
//                recommendedAmountPerMeal = tmp.recommendedAmountPerMeal;
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Getting Post failed, log a message
//
//            }
//        };
//        reference.addValueEventListener(postListener);

    }

    private void setListeners() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Baby tmp = dataSnapshot.child(name).getValue(Baby.class);
                _weight = tmp._weight;
                ageInMonths = tmp.ageInMonths;
                id = tmp.id;
                name = tmp.name;
                recommendedAmountOfMeals = tmp.recommendedAmountOfMeals;
                parent = tmp.parent;
                recommendedAmountPerMeal = tmp.recommendedAmountPerMeal;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    //returns the parent as a Local user, cast if need be
    public LocalUser getParent() {
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

    public void setNeedToEat(){
        if (history.size() == 0){
            history.add(new Meal(60));
        }
        history.get(history.size() -1).setTimeToEat(new Time(System.currentTimeMillis() - 1000));
    }


    //Baby to Json
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("name", name);
            json.put("id", id);
            json.put("ageInMonths", ageInMonths);
            json.put("_weight", _weight);
            json.put("recommendedAmountOfMeals", recommendedAmountOfMeals);
            json.put("recommendedAmountPerMeal", recommendedAmountPerMeal);
            json.put("parent", parent.getUserName());
            for (Meal meal : history) {
                json.put("history", meal.toJson());
            }

        }catch (JSONException e){
            e.printStackTrace();
        }
        return json;
    }

    //Baby from Json
    public static Baby fromJson(JSONObject json) {
        Baby baby = new Baby();
        try {
            baby.name = json.getString("name");
            baby.id = json.getInt("id");
            baby.ageInMonths = json.getInt("ageInMonths");
            baby._weight = json.getDouble("_weight");
            baby.recommendedAmountOfMeals = json.getInt("recommendedAmountOfMeals");
            baby.recommendedAmountPerMeal = json.getInt("recommendedAmountPerMeal");
            baby.parent = Parent.fromJson(json.getJSONObject("parent"));
            baby.history = new LinkedList<Meal>();
            JSONArray jsonHistory = json.getJSONArray("history");
            for (int i = 0; i < jsonHistory.length(); i++) {
                baby.history.add(Meal.fromJson(jsonHistory.getJSONObject(i)));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return baby;
    }

    public List<Meal> getHistory() {
        return history;
    }

    public void setHistory(List<Meal> history) {
        this.history = history;
    }
}