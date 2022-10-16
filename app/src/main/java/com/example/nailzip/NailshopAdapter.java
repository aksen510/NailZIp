package com.example.nailzip;

import android.content.Context;
import android.content.Intent;
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
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.nailzip.model.NailshopData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class NailshopAdapter extends RecyclerView.Adapter<NailshopAdapter.CustomViewHolder> {
    private ArrayList<NailshopData> arrayList;
    private String TAG = "NailshopAdapter";
    private Context mContext;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentStateAdapter fragmentStateAdapter;
    private InfoHomeFragment infoHomeFragment = new InfoHomeFragment();
    private InfoMenuFragment infoMenuFragment = new InfoMenuFragment();
    private ShopInfoActivity shopInfoActivity = new ShopInfoActivity();
    private ChattingroomActivity chattingroomActivity = new ChattingroomActivity();
    private String location = " ";
    private String shopname = " ";
    private String chatUid = " ";
    private int pos = 0;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public NailshopAdapter(ArrayList<NailshopData> arrayList, Context mContext) {
        this.arrayList = arrayList;
        this.mContext = mContext;
//        this.infoHomeFragment = infoHomeFragment;
    }

    @NonNull
    @Override
    public NailshopAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.item_shop, parent, false);
        NailshopAdapter.CustomViewHolder holder = new NailshopAdapter.CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NailshopAdapter.CustomViewHolder holder, int position) {
        holder.linear_bg.setBackgroundResource(arrayList.get(position).getImg_shop());
//        holder.img_scrab.setImageResource(arrayList.get(position).getImg_scrab());
        holder.tv_shopname.setText(arrayList.get(position).getShopname());
        holder.tv_rating.setText(arrayList.get(position).getRating());
        holder.tv_ratingcnt.setText(arrayList.get(position).getRatingcnt());
        holder.tv_time.setText(arrayList.get(position).getTime());
        holder.tv_closed.setText(arrayList.get(position).getClosed());
        holder.tv_location.setText(arrayList.get(position).getLocation());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos = holder.getLayoutPosition();
                shopname = arrayList.get(pos).getShopname();
                location = arrayList.get(pos).getLocation();
                chatUid = arrayList.get(pos).getUid();
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
        return (null != arrayList ? arrayList.size() : 0);
    }

    public void remove(int position){
        try{
            arrayList.remove(position);
            notifyItemRemoved(position);
        }catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

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
