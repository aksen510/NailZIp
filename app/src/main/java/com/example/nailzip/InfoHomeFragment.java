package com.example.nailzip;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.nailzip.model.Chat;
import com.example.nailzip.model.NailshopData;
import com.example.nailzip.mypage.PagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoHomeFragment extends Fragment {

    private String TAG = "InfoHomeFragment";

    private TabLayout tablayout;
    private ViewPager viewpager;
    private PagerAdapter pagerAdapter;
    private TextView txt_title, tv_shopname, tv_opentimeInfo, tv_closedInfo, tv_memoInfo;
    private Button btn_scrap, btn_location, btn_call, btn_reservation, btn_share;
    private LinearLayout linear_scrap;
    private static String shopName;
    private static String shopLocation;
    private static String chatUid;
    private static int shopPos;
    private String follow_shop_id;
    private String follow_shop_name;

    private String tel, shopPhone;

    private Chat chat = new Chat();
    private List<Chat> chats = new ArrayList<>();
    private NailshopData follow_nailshop = new NailshopData();
    private List<NailshopData> nailshopDataList = new ArrayList<>();
    private static String shopUid = "";

    //Todo: 이미지 추가

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

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

        final String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Log.d(TAG, "mainShopInfo : " + shopPos + " / " + shopName + " / " + shopLocation);

        firestore.collection("shoplist")
                .whereEqualTo("shopname", shopName)
                .whereEqualTo("location", shopLocation)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                follow_shop_id = document.getId();
                                isFollowing(follow_shop_id, btn_scrap);
                                follow_shop_name = document.get("shopname").toString();
//                                isFollowing(follow_shop_name,btn_scrab);

                                Log.d(TAG, document.getId() + " => " + document.getData());
//                                txt_title.setText(document.get("shopname").toString());
                                tv_shopname.setText(document.get("shopname").toString());
                                tv_opentimeInfo.setText(document.get("time").toString());
                                tv_closedInfo.setText(document.get("closed").toString());
                                tv_memoInfo.setText(document.get("memo").toString());

                                shopPhone = document.get("shopphone").toString();

                            }
                        }
                        else{
                            Log.d(TAG, "일치하는 매장정보가 없습니다.");
                            Toast.makeText(getActivity(), "일치하는 매장정보가 없습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "일치하는 매장정보가 없습니다.");
                        Toast.makeText(getActivity(), "일치하는 매장정보가 없습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });

        // InfoShopfragment에 적용
//            FirebaseDatabase.getInstance().getReference().child("chatUsers").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    chats.clear();
//                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//
//                        Chat chat = snapshot.getValue(Chat.class);
//
//                        if (chat.chatUid.equals(myUid)){
//                            continue;
//                        }
//
//                        chats.add(chat);
//                    }
//                    notifyDataSetChanged();
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });

        firebaseDatabase.getReference().child("chatUsers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chats.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Chat chat = dataSnapshot.getValue(Chat.class);

                    if (myUid.equals(chat.chatUid)){
                        continue;
                    }

                    chats.add(chat);

                }

                Log.d(TAG, "chats 리스트 : " + chats);

                for(int i=0; i<chats.size(); i++){
                    Log.d(TAG, "chats 리스트 중 chatUid : " + chats.get(i).getChatUid());

                    int savePos = chats.get(i).getPosition();
                    String saveName = String.valueOf(chats.get(i).getUserName());
                    Log.d(TAG, "savePos & saveName : " + savePos + " / " + saveName);
                    Log.d(TAG, "shopPos & shopName : " + shopPos + " / " + shopName);

                    if(saveName.equals(shopName)){
                        shopUid = chats.get(i).getChatUid();
                        Log.d(TAG, "shopUid : " + shopUid);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Error : 리스트 저장 실패");
            }
        });

        // 예약 - 채팅 버튼
        btn_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chattingroomActivity = new Intent(getContext(), ChattingroomActivity.class);
                chattingroomActivity.putExtra("chatUid", shopUid);
                Log.d(TAG, "chatUid2 : " + shopUid);
                startActivity(chattingroomActivity);
            }
        });

        // 전화걸기 버튼
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tel = "tel:" + shopPhone;

                startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
            }
        });

        // 위치 버튼
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent locationActivity = new Intent(getContext(), LocationActivity.class);
                locationActivity.putExtra("shopName", shopName);
                locationActivity.putExtra("shopLocation", shopLocation);
                Log.d(TAG, "shopLocation : " + shopLocation);
                startActivity(locationActivity);
            }
        });

        // 팔로우 버튼
        // TODO : 사용자가 shop일 경우, 스크랩 버튼 없애는 기능 추가

        FirebaseDatabase.getInstance().getReference().child("Follow").child(myUid).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()){
//                    saveFollowingUid.add(item.getValue().toString());
                    String saveFollowingUid = item.getValue().toString();
                    Log.d(TAG, "네일샵 이름 : " + saveFollowingUid);

                    if (shopName.equals(saveFollowingUid)){
                        btn_scrap.setBackgroundResource(R.drawable.bookmark_fill);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "error");

            }
        });


        btn_scrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn_scrap.getText().toString().equals("follow")){
                    firebaseDatabase.getReference().child("Follow").child(firebaseAuth.getUid())
                            .child("following").child(follow_shop_id).setValue(follow_shop_name);
                    firebaseDatabase.getReference().child("Follow").child(follow_shop_id)
                            .child("followers").child(firebaseAuth.getUid()).setValue(true);
                    btn_scrap.setBackgroundResource(R.drawable.bookmark_fill);
                }
                else{
                    firebaseDatabase.getReference().child("Follow").child(firebaseAuth.getUid())
                            .child("following").child(follow_shop_id).removeValue();
                    firebaseDatabase.getReference().child("Follow").child(follow_shop_id)
                            .child("followers").child(firebaseAuth.getUid()).removeValue();
                    btn_scrap.setBackgroundResource(R.drawable.bookmark_border);
                }
            }
        });

        return view;
    }

    private void isFollowing (final String userid, final Button button){
        DatabaseReference reference = firebaseDatabase.getReference()
                .child("Follow").child(firebaseAuth.getCurrentUser().getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(userid).exists()){
                    button.setText("following");
                }
                else{
                    button.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setShopInfo(int pos, String shopname, String location, String chatUid){
        shopPos = pos;
        shopName = shopname;
        shopLocation = location;
        chatUid = chatUid;

        Log.d(TAG, "setShopInfo : " + shopPos + " / " + shopName + " / " + shopLocation + " / " + chatUid);

    }

    public void init(View view){
//        txt_title = (TextView) view.findViewById(R.id.txt_title);
        tv_shopname = (TextView) view.findViewById(R.id.tv_shopname);
        tv_opentimeInfo = (TextView) view.findViewById(R.id.tv_opentimeInfo);
        tv_closedInfo = (TextView) view.findViewById(R.id.tv_closedInfo);
        tv_memoInfo = (TextView) view.findViewById(R.id.tv_memoInfo);
        btn_scrap = (Button) view.findViewById(R.id.btn_scrap);
        btn_location = (Button) view.findViewById(R.id.btn_location);
        btn_call = (Button) view.findViewById(R.id.btn_call);
        btn_reservation = (Button) view.findViewById(R.id.btn_reservation);
//        btn_share = (Button) view.findViewById(R.id.btn_share);
        linear_scrap = view.findViewById(R.id.linear_scrap);

    }
}