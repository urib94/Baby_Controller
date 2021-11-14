//package com.baby_controller;
//
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.baby_controller.src.BTActivity;
//import com.baby_controller.src.Baby;
//import com.baby_controller.src.BabyListAdapter;
//import com.baby_controller.src.Config;
//import com.baby_controller.src.Institution;
//import com.baby_controller.src.LocalUser;
//import com.baby_controller.src.Parent;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//
//public class FeedingActivity extends AppCompatActivity  {
//    private static final String TAG = "Feeding Activity";
//
//    //todo tranfer to config
//    public static double foodWight = 0;
////    BluetoothAdapter mBluetoothAdapter;
//
//
////    BluetoothConnectionService mBluetoothConnection;
//
//    private Button feed;
//
//    public static Baby babyToFeed;
//
//    //todo check if its making problems
//    private TextView measuredWight;
//
//    public ArrayList<Baby> mHungryBabies = new ArrayList<>();
//
//    public ArrayList<Baby> mReallyHungryBabies = new ArrayList<>();
//
//    public BabyListAdapter mReallyHungryBabyListAdapter;
//
//    public BabyListAdapter mHungryBabyListAdapter;
//
//
//    ListView reallyHungryBabies;
//    ListView hungryBabies;
//
//
//
//    private void configureBabyChooserButtons() {
//        reallyHungryBabies = (ListView) findViewById(R.id.really_hungry_list);
//        hungryBabies = (ListView) findViewById(R.id.hungry_list);
//
//        reallyHungryBabies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                babyToFeed = mReallyHungryBabies.get(position);
//                Intent intent  = new Intent(FeedingActivity.this,AdministerFoodActivity.class);
//                startActivity(intent);
//            }
//
//        });
//
//        hungryBabies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                babyToFeed = mHungryBabies.get(position);
//                Intent intent  = new Intent(FeedingActivity.this,AdministerFoodActivity.class);
//                startActivity(intent);
//            }
//        });
//
//    }
//
//
//
//
//    //copied
//    public void babyListsMaker(){
//        //get the institute of the user from the database
//        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Institutions")
//                .child(Config.getCurrentUser().getInstitutionName());
//
//        mDatabaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Institution institution = dataSnapshot.getValue(Institution.class);
//                if(institution != null){
//                    for(LocalUser parent : institution.getParents()){
//                        mHungryBabies.addAll(((Parent)parent).getBabiesNeedToFeed());
//                    }
//                    mReallyHungryBabyListAdapter = new BabyListAdapter(FeedingActivity.this,R.layout.baby_adapter_view, mHungryBabies);
//                    reallyHungryBabies.setAdapter(mReallyHungryBabyListAdapter);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//        //the same for the babies that are not hungry
//        DatabaseReference mDatabaseReference2 = FirebaseDatabase.getInstance().getReference().child("Institutions")
//                .child(Config.getCurrentUser().getInstitutionName());
//
//        mDatabaseReference2.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Institution institution = dataSnapshot.getValue(Institution.class);
//                if(institution != null){
//                    for(LocalUser parent : institution.getParents()){
//                        mHungryBabies.addAll(((Parent)parent).getBabiesNotNeedToFeed());
//                    }
//                    mHungryBabyListAdapter = new BabyListAdapter(FeedingActivity.this,R.layout.baby_adapter_view, mHungryBabies);
//                    hungryBabies.setAdapter(mReallyHungryBabyListAdapter);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    @Override
//    protected void onDestroy() {
//        Log.d(TAG, "onDestroy: called.");
//        super.onDestroy();
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //todo change the content view to bluetooth connection (and make one)
//        if(Config.getCurrentUser().getDefaultDevice() == null ||
//                ! Config.getBluetoothConnectionManger().getmBTSocket().getRemoteDevice().equals(Config.getCurrentUser().getDefaultDevice()) ) {
//            Intent intent = new Intent(FeedingActivity.this, BTActivity.class);
//            startActivity(intent);
//        }else {
//            setContentView(R.layout.choos_baby_to_feed);
//            configureBabyChooserButtons();
//            babyListsMaker();
//        }
//
//    }
//
//    public static synchronized void setFoodWight(double newFoodWight){
//        foodWight = newFoodWight;
//    }
//
//    public static synchronized double getFoodWight(){
//        return foodWight;
//    }
//
//}
