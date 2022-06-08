package com.example.nailzip.member;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.nailzip.model.NailshopData;
import com.example.nailzip.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MemberModel {

    private String TAG = "MemberModel";

    private Application application;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private MutableLiveData<FirebaseUser> userMutableLiveData;
    private MutableLiveData<Boolean> isSuccessful;
    private MutableLiveData<Boolean> logoutMutableLiveData;
    private MutableLiveData<Boolean> saveUserInfoMutableLiveData;
    private MutableLiveData<FirebaseFirestore> userInfoMutableLiveData;
    private MutableLiveData<FirebaseFirestore> shopMutableLiveData;

    public MemberModel(Application application) {
        this.application = application;

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userMutableLiveData = new MutableLiveData<>();
        logoutMutableLiveData = new MutableLiveData<>();
        saveUserInfoMutableLiveData = new MutableLiveData<>();
        isSuccessful = new MutableLiveData<>();

        if (firebaseAuth.getCurrentUser() != null) {
            userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
            logoutMutableLiveData.postValue(false);
        }
    }

    public void register(String email, String password, User userAccount){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            userMutableLiveData.postValue(firebaseAuth.getCurrentUser());
                            firestore.collection("users")
                                    .document(firebaseAuth.getCurrentUser().getUid())
                                    .set(userAccount)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        saveUserInfoMutableLiveData.postValue(true);
                                        Log.d(TAG, "회원정보 저장 성공");
                                    }
                                    else {
                                        saveUserInfoMutableLiveData.postValue(false);
                                        Log.w(TAG, "회원정보 저장 오류");
                                    }
                                }
                            });
                        }else {
                            saveUserInfoMutableLiveData.postValue(false);
                            Log.w(TAG, "회원가입 오류");
                        }
                    }
                });
    }

    public void storeInfo(NailshopData shopAccount){
        firestore.collection("users")
                .document(firebaseAuth.getCurrentUser().getUid())
                .collection("shopInfo")
                .add(shopAccount)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "매장 등록 완료");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "매장 등록 실패");
                    }
                });

        firestore.collection("shoplist")
                .document()
                .set(shopAccount)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Log.d(TAG, "매장 등록 완료2");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "매장 등록 실패2");
                    }
                });
    }

    public void login(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userMutableLiveData.postValue(firebaseAuth.getCurrentUser());

                        } else {
                            Toast.makeText(application, "로그인 오류", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void logout(){
        firebaseAuth.signOut();
        logoutMutableLiveData.postValue(true);
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public MutableLiveData<Boolean> getIsSuccessful() {
        return isSuccessful;
    }

    public MutableLiveData<Boolean> getLogoutMutableLiveData() {
        return logoutMutableLiveData;
    }

    public MutableLiveData<Boolean> getSaveUserInfoMutableLiveData() {
        return saveUserInfoMutableLiveData;
    }

    public MutableLiveData<FirebaseFirestore> getUserInfoMutableLiveData() {
        return userInfoMutableLiveData;
    }

    public MutableLiveData<FirebaseFirestore> getShopMutableLiveData() {
        return shopMutableLiveData;
    }
}
