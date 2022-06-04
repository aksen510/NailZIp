package com.example.nailzip.mypage;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nailzip.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {

    private static final String TAG = "SettingFragment";
    private ListView lv_mypage;
    private ArrayList<Setting> settings;
    private TextView tv_nickname;
    private static CustomAdapter customAdapter;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public String nickname;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
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
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        init(view);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = user.getEmail();

        firestore.collection("users")
                .whereEqualTo("email",uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Log.d(TAG, document.getId() + " => " + document.get("username"));
                                nickname = document.get("username").toString();
//                                tv_nickname.setText(nickname);
                            }
                            if (nickname == null){
                                Log.d(TAG, "일치하는 회원정보가 없습니다.");
                                Toast.makeText(getContext(), "일치하는 회원정보가 없습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else {
                                tv_nickname.setText(nickname);
                            }
                        }
                        else{
                            Log.d(TAG, "일치하는 회원정보가 없습니다.");
                            Toast.makeText(getContext(), "일치하는 회원정보가 없습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "일치하는 회원정보가 없습니다.");
                        Toast.makeText(getContext(), "일치하는 회원정보가 없습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });

//        Intent intent = getActivity().getIntent();
//        Log.d(TAG,"닉네임 => " + nickname);
//        tv_nickname.setText(nickname);

        // TODO: 회원정보에 따라 다른 리스트뷰 생성
        final String[] lv1 = {"설정", "찜 목록", "팔로우", "나의 후기"};
        final String[] lv2 = {"설정", "찜 목록", "팔로우", "매장 정보 수정"};
        final int image[] = {R.drawable.ic_outline_settings_24, R.drawable.ic_heart_regular,R.drawable.ic_bookmark_regular, R.drawable.ic_pen_to_square_regular};

        settings = new ArrayList<>();
        settings.add(new Setting("설정", R.drawable.ic_outline_settings_24));
        settings.add(new Setting("찜 목록", R.drawable.ic_heart_regular));
        settings.add(new Setting("팔로우", R.drawable.ic_baseline_bookmark_border_24));
        settings.add(new Setting("나의 후기", R.drawable.ic_pen_to_square_regular));

        lv_mypage = (ListView) view.findViewById(R.id.lv_mypage);
        customAdapter = new CustomAdapter(getContext(), settings);
        lv_mypage.setAdapter(customAdapter);

        lv_mypage.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = (String) view.findViewById(R.id.tv_list).getTag().toString();
               // Toast.makeText(getActivity(), "item 클릭", Toast.LENGTH_SHORT).show();

                switch (position){
                    case 0:
                        Toast.makeText(getActivity(), "설정", Toast.LENGTH_SHORT).show();
                        Intent startChangepwActivity = new Intent(getContext(), SettingEditInfoActivity.class);
                        startActivity(startChangepwActivity);
                        break;
                    case 1:
                        Toast.makeText(getActivity(), "찜목록", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getActivity(), "팔로우", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(getActivity(), "나의 후기", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        return view;
    }

    public void init(View view){
        lv_mypage = view.findViewById(R.id.lv_mypage);
        tv_nickname = view.findViewById(R.id.tv_nickname);
    }

    class Setting{
        private String menuname;
        private int image;

        public Setting(String menuname, int image){
            this.menuname = menuname;
            this.image = image;
        }

        public String getMenuname(){
            return menuname;
        }

        public int getImage(){
            return image;
        }
    }
}