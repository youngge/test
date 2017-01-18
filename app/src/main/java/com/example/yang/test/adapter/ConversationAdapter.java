package com.example.yang.test.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yang.test.R;
import com.example.yang.test.bean.ConversationBean;
import com.example.yang.test.minterface.ItemClickListener;

import java.util.List;


/**
 * Created by Administrator on 2016/12/17.
 */

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder> {

    private Context mContext;

    private List<ConversationBean> mList;
    private ItemClickListener longClickListener;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_left;
        TextView tv_right;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_left = (TextView) itemView.findViewById(R.id.tv_left);
            tv_right = (TextView) itemView.findViewById(R.id.tv_right);

        }
    }

    public ConversationAdapter(List<ConversationBean> list, ItemClickListener listener) {
        this.mList = list;
        this.longClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_conversation, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.tv_left.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longClickListener.onLongClick(viewHolder.getAdapterPosition());
                return true;
            }
        });
        viewHolder.tv_right.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longClickListener.onLongClick(viewHolder.getAdapterPosition());
                return true;
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ConversationBean conversationBean = mList.get(position);
        if (conversationBean.getType() == 0) {
            holder.tv_right.setVisibility(View.GONE);
            holder.tv_left.setVisibility(View.VISIBLE);
            holder.tv_left.setText(mList.get(position).getContent());
        } else {
            holder.tv_left.setVisibility(View.GONE);
            holder.tv_right.setVisibility(View.VISIBLE);
            holder.tv_right.setText(mList.get(position).getContent());
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
