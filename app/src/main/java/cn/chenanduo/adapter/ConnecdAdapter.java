package cn.chenanduo.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.chenanduo.R;


/**
 * Created by chen on 2017
 */

public class ConnecdAdapter extends RecyclerView.Adapter<ConnecdAdapter.MyViewHolder> {
    private List<String> list;

    public ConnecdAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.connecd_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        String text = list.get(position);
        //实现无焦点滚动
        holder.tv_uuid.setSelected(true);
        if (!text.startsWith("service")) {
            holder.tv_uuid.setText("   " + text);
            holder.tv_uuid.setTextColor(Color.BLUE);
        } else {
            holder.tv_uuid.setTextColor(Color.RED);
            holder.tv_uuid.setText(text);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClick != null)
                    mItemClick.OnItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_uuid;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_uuid = itemView.findViewById(R.id.tv_uuid);
        }
    }

    public interface itemClick {
        void OnItemClick(int position);
    }

    private itemClick mItemClick;

    public void onItemClick(itemClick itemClick) {
        this.mItemClick = itemClick;
    }
}
