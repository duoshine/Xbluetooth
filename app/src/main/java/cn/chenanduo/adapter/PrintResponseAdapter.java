package cn.chenanduo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.chenanduo.R;

/**
 * Created by chen on 2018
 */

public class PrintResponseAdapter extends RecyclerView.Adapter<PrintResponseAdapter.MyViewHolder> {
    private List<String> mDatas;

    public PrintResponseAdapter(List<String> datas) {
        mDatas = datas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_printrespones_item_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_result.setText("返回" + mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_result;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_result = itemView.findViewById(R.id.tv_result);
        }
    }
}
