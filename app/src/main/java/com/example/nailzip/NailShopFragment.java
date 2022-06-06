package com.example.nailzip;

import android.os.Bundle;

import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NailShopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NailShopFragment extends Fragment {

    private ArrayList<NailshopData> arrayShops = new ArrayList<>();
    private NailshopAdapter nailshopAdapter = new NailshopAdapter(arrayShops);
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private LinearLayoutManager linearLayoutManager;
    private TextView tv_region;
    private ImageFilterButton btn_region;
    private Button btn_sort;

    FragmentManager fragmentManager;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NailShopFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NailShopFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NailShopFragment newInstance(String param1, String param2) {
        NailShopFragment fragment = new NailShopFragment();
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
        View view = inflater.inflate(R.layout.fragment_nail_shop, container, false);

        init(view);
        fragmentManager = getActivity().getSupportFragmentManager();
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(nailshopAdapter);

        //데이터 추가방법
        NailshopData nailshopData = new NailshopData(R.drawable.edge, R.drawable.ic_baseline_bookmark_white, "만두네일", "5.0", "(112)","11:00~21:00", "(휴무:일)", "경기도 용인시 수지구 현암로 123번길 33" );
        arrayShops.add(nailshopData);
        nailshopAdapter.notifyDataSetChanged();

        return view;
    }

    public void init(View view){
        tv_region = view.findViewById(R.id.tv_region);
        btn_region = view.findViewById(R.id.btn_region);
        btn_sort = view.findViewById(R.id.btn_sort);
        recyclerView = view.findViewById(R.id.recyclerview);
    }
}