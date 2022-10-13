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
import androidx.lifecycle.ViewModelProvider;

import com.example.nailzip.MainActivity;
import com.example.nailzip.R;
import com.example.nailzip.model.NailshopData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class StoreInfoActivity extends AppCompatActivity {

    //Todo: 가격 추가
    private EditText edt_shopname, edt_opentime, edt_shopphonenum, edt_address, edt_price_nail, edt_price_pedi, edt_memocontent, edt_closed;
    private Button btn_next;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private MemberViewModel memberViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);

        memberViewModel = new ViewModelProvider(this).get(MemberViewModel.class);
        init();

        edt_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 주소 검색 웹뷰 화면으로 이동
                Intent startSearchAddress = new Intent(StoreInfoActivity.this, SearchAddressActivity.class);
                getSearchResult.launch(startSearchAddress);
                //startActivity(startSearchAddress);
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shopname = edt_shopname.getText().toString().trim();
                String opentime = edt_opentime.getText().toString().trim();
                String shopphonenum = edt_shopphonenum.getText().toString().trim();
                String address = edt_address.getText().toString().trim();
                String price_nail = edt_price_nail.getText().toString().trim();
                String price_pedi = edt_price_pedi.getText().toString().trim();
                String memocontent = edt_memocontent.getText().toString().trim();
                String closed = edt_closed.getText().toString().trim();

                if (shopname.isEmpty() || opentime.isEmpty() || shopphonenum.isEmpty() || address.isEmpty() || price_nail.isEmpty() || price_pedi.isEmpty() || memocontent.isEmpty() || closed.isEmpty()) {
                    Toast.makeText(StoreInfoActivity.this, "빈 칸을 채워주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {

                    NailshopData shopAccount = new NailshopData();
                    shopAccount.setShopname(shopname);
                    shopAccount.setTime(opentime);
                    shopAccount.setClosed(closed);
                    shopAccount.setShopphone(shopphonenum);
                    shopAccount.setLocation(address);
                    shopAccount.setPrice_nail(price_nail);
                    shopAccount.setPrice_pedi(price_pedi);
                    shopAccount.setMemo(memocontent);

                    //Todo: 추후 삽입
                    shopAccount.setImg_shop(R.drawable.edge);
                    shopAccount.setImg_scrab(R.drawable.ic_baseline_bookmark_white);
                    shopAccount.setRating("0");
                    shopAccount.setRatingcnt("0");
//                    shopAccount.setPrice_nail("0");
//                    shopAccount.setPrice_pedi("0");

                    final ProgressDialog mDialog = new ProgressDialog(StoreInfoActivity.this);

                    memberViewModel.storeInfo(shopAccount);
                    mDialog.setMessage("등록중입니다");
                    mDialog.show();

                    mDialog.dismiss();
                    Intent startMain = new Intent(StoreInfoActivity.this, MainActivity.class);
                    startActivity(startMain);
                    finish();
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
        edt_opentime = findViewById(R.id.edt_opentime);
        edt_shopphonenum = findViewById(R.id.edt_shopphonenum);
        edt_address = findViewById(R.id.edt_address);
        edt_price_nail = findViewById(R.id.edt_price_nail);
        edt_price_pedi = findViewById(R.id.edt_price_pedi);
        edt_memocontent = findViewById(R.id.edt_memocontent);
        edt_closed = findViewById(R.id.edt_closed);
        btn_next = findViewById(R.id.btn_next);

    }
}