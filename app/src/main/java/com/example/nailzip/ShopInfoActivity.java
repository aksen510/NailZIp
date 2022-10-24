package com.example.nailzip;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.nailzip.mypage.PagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class ShopInfoActivity extends AppCompatActivity {

    private String TAG = "ShopInfoActivity";

    InfoHomeFragment infoHomeFragment;
    InfoMenuFragment infoMenuFragment;
    InfoDesignFragment infoDesignFragment;
    InfoReviewFragment infoReviewFragment;
    private TextView txt_title;
    private Toolbar tb_back;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private static String shopName, shopLocation, chatUid;
    private static int shopPos;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_info);

        init();

        infoHomeFragment = new InfoHomeFragment();
        infoMenuFragment = new InfoMenuFragment();
        infoDesignFragment = new InfoDesignFragment();
        infoReviewFragment = new InfoReviewFragment();

        tabLayout.setupWithViewPager(viewPager);

        getSupportFragmentManager().beginTransaction().replace(R.id.viewpager, infoHomeFragment).commit();

        pagerAdapter = new PagerAdapter(this.getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pagerAdapter.addFragment(new InfoHomeFragment(), "HOME");
        pagerAdapter.addFragment(new InfoMenuFragment(), "MENU");
        pagerAdapter.addFragment(new InfoDesignFragment(), "DESIGN");
        pagerAdapter.addFragment(new InfoReviewFragment(), "REVIEW");
        viewPager.setAdapter(pagerAdapter);

        firestore.collection("shoplist")
                .whereEqualTo("shopname", shopName)
                .whereEqualTo("location", shopLocation)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                txt_title.setText(document.get("shopname").toString());

                                Log.d(TAG,"Data : " + document.get("shopname") + " / " + document.get("time") + " / " + document.get("closed") + " / " + document.get("memo"));
                            }
                        }
                        else{
                            Log.d(TAG, "일치하는 매장정보가 없습니다.");
                            Toast.makeText(ShopInfoActivity.this, "일치하는 매장정보가 없습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "일치하는 매장정보가 없습니다.");
                        Toast.makeText(ShopInfoActivity.this, "일치하는 매장정보가 없습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });

        // Toolbar 활성화
        setSupportActionBar(tb_back);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 생성
        getSupportActionBar().setTitle(null); // Toolbar 제목 제거

    }

    public void setShopInfo(int pos, String shopname, String location, String chatUid){
        shopPos = pos;
        shopName = shopname;
        shopLocation = location;
        chatUid = chatUid;

        Log.d(TAG, "setShopInfo : " + shopPos + " / " + shopName + " / " + shopLocation + " / " + chatUid);

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
        txt_title = findViewById(R.id.txt_title);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        tb_back = findViewById(R.id.tb_back);
    }
}