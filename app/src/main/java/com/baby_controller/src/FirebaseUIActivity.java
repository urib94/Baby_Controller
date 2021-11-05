//package com.baby_controller.src;
//
//import android.content.Intent;
//
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.appcompat.app.AppCompatActivity;
//import android.content.Intent;
//import android.os.Bundle;
//
//import androidx.activity.result.ActivityResultCallback;
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
////import com.google.firebase.auth.internal
////import com.google.firebase.auth.FirebaseAuthUIActivityResultContract;
//
//import com.google.firebase.auth.*;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.ActionCodeSettings;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//public class EmailPasswordFragment extends AppCompatActivity {
//
//    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
//            new FirebaseAuthUIActivityResultContract(),
//            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
//                @Override
//                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
//                    onSignInResult(result);
//                }
//            }
//    );
//    // [END auth_fui_create_launcher]
//
//}
