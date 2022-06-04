package com.example.nailzip.mypage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import java.util.Objects;

public class ChangepwActivity extends AppCompatActivity {

    private EditText edt_newpw, edt_pw_check;
    private Button btn_complete;
    private Toolbar tb_changepw;
    private static final String TAG = "EditInfoActivity";
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepw);

        init();

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                String newPW = edt_newpw.getText().toString().trim();
                String checkPW = edt_pw_check.getText().toString().trim();

                if (newPW.isEmpty() == false || checkPW.isEmpty() == false) {
                    if(newPW.equals(checkPW)){
                        user.updatePassword(newPW)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Log.d(TAG, "비밀번호 변경 성공");
                                            //성공적으로 변경되었을 시 설정으로 돌아감
                                            finish();
                                            Toast.makeText(ChangepwActivity.this, "비밀번호 변경 성공", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(TAG, "오류 발생",e);
                                    }
                                });
                    }
                    //비밀번호 일치하지 않을 때
                    else {
                        Toast.makeText(ChangepwActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                //빈칸 존재
                else {
                    Toast.makeText(ChangepwActivity.this, "빈칸을 채워주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        // Toolbar 활성화
        setSupportActionBar(tb_changepw);
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
        tb_changepw = findViewById(R.id.tb_changepw);
        edt_newpw = findViewById(R.id.edt_newpw);
        edt_pw_check = findViewById(R.id.edt_pw_check);
        btn_complete = findViewById(R.id.btn_complete);
    }
}