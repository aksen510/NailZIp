package com.example.nailzip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NailshopAdapter extends RecyclerView.Adapter<NailshopAdapter.CustomViewHolder> {
    private ArrayList<NailshopData> arrayList;

    public NailshopAdapter(ArrayList<NailshopData> arrayList) {
        this.arrayList = arrayList;
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
        holder.img_scrab.setImageResource(arrayList.get(position).getImg_scrab());
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
                // Todo: 해당 게시글로 이동
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
            img_scrab = itemView.findViewById(R.id.img_scrab);
            tv_shopname = itemView.findViewById(R.id.tv_shopname);
            tv_rating = itemView.findViewById(R.id.tv_rating);
            tv_ratingcnt = itemView.findViewById(R.id.tv_ratingcnt);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_closed = itemView.findViewById(R.id.tv_closed);
            tv_location = itemView.findViewById(R.id.tv_location);


        }
    }
}
