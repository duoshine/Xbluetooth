package cn.chenanduo.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.chenanduo.BaseFragment;
import cn.chenanduo.MainActivity;
import cn.chenanduo.R;
import cn.chenanduo.adapter.ConnecdAdapter;
import cn.chenanduo.adapter.PrintResponseAdapter;
import cn.chenanduo.util.Logger;
import cn.chenanduo.util.ToastUtils;
import cn.chenanduo.util.Util;


/**
 * Created by chen on 2017
 */

public class ConnecdFragment extends BaseFragment implements View.OnClickListener, ConnecdAdapter.itemClick {

    private TextView mtv_macName;
    private EditText et_send;
    private Button btn_send;
    private Button btn_reconnection_view;
    private Button btn_clean;
    private RecyclerView mRecyclerView;
    private ConnecdAdapter mConnecdAdapter;
    private PrintResponseAdapter mResponseAdapter;
    private MainActivity mActivity;
    //uuid存储
    private List<String> mDatas = new ArrayList<>();
    //返回结果存储
    private List<String> responseDatas = new ArrayList<>();
    //收到设备返回结果的显示item
    private RecyclerView mRecyclerView_response;
    //开启通知使用
    private String serviceUuid = "";
    private String notifyUuid = "";
    private String writeUuid = "";
    private  String currentBleMac = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connecd, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mtv_macName = view.findViewById(R.id.tv_connecd_mac);
        et_send = view.findViewById(R.id.et_send);
        btn_send = view.findViewById(R.id.btn_send);
        btn_reconnection_view = view.findViewById(R.id.btn_reconnection_view);
        btn_clean = view.findViewById(R.id.btn_clean);
        mRecyclerView = view.findViewById(R.id.RecyclerView);

        btn_send.setOnClickListener(this);
        btn_clean.setOnClickListener(this);
        btn_reconnection_view.setOnClickListener(this);

        mActivity = (MainActivity) getActivity();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mConnecdAdapter = new ConnecdAdapter(mDatas);
        mRecyclerView.setAdapter(mConnecdAdapter);
        mConnecdAdapter.onItemClick(this);

        mRecyclerView_response = view.findViewById(R.id.RecyclerView_response);
        mRecyclerView_response.setLayoutManager(new LinearLayoutManager(mContext));
        mResponseAdapter = new PrintResponseAdapter(responseDatas);
        mRecyclerView_response.setAdapter(mResponseAdapter);

        //切换时会销毁 但是蓝牙连接并没有断开  这里还是需要默认显示连接状态
        mtv_macName.setText(currentBleMac);
    }

    /**
     * 断开或者连接的显示
     *
     * @param
     */
    public void displayConnectState(String text) {
        if (mtv_macName == null || btn_reconnection_view == null)  {
            return;
        }
        currentBleMac = text;
        mtv_macName.setText(currentBleMac);
        btn_reconnection_view.setText("断开重连");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                //获取用户输入
                getUserInput();
                break;
            case R.id.btn_clean: //清空
                cleanResponse();
                break;
            case R.id.btn_reconnection_view: //断线重连
                reconnection();
                break;
        }
    }

    /**
     * 断线重连
     */
    private void reconnection() {
        if (mActivity == null) {
            return;
        }
        btn_reconnection_view.setText("正在重连...");
        mActivity.connect();
    }

    private void cleanResponse() {
        if (mResponseAdapter == null) {
            return;
        }
        responseDatas.clear();
        mResponseAdapter.notifyDataSetChanged();
    }

    public void getUserInput() {
        String date = et_send.getText().toString().trim().replace(" ", "");
        if (TextUtils.isEmpty(date)) {
            showHint("为空");
            return;
        }
        try {
            Logger.d("原始数据:" + date);
            byte[] bytes = Util.HexString2Bytes(date);
            mActivity.write(bytes);
        } catch (Exception e) {
            ToastUtils.showToast(mActivity, "数据转换出错请检查输入");
            return;
        }
    }

    /**
     * 显示uuid给用户选择
     *
     * @param bean
     */
    public void displayUuid(List<String> bean) {
        Logger.d("mConnecdAdapter:" + mConnecdAdapter);
        if (mConnecdAdapter == null) {
            return;
        }
        mDatas.clear();
        mDatas.addAll(bean);
        mConnecdAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnItemClick(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("设置UUID");
        //builder.setMessage("你的时间快到了，请到114缴费！");
        final String items[] = {"service", "notify", "write"};
        /**
         * 第二个参数：指定被选中的项
         */
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = items[which];
                switch (name) {
                    case "service":
                        serviceUuid = mDatas.get(position);
                        break;
                    case "notify":
                        notifyUuid = mDatas.get(position);
                        break;
                    case "write":
                        writeUuid = mDatas.get(position);
                        break;
                    default:
                        break;
                }
                //消失
                dialog.dismiss();
                checkUUID();
            }
        });
        builder.show();
    }

    private void checkUUID() {
        if (TextUtils.isEmpty(serviceUuid)) {
            ToastUtils.showToast(getContext(), "服务的uuid没有设置哦");
            return;
        }
        if (TextUtils.isEmpty(notifyUuid)) {
            ToastUtils.showToast(getContext(), "通知的uuid没有设置哦");
            return;
        }
        if (TextUtils.isEmpty(writeUuid)) {
            ToastUtils.showToast(getContext(), "写的uuid没有设置哦");
            return;
        }
        //去除其他多余的uuid  只显示目前选择的Uuid在界面上 节省一些空间来显示返回结果
        if (mConnecdAdapter != null) {
            mDatas.clear();
            mDatas.add(serviceUuid);
            mDatas.add(notifyUuid);
            mDatas.add(writeUuid);
            mConnecdAdapter.notifyDataSetChanged();
        }
        serviceUuid = serviceUuid.substring(serviceUuid.length() - 36);// 截取掉开头 开始不需要的数据长度不可控 从后面控制
        notifyUuid = notifyUuid.substring(notifyUuid.length() - 36);
        writeUuid = writeUuid.substring(writeUuid.length() - 36);
        //根据已发现的服务 开启读写通道
        mActivity.setUUID(serviceUuid, notifyUuid, writeUuid);
        serviceUuid = null;
        notifyUuid = null;
        writeUuid = null;
    }

    public void onResponse(String response) {
        if (mResponseAdapter == null) {
            return;
        }
        responseDatas.add(response);
        mRecyclerView_response.scrollToPosition(mResponseAdapter.getItemCount() - 1);
    }
}
