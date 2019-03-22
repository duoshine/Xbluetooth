package cn.chenanduo.fragment;

import android.bluetooth.BluetoothDevice;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.chenanduo.BaseFragment;
import cn.chenanduo.MainActivity;
import cn.chenanduo.R;
import cn.chenanduo.adapter.ShowNotBleAdapter;
import cn.chenanduo.adapter.ShowNotBleInterface;
import cn.chenanduo.util.Logger;


/**
 * Created by chen on 2017  显示所有未连接的蓝牙设备
 */

public class ShowBleFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<BluetoothDevice> devicesNew = new ArrayList<>();
    private Handler mHandler = new Handler();
    private ShowNotBleAdapter mMShowNotBleAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shownotble, null);
        initView(view);
        Logger.d("扫描界面被创建了onCreateView");
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d("扫描界面被创建了onCreate");

    }

    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerview);
        swipeRefreshLayout = view.findViewById(R.id.SwipeRefreshLayout);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);
        //下拉刷新的监听事件
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                devicesNew.clear();
                mMShowNotBleAdapter.notifyDataSetChanged();
                //刷新就扫描蓝牙
                ((MainActivity) getActivity()).startScan();
                //一秒后隐藏刷新
                mHandler.removeCallbacksAndMessages(null);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (swipeRefreshLayout != null) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 200);
            }
        });
        Logger.d("devicesNew：" + devicesNew.size());
        mMShowNotBleAdapter = new ShowNotBleAdapter(devicesNew);
        mRecyclerView.setAdapter(mMShowNotBleAdapter);
        /*条目点击事件*/
        mMShowNotBleAdapter.onItemClick(new ShowNotBleInterface() {
            @Override
            public void itemClick(int position) {
                if (devicesNew != null && devicesNew.size() != 0) {
                    ((MainActivity) getActivity()).setPagerIndex(devicesNew.get(position).getAddress(), devicesNew.get(position).getName());
                    Logger.d("连接的mac地址：" + devicesNew.get(position).getAddress());
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d("扫描界面被销毁了");
        mHandler.removeCallbacksAndMessages(null);
    }

    public void setDate(BluetoothDevice device) {
        if (mMShowNotBleAdapter != null) {//必须判断 避免扫描中fragment切换销毁时为null
            if (devicesNew.contains(device)) {
                return;
            }
            devicesNew.add(device);
            mMShowNotBleAdapter.notifyItemChanged(devicesNew.size() - 1);
        }
    }
}