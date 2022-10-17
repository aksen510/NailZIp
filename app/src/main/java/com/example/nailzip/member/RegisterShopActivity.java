package com.example.nailzip.member;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.nailzip.R;
import com.example.nailzip.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterShopActivity extends AppCompatActivity {

    private static final String TAG = "RegisterShopActivity";

    private EditText edt_shopname, edt_ceoname, edt_email, edt_pw, edt_checkPw, edt_phonenum, edt_address, edt_shopphonenum;
    private Button btn_complete;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private MemberViewModel memberViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_shop);

        memberViewModel = new ViewModelProvider(this).get(MemberViewModel.class);
        init();

        edt_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 주소 검색 웹뷰 화면으로 이동
                Intent startSearchAddress = new Intent(RegisterShopActivity.this, SearchAddressActivity.class);
                getSearchResult.launch(startSearchAddress);
                //startActivity(startSearchAddress);
            }
        });

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //가입 정보 가져오기
                final String email = edt_email.getText().toString().trim();
                String pwd = edt_pw.getText().toString().trim();
                String pwdcheck = edt_checkPw.getText().toString().trim();
                String username = edt_ceoname.getText().toString().trim();
                String phonenum = edt_phonenum.getText().toString().trim();
                String shopname = edt_shopname.getText().toString().trim();

                // Todo : Nailshop 모델에 추가 저장
                String shopphone = edt_shopphonenum.getText().toString().trim();
                String address = edt_address.getText().toString().trim();

                if (email.isEmpty() || pwd.isEmpty() || pwdcheck.isEmpty() || username.isEmpty() || phonenum.isEmpty() || shopname.isEmpty()) {
                    Toast.makeText(RegisterShopActivity.this, "빈 칸을 채워주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    if(pwd.equals(pwdcheck)){

                        User userAccount = new User();
                        userAccount.setPosition(1);
                        userAccount.setEmail(email);
                        userAccount.setUsername(username);
                        userAccount.setPhonenum(phonenum);
                        userAccount.setShopname(shopname);

//                        userAccount.setUid(firebaseAuth.getCurrentUser().getUid());

                        final ProgressDialog mDialog = new ProgressDialog(RegisterShopActivity.this);

                        memberViewModel.register(email, pwd, userAccount);
                        mDialog.setMessage("가입중입니다");
                        mDialog.show();
                        memberViewModel.getSaveUserInfoMutableLiveData().observe(RegisterShopActivity.this, new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean aBoolean) {
                                if (aBoolean) {
                                    mDialog.dismiss();
                                    Toast.makeText(RegisterShopActivity.this, "가입 성공", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterShopActivity.this, StoreInfoActivity.class);
                                    startActivity(intent);
                                }else{
                                    mDialog.dismiss();
                                    Toast.makeText(RegisterShopActivity.this, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(RegisterShopActivity.this, "비밀번호가 일치하지 않습니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });
    }

    private final ActivityResultLauncher<Intent> getSearchResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // SearchAddressActivity로부터의 결과 값이 이곳으로 전달됨 (setResult에 의해)
                if(result.getResultCode() == RESULT_OK){
                    if(result.getData() != null){
                        String data = result.getData().getStringExtra("data");
                        edt_address.setText(data);
                    }
                }
            }
    );

    public void init(){
        edt_shopname = findViewById(R.id.edt_shopname);
        edt_ceoname = findViewById(R.id.edt_ceoname);
        edt_email = findViewById(R.id.edt_email);
        edt_pw = findViewById(R.id.edt_pw);
        edt_checkPw = findViewById(R.id.edt_pw_check);
        edt_phonenum = findViewById(R.id.edt_phonenum);
        edt_shopphonenum = findViewById(R.id.edt_shopphonenum);
        edt_address = findViewById(R.id.edt_address);
        btn_complete = (Button) findViewById(R.id.btn_complete);
    }
}