package com.example.nailzip;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.nailzip.mypage.PagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class ShopInfoActivity extends AppCompatActivity {

    InfoHomeFragment infoHomeFragment;
    InfoMenuFragment infoMenuFragment;
    InfoDesignFragment infoDesignFragment;
    InfoReviewFragment infoReviewFragment;
    private TextView txt_title;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

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
    }

    public void init(){
        txt_title = findViewById(R.id.txt_title);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);

    }
}