package com.example.nailzip.member;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.nailzip.MainActivity;
import com.example.nailzip.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class StartActivity extends AppCompatActivity {

    private EditText edt_id, edt_pw;
    private Button btn_login, btn_register;
    private TextView txt_findID, txt_findPW;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private MemberViewModel memberViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        init();

        memberViewModel = new ViewModelProvider(this).get(MemberViewModel.class);
        memberViewModel.getUserMutableLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {

                Intent startMain = new Intent(StartActivity.this, MainActivity.class);
                startActivity(startMain);

                finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_id.getText().toString().trim();
                String password = edt_pw.getText().toString().trim();

                // 아이디 입력 안했으면
                if (email.isEmpty()) {
                    Toast.makeText(StartActivity.this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 아이디가 이메일 형식을 벗어낫을경우
                if (!email.contains("@")) {
                    Toast.makeText(StartActivity.this, "아이디를 이메일 형식으로 작성해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 비밀번호 입력 안했으면
                if (password.isEmpty()) {
                    Toast.makeText(StartActivity.this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                memberViewModel.login(email, password);

//                Intent starMainActivity = new Intent(StartActivity.this, MainActivity.class);
//                startActivity(starMainActivity);
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startRegisterqueActivity = new Intent(StartActivity.this, RegisterqueActivity.class);
                startActivity(startRegisterqueActivity);
            }
        });

        txt_findID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startFindIDActivity = new Intent(StartActivity.this, FindIDActivity.class);
                startActivity(startFindIDActivity);
            }
        });

        txt_findPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startFindPWActivity = new Intent(StartActivity.this, FindPwActivity.class);
                startActivity(startFindPWActivity);
            }
        });
    }

    public void init(){
        edt_id = findViewById(R.id.edt_id);
        edt_pw = findViewById(R.id.edt_pw);
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_register = (Button)findViewById(R.id.btn_register);
        txt_findID = findViewById(R.id.txt_findID);
        txt_findPW = findViewById(R.id.txt_findPW);
    }
}