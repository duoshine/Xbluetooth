package cn.chenanduo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.chenanduo.BaseFragment;
import cn.chenanduo.MainActivity;
import cn.chenanduo.R;
import cn.chenanduo.util.SpUtil;


/**
 * Created by chen on 2017  信任设备
 */

public class BondedFragment extends BaseFragment implements View.OnClickListener {

    private TextView mBle_name;
    private TextView mBle_mac;
    private String mBondedname;
    private String mBondedaddress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bonded, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mBle_name = (TextView) view.findViewById(R.id.ble_name);
        mBle_mac = (TextView) view.findViewById(R.id.ble_mac);

        mBondedname = SpUtil.getString(mContext, "currentBleName", null);
        mBondedaddress = SpUtil.getString(mContext, "currentBleAddress", null);

        if (mBondedaddress == null) {
            return;
        }
        mBle_mac.setText(mBondedaddress);
        if (mBondedname == null) {
            mBle_name.setText("");
        } else {
            mBle_name.setText(mBondedname);
        }
        mBle_name.setOnClickListener(this);
        mBle_mac.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ble_mac:
            case R.id.ble_name:
                ((MainActivity) getActivity()).setPagerIndex(mBondedaddress, mBondedname);
                break;
            default:
                break;
        }
    }
}
