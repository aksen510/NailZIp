package com.example.nailzip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.nailzip.mypage.PagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoHomeFragment extends Fragment {

    private TabLayout tablayout;
    private ViewPager viewpager;
    private PagerAdapter pagerAdapter;
    private TextView txt_title, tv_shopname, tv_opentimeInfo, tv_closedInfo, tv_memoInfo;
    private Button btn_scrab, btn_location, btn_call, btn_reservation, btn_share;

    //Todo: 이미지 추가

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InfoHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoHomeFragment newInstance(String param1, String param2) {
        InfoHomeFragment fragment = new InfoHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info_home, container, false);

        init(view);



        return view;
    }

    public void init(View view){
        txt_title = view.findViewById(R.id.txt_title);
        tv_shopname = view.findViewById(R.id.tv_shopname);
        tv_opentimeInfo = view.findViewById(R.id.tv_opentimeInfo);
        tv_closedInfo = view.findViewById(R.id.tv_closedInfo);
        tv_memoInfo = view.findViewById(R.id.tv_memoInfo);
        btn_scrab = (Button) view.findViewById(R.id.btn_scrab);
        btn_location = (Button) view.findViewById(R.id.btn_location);
        btn_call = (Button) view.findViewById(R.id.btn_call);
        btn_reservation = (Button) view.findViewById(R.id.btn_reservation);
        btn_share = (Button) view.findViewById(R.id.btn_share);

    }
}