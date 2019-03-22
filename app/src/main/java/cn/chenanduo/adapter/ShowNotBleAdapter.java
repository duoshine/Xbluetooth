package cn.chenanduo.adapter;

import android.bluetooth.BluetoothDevice;
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

public class ShowNotBleAdapter extends RecyclerView.Adapter<ShowNotBleAdapter.MyViewHolder> {
    private List<BluetoothDevice> devices;

    public ShowNotBleAdapter(List<BluetoothDevice> device) {
        this.devices = device;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shownotble_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        String name = devices.get(position).getName();
        if (null == name) {
            holder.ble_name.setText("name:");
        } else {
            holder.ble_name.setText("name:" + name);
        }
        holder.ble_mac.setText("address:" + devices.get(position).getAddress());
        holder.ble_rssi.setText("Rssi:--" );
        holder.ble_ScanRecord.setSelected(true);
        holder.ble_ScanRecord.setText("ScanRecord:--");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShowNotBleInterface != null)
                    mShowNotBleInterface.itemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView ble_name;
        private TextView ble_mac;
        private TextView ble_rssi;
        private TextView ble_ScanRecord;

        public MyViewHolder(View itemView) {
            super(itemView);
            ble_name = itemView.findViewById(R.id.ble_name);
            ble_mac = itemView.findViewById(R.id.ble_mac);
            ble_rssi = itemView.findViewById(R.id.ble_rssi);
            ble_ScanRecord = itemView.findViewById(R.id.ble_ScanRecord);

        }
    }

    private ShowNotBleInterface mShowNotBleInterface;

    public void onItemClick(ShowNotBleInterface showNotBleInterface) {
        this.mShowNotBleInterface = showNotBleInterface;

    }
}
