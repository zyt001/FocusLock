package com.zyt.kineticlock.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zyt.kineticlock.R;
import com.zyt.kineticlock.bean.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task>mTask;

    static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_name;
        private TextView tv_lockTime;
        private TextView tv_lockMode;

        public ViewHolder(@NonNull View view) {
            super(view);
            tv_name=view.findViewById(R.id.tv_name);
            tv_lockTime=view.findViewById(R.id.tv_lockTime);
            tv_lockMode=view.findViewById(R.id.tv_lockMode);

        }
    }
    public TaskAdapter(List<Task>taskList){
        mTask=taskList;
    }

    public interface ItemOnClickListener{

        void OnItemClick(View view, int position);

        void OnItemLongClick(View view, int position);
    }

    private ItemOnClickListener itemOnClickListener;

    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tasks,viewGroup,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Task task=mTask.get(position);
        holder.tv_name.setText(task.getTitle());
        holder.tv_lockTime.setText(String.valueOf(task.getLockTime())+"分钟");
        holder.tv_lockMode.setText(task.getTaskMode());

        if(itemOnClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemOnClickListener.OnItemClick(holder.itemView,position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    itemOnClickListener.OnItemLongClick(holder.itemView,position);
                    return false;
                }
            });

        }
    }

    public void removeData(int position){
        mTask.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mTask.size();
    }
}
