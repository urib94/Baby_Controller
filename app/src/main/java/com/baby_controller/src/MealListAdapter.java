package com.baby_controller.src;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.baby_controller.R;

import java.sql.Time;
import java.text.MessageFormat;
import java.util.ArrayList;

public class MealListAdapter extends ArrayAdapter<Meal> {
    private final LayoutInflater mLayoutInflater;
    private final ArrayList<Meal> mMeals;
    private final int  mViewResourceId;



    public MealListAdapter(Context context, int tvResourceId, ArrayList<Meal> meals){
        super(context, tvResourceId, meals);
        this.mMeals = meals;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = tvResourceId;
}

    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mLayoutInflater.inflate(mViewResourceId, null);

        Meal meal = mMeals.get(position);

        if (meal != null) {
            TextView timeOfEating = convertView.findViewById(R.id.time_of_eating);
            TextView recommendedAmount = convertView.findViewById(R.id.tv_recommended_amount);
            TextView eatenAmount = convertView.findViewById(R.id.eaten_amount);

            if (timeOfEating != null) {
                Time tmp = new Time(meal.getWhenEaten());
                String res = getContext().getString(R.string.recommended_amount_adapter);
                timeOfEating.setText(MessageFormat.format("{0}:{1}", String.valueOf(tmp.getHours()), String.valueOf(tmp.getMinutes())));
            }
            if (recommendedAmount != null) {
                recommendedAmount.setText(MessageFormat.format("{0}mL", String.valueOf(meal.getRecommendedAmount())));
            }
            if(eatenAmount != null){
                String res = getContext().getString(R.string.eaten_amount);
                eatenAmount.setText(MessageFormat.format("{0}mL", String.valueOf(meal.getReceivedAmount())));

            }
        }

        return convertView;
    }
}
