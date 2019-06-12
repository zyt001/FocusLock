package com.zyt.kineticlock.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.zyt.kineticlock.R;
import com.zyt.kineticlock.bean.AppInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WhiteAppAdapter extends RecyclerView.Adapter<WhiteAppAdapter.ViewHolder>  {

    private Context mContext;
    private List<AppInfo> mAppInfo;

    public Map<Integer,Boolean> isSelectMap =new HashMap<>();



    public WhiteAppAdapter(Context context, List<AppInfo> infoList){
        mAppInfo =infoList;

        this.mContext=context;

    }




    public interface ItemOnCheckListener{
        void OnCheck(View view, int position, boolean isChecked);
    }

    private ItemOnCheckListener itemOnCheckListener;

    public void setItemOnCheckListener(ItemOnCheckListener itemOnCheckListener) {
        this.itemOnCheckListener = itemOnCheckListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv_icon;
        private TextView tv_appName,tv_packageName;
        private Switch btn_switch;



        public ViewHolder(@NonNull View view) {
            super(view);
            iv_icon=view.findViewById(R.id.iv_appIcon);
            tv_appName=view.findViewById(R.id.tv_appName);
            tv_packageName=view.findViewById(R.id.tv_packageName);
            btn_switch=view.findViewById(R.id.btn_Swtich);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_apps,viewGroup,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final AppInfo appInfo=mAppInfo.get(position);

        holder.tv_appName.setText(appInfo.getAppName());
        holder.tv_packageName.setText(appInfo.getPackageName());

        holder.iv_icon.setBackground(appInfo.getAppIcon());
        holder.btn_switch.setChecked(appInfo.isSelect());
        holder.btn_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               itemOnCheckListener.OnCheck(holder.itemView,position,isChecked);
            }
        });

        if(isSelectMap !=null&& isSelectMap.containsKey(position)){
            holder.btn_switch.setChecked(false);
        }else {
            holder.btn_switch.setChecked(true);
        }


    }



    @Override
    public int getItemCount() {
        return mAppInfo.size();
    }


}
