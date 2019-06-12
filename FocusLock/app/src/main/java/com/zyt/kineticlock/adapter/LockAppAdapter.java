package com.zyt.kineticlock.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zyt.kineticlock.R;
import com.zyt.kineticlock.bean.AppInfo;

import java.util.List;

public class LockAppAdapter extends RecyclerView.Adapter<LockAppAdapter.ViewHolder> {



    List<AppInfo> mAppInfoList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_WhiteIcon;
        private TextView tv_appName;


        public ViewHolder(@NonNull View view) {
            super(view);

            iv_WhiteIcon=view.findViewById(R.id.iv_WhiteIcon);
            tv_appName=view.findViewById(R.id.tv_appName);

        }
    }

    public LockAppAdapter(List<AppInfo> appInfos){
        mAppInfoList =appInfos;
    }


    public interface ItemOnClickListener{
        void OnItemClick(View view, int position);
    }

    private ItemOnClickListener itemOnClickListener;

    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_whiteapp,viewGroup,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final AppInfo appInfo= mAppInfoList.get(position);

        holder.iv_WhiteIcon.setBackground(appInfo.getAppIcon());
        holder.tv_appName.setText(appInfo.getAppName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemOnClickListener!=null){
                    itemOnClickListener.OnItemClick(holder.itemView,position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mAppInfoList.size();
    }


}
