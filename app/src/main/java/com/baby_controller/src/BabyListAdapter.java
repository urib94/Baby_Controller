package com.baby_controller.src;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.baby_controller.R;

import java.util.ArrayList;

public class BabyListAdapter extends ArrayAdapter<Baby> {
    private LayoutInflater mLayoutInflater;
    private ArrayList<Baby> mBabies;
    private int  mViewResourceId;



    public BabyListAdapter(Context context, int tvResourceId, ArrayList<Baby> babies){
        super(context, tvResourceId, babies);
        this.mBabies = babies;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = tvResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mLayoutInflater.inflate(mViewResourceId, null);

        Baby baby = mBabies.get(position);

        if (baby != null) {
            TextView babyName = (TextView) convertView.findViewById(R.id.tv_baby_name);
            TextView recommendedAmount = (TextView) convertView.findViewById(R.id.tv_recommended_amount);
            TextView timeToEat = (TextView) convertView.findViewById(R.id.tv_time_to_eat);

            if (babyName != null) {
                babyName.setText(baby.getName());
            }
            if (recommendedAmount != null) {
                recommendedAmount.setText(String.valueOf(baby.getRecommendedAmountPerMeal()));
            }
            if(timeToEat != null && baby.getHistory().get(baby.getHistory().size() - 1).getTimeToEat() != null){
                timeToEat.setText(baby.getHistory().get(baby.getHistory().size() - 1).getTimeToEat().toString());
            }
        }

        return convertView;
    }
}
