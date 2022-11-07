package com.example.nailzip;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nailzip.model.NailshopData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NailShopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NailShopFragment extends Fragment {

    private String TAG = "NailShopFragment";
    private ArrayList<NailshopData> arrayShops = new ArrayList<>();
//    private InfoHomeFragment infoHomeFragment = new InfoHomeFragment();
    private NailshopAdapter nailshopAdapter = new NailshopAdapter(arrayShops, getContext());
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private LinearLayoutManager linearLayoutManager;
    private TextView tv_region;
    private ImageFilterButton btn_region;
    private Button btn_sort;
    private SearchView searchView;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

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

        //TODO: 네일샵 정렬 기능 추가
//        btn_sort.setVisibility(View.GONE);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = user.getUid();

        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(nailshopAdapter);

        //데이터 추가방법
//        NailshopData nailshopData = new NailshopData(R.drawable.edge, R.drawable.ic_baseline_bookmark_white, "만두네일", null,"5.0", "(112)","11:00~21:00", "(휴무:일)", "경기도 용인시 수지구 현암로 123번길 33" ,null, null, null);
//        arrayShops.add(nailshopData);
//        nailshopAdapter.notifyDataSetChanged();

        firestore.collection("shoplist")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error == null){
                            if(value.isEmpty()){
                                Toast.makeText(getActivity().getApplicationContext(), "등록된 네일샵이 없습니다.", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "등록된 네일샵 없음");
                            }
                            else{
                                for(DocumentChange dc : value.getDocumentChanges()){
                                    NailshopData nailshop = dc.getDocument().toObject(NailshopData.class);
                                    arrayShops.add(nailshop);
                                    nailshopAdapter.notifyDataSetChanged();
                                    Log.d(TAG, "현재 arrayShops 사이즈 : " + arrayShops.size());

                                }
                            }
                        }
                        else {
                            Log.d(TAG, "error == null : " + error.getMessage());
                        }
                    }
                });

//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                nailshopAdapter.getFilter().filter(newText);
//                Log.d(TAG, "검색 test : " + newText);
//
//                return false;
//            }
//        });

        return view;
    }

    public void init(View view){
//        tv_region = view.findViewById(R.id.tv_region);
//        btn_region = view.findViewById(R.id.btn_region);
//        btn_sort = view.findViewById(R.id.btn_sort);
        recyclerView = view.findViewById(R.id.recyclerview);
//        searchView = view.findViewById(R.id.searchview);

    }
}