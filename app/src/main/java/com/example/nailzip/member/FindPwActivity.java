package com.example.nailzip.member;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nailzip.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class FindPwActivity extends AppCompatActivity {

    private EditText edt_username, edt_phonenum, edt_email;
    private Button btn_complete;
    private Toolbar tb_back;
    private static final String TAG = "FindPwActivity";
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        init();

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                String useremail = edt_email.getText().toString().trim();
                String username = edt_username.getText().toString().trim();
                String phonenum = edt_phonenum.getText().toString().trim();

                if(useremail.isEmpty() || username.isEmpty() || phonenum.isEmpty()){
                    Toast.makeText(FindPwActivity.this, "빈칸을 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                else{
                    firestore.collection("users")
                            .whereEqualTo("username", username)
                            .whereEqualTo("email", useremail)
                            .whereEqualTo("phonenum", phonenum)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        Log.d(TAG, "회원정보 일치");
                                        firebaseAuth.sendPasswordResetEmail(useremail)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Log.d(TAG, "이메일 전송 성공");
                                                            Toast.makeText(FindPwActivity.this, "이메일을 확인해주세요", Toast.LENGTH_SHORT).show();
                                                            Intent startLoginActivity = new Intent(FindPwActivity.this,StartActivity.class);
                                                            startActivity(startLoginActivity);
                                                            finish();
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.e(TAG, "이메일 전송 실패", e);
                                                        Toast.makeText(FindPwActivity.this, "이메일 전송에 실패하였습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "일치하는 회원정보 없음");
                                    Toast.makeText(FindPwActivity.this, "일치하는 회원정보가 없습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        // Toolbar 활성화
        setSupportActionBar(tb_back);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 생성
        getSupportActionBar().setTitle(null); // Toolbar 제목 제거
    }

    // Toolbar 뒤로가기 버튼 활성화 코드
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void init(){
        edt_username = findViewById(R.id.edt_username);
        edt_phonenum = findViewById(R.id.edt_phonenum);
        edt_email = findViewById(R.id.edt_email);
        btn_complete = (Button) findViewById(R.id.btn_complete);
        tb_back = findViewById(R.id.tb_back);
    }
}