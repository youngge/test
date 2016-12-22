package com.example.yang.test.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yang.test.R;
import com.example.yang.test.activity.GameActivity;
import com.example.yang.test.activity.MusicActivity;
import com.example.yang.test.activity.NotificationActivity;
import com.example.yang.test.activity.RussianSquareActivity;
import com.example.yang.test.activity.ZhimafenActivity;

import java.util.List;

/**
 * Created by Administrator on 2016/12/17.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private  Context mContext;

    private List<String> mList;

    static class ViewHolder extends RecyclerView.ViewHolder{

         CardView cardview;
         TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            cardview = (CardView)itemView.findViewById(R.id.cardview);
            tv = (TextView) itemView.findViewById(R.id.tv);

        }
    }

    public HomeAdapter(List<String> list) {
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext==null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_home,parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                switch (viewHolder.getAdapterPosition()){
                    case 0:
                        intent.setClass(mContext,ZhimafenActivity.class);
                        mContext.startActivity(intent);
                        break;
                    case 1:
                        intent.setClass(mContext,MusicActivity.class);
                        mContext.startActivity(intent);
                        break;
                    case 2:
                        intent.setClass(mContext,NotificationActivity.class);
                        mContext.startActivity(intent);
                        break;
                    case 3:
                        intent.setClass(mContext,GameActivity.class);
                        mContext.startActivity(intent);
                        break;
                    case 4:
                        intent.setClass(mContext,RussianSquareActivity.class);
                        mContext.startActivity(intent);
                        break;
                }

            }
        });
        viewHolder.cardview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = viewHolder.getAdapterPosition();
                mList.add(position,"--添加项--");
                notifyItemInserted(position);
                return true;
            }
        });
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
