package com.baby_controller.src;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.MultiFactor;
import com.google.firebase.auth.MultiFactorInfo;
import com.google.firebase.auth.UserInfo;

import java.util.List;

@SuppressLint("ParcelCreator")
public class UserManger extends FirebaseUser {
    @Nullable
    @Override
    public Uri getPhotoUrl() {
        return null;
    }

    @Nullable
    @Override
    public FirebaseUserMetadata getMetadata() {
        return null;
    }

    @NonNull
    @Override
    public MultiFactor getMultiFactor() {
        return null;
    }

    @Nullable
    @Override
    public String getDisplayName() {
        return null;
    }

    @Nullable
    @Override
    public String getEmail() {
        return null;
    }

    @Nullable
    @Override
    public String getPhoneNumber() {
        return null;
    }

    @NonNull
    @Override
    public String getProviderId() {
        return null;
    }

    @Nullable
    @Override
    public String getTenantId() {
        return null;
    }

    @NonNull
    @Override
    public String getUid() {
        return null;
    }

    @Override
    public boolean isEmailVerified() {
        return false;
    }

    @NonNull
    @Override
    public List<? extends UserInfo> getProviderData() {
        return null;
    }

    @Override
    public boolean isAnonymous() {
        return false;
    }

    @NonNull
    @Override
    public FirebaseApp zza() {
        return null;
    }

    @NonNull
    @Override
    public FirebaseUser zzb() {
        return null;
    }

    @NonNull
    @Override
    public FirebaseUser zzc(@NonNull List<? extends UserInfo> list) {
        return null;
    }

    @Override
    @NonNull
    public zzwq zzd() {
        return null;
    }

    @NonNull
    @Override
    public String zze() {
        return null;
    }

    @NonNull
    @Override
    public String zzf() {
        return null;
    }

    @Nullable
    @Override
    public List<String> zzg() {
        return null;
    }

    @Override
    public void zzi(@NonNull List<MultiFactorInfo> list) {

    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
