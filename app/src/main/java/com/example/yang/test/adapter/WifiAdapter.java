package com.example.yang.test.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yang.test.R;
import com.example.yang.test.minterface.ItemClickListener;

import java.util.List;

import static android.R.id.list;

/**
 * Created by Administrator on 2016/12/17.
 */

public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.ViewHolder> {

    private  Context mContext;

    private List<ScanResult> mList;
    private ItemClickListener itemClickListener;

    static class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout root;
        ImageView imageView;
         TextView textView;
         TextView signal_strenth;

        public ViewHolder(View itemView) {
            super(itemView);
            root = (RelativeLayout) itemView.findViewById(R.id.root);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            signal_strenth = (TextView) itemView.findViewById(R.id.signal_strenth);

        }
    }

    public WifiAdapter(List<ScanResult> list, ItemClickListener listener) {
        this.mList = list;
        this.itemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext==null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_wifi,parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(viewHolder.getAdapterPosition());
            }
        });
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ScanResult scanResult = mList.get(position);
        holder.textView.setText(scanResult.SSID);
        holder.signal_strenth.setText(String.valueOf(Math.abs(scanResult.level)));
        if (Math.abs(scanResult.level) > 100) {
            holder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.wifi04));
        } else if (Math.abs(scanResult.level) > 80) {
            holder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.wifi03));
        } else if (Math.abs(scanResult.level) > 60) {
            holder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.wifi02));
        } else if (Math.abs(scanResult.level) > 40) {
            holder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.wifi01));
        } else {
            holder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.wifi00));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
