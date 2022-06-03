package com.example.nailzip.mypage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.nailzip.R;
import com.example.nailzip.mypage.SettingFragment;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter implements AdapterView.OnItemClickListener {

    private Context context;
    private List list;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
    }

    class ViewHolder{
        public TextView tv_menuname;
        public ImageView img_menu;
    }

    public CustomAdapter(Context context, ArrayList list){
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final ViewHolder viewHolder;

        if(convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            convertView = layoutInflater.inflate(R.layout.item_mypage, parent, false);
        }

        viewHolder = new ViewHolder();
        viewHolder.tv_menuname = (TextView) convertView.findViewById(R.id.tv_list);
        viewHolder.img_menu = (ImageView) convertView.findViewById(R.id.img_menu);

        final SettingFragment.Setting setting = (SettingFragment.Setting) list.get(position);
        viewHolder.tv_menuname.setText(setting.getMenuname());

        Glide.with(context)
                .load(setting.getImage())
                .centerCrop()
                .apply(new RequestOptions().override(512,512))
                .into(viewHolder.img_menu);
        viewHolder.tv_menuname.setTag(setting.getMenuname());

        return convertView;
    }

}
