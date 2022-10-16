package com.example.nailzip;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nailzip.model.NailshopData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FollowingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FollowingFragment extends Fragment {
    private String TAG = "FollowingFragment";

    private FirebaseRemoteConfig mfirebaseRemoteConfig;
    private InfoHomeFragment infoHomeFragment = new InfoHomeFragment();
    private InfoMenuFragment infoMenuFragment = new InfoMenuFragment();
    private ShopInfoActivity shopInfoActivity = new ShopInfoActivity();
    private ChattingroomActivity chattingroomActivity = new ChattingroomActivity();
    private String location = " ";
    private String shopname = " ";
    private String chatUid = " ";
    private int pos = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FollowingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FollowingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FollowingFragment newInstance(String param1, String param2) {
        FollowingFragment fragment = new FollowingFragment();
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
        View view = inflater.inflate(R.layout.fragment_following, container, false);

        init(view);

        RecyclerView recyclerView = view.findViewById(R.id.chat_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new FollowingFragmentRecyclerViewAdapter());

        mfirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();


        return view;
    }

    class FollowingFragmentRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<NailshopData> nailshopDataList = new ArrayList<>();
        String myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        private ArrayList<String> followingList = new ArrayList<>();

        public FollowingFragmentRecyclerViewAdapter(){

            FirebaseDatabase.getInstance().getReference().child("Follow").orderByChild(myUid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    nailshopDataList.clear();
                    for (DataSnapshot item : snapshot.getChildren()){
                        nailshopDataList.add(item.getValue(NailshopData.class));
                    }
                    notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop, parent, false);

            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            CustomViewHolder customViewHolder = (CustomViewHolder) holder;

            customViewHolder.linear_bg.setBackgroundResource(nailshopDataList.get(position).getImg_shop());
            customViewHolder.img_scrab.setImageResource(nailshopDataList.get(position).getImg_scrab());
            customViewHolder.tv_shopname.setText(nailshopDataList.get(position).getShopname());
            customViewHolder.tv_rating.setText(nailshopDataList.get(position).getRating());
            customViewHolder.tv_ratingcnt.setText(nailshopDataList.get(position).getRatingcnt());
            customViewHolder.tv_time.setText(nailshopDataList.get(position).getTime());
            customViewHolder.tv_closed.setText(nailshopDataList.get(position).getClosed());
            customViewHolder.tv_location.setText(nailshopDataList.get(position).getLocation());

            customViewHolder.itemView.setTag(position);
            customViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos = holder.getLayoutPosition();
                    shopname = nailshopDataList.get(pos).getShopname();
                    location = nailshopDataList.get(pos).getLocation();
                    chatUid = nailshopDataList.get(pos).getUid();
                    Log.d(TAG, "position and id: " + pos + " / " + shopname + " / " + location);

                    if (shopname.isEmpty() == false){
                        Log.d(TAG, "position2 and id2: " + pos + " / " + shopname + " / " + location);
                        infoHomeFragment.setShopInfo(pos, shopname, location, chatUid);
                        shopInfoActivity.setShopInfo(pos, shopname, location, chatUid);
                        chattingroomActivity.setShopInfo(pos, shopname, location, chatUid);
                        infoMenuFragment.setShopInfo(pos, shopname, location, chatUid);

                        Intent startInformation = new Intent(v.getContext(), ShopInfoActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        v.getContext().startActivity(startInformation);

                        Toast.makeText(v.getContext(), "네일 상세 정보 확인", Toast.LENGTH_SHORT).show();
                    }



                }
            });
        }

        @Override
        public int getItemCount() {
            return nailshopDataList.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {
            private LinearLayout linear_bg;
            private ImageView img_scrab;
            private TextView tv_shopname, tv_rating, tv_ratingcnt, tv_time, tv_closed, tv_location;


            public CustomViewHolder(@NonNull View itemView) {
                super(itemView);

                linear_bg = itemView.findViewById(R.id.linear_bg);
//            img_scrab = itemView.findViewById(R.id.img_scrab);
                tv_shopname = itemView.findViewById(R.id.tv_shopname);
                tv_rating = itemView.findViewById(R.id.tv_rating);
                tv_ratingcnt = itemView.findViewById(R.id.tv_ratingcnt);
                tv_time = itemView.findViewById(R.id.tv_time);
                tv_closed = itemView.findViewById(R.id.tv_closed);
                tv_location = itemView.findViewById(R.id.tv_location);

            }
        }
    }

    public void init(View view) {

    }

}