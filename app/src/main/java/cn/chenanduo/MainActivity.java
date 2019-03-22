package cn.chenanduo;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import cn.chenanduo.adapter.HomePagerAdapter;
import cn.chenanduo.fragment.BondedFragment;
import cn.chenanduo.fragment.ConnecdFragment;
import cn.chenanduo.fragment.ShowBleFragment;
import cn.chenanduo.simplebt.BluetoothBLeClass;
import cn.chenanduo.simplebt.BluetoothChangeListener;
import cn.chenanduo.util.Logger;
import cn.chenanduo.util.SpUtil;
import cn.chenanduo.util.ThreadUtils;
import cn.chenanduo.util.Util;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, BluetoothChangeListener {
    private List<String> titles = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothBLeClass mBLE;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ConnecdFragment mConnecdFragment;
    private ShowBleFragment mShowNotBleFragment;
    private Timer mTimer;
    private int currentPosition = 0; //当前页面
    private String currentBleAddress;
    private String currentBleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        init();
        //请求定位权限
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        requestPermission(permissions, 2);
        //初始化蓝牙
        initBle();
    }

    private void init() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("BluetoothTest");
        mConnecdFragment = new ConnecdFragment();
        mShowNotBleFragment = new ShowBleFragment();
        fragments.add(mShowNotBleFragment);
        fragments.add(new BondedFragment());
        fragments.add(mConnecdFragment);
        titles.add("Scanner");
        titles.add("Bonded");
        titles.add("Connecd");

        HomePagerAdapter adapter = new HomePagerAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(adapter);
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_behavior);
        mTabLayout = (TabLayout) findViewById(R.id.tablayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        //设置和viewpager联动
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(this);
    }

    private void initBle() {
        mBLE = BluetoothBLeClass.getInstane(MainActivity.this, null, null, null)
                .setScanTime(5000)//设置扫描时间为5秒 不设置默认5秒
                .setAutoConnect(false)//设置断开后自动连接
                .closeCleanCache(false);//设置每次断开连接都清除缓存
        mBLE.getBleCurrentState(this);
        if (!mBLE.initialize()) {
            //弹窗显示开启蓝牙
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            //开始扫描设备
            startScan();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            //开始扫描设备
            startScan();
        }
    }

    public void startScan() {
        //扫描耗时操作 最好放在子线程操作 这样Ui体验会好一些
        ThreadUtils.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                mBLE.startScanDevices(true);
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCurrentState(int state) {
        switch (state) {
            case 0:
                Logger.d("设备连接断开");
                mConnecdFragment.displayConnectState("蓝牙连接断开");
                break;
            case 1:
                Logger.d("设备正在扫描");
                break;
            case 2:
                Logger.d("设备扫描结束");
                break;
            case 3:
                Logger.d("设备正在连接");
                break;
            case 4:
                Logger.d("设备连接成功");
                hideDialog();
                //如果连接成功且页面在0 就跳转到connecd页面
                if (currentPosition != 2) {
                    mViewPager.setCurrentItem(2);
                }
                mConnecdFragment.displayConnectState(currentBleAddress);
                break;
            case 5:
                Logger.d("正在尝试重连 ");
                break;
            default:
                break;
        }
    }

    @Override
    public void onBleWriteResult(byte[] bytes) {
        String s = Util.Bytes2HexString(bytes);
        Logger.d("收到返回:" + s);
        mConnecdFragment.onResponse(s);
    }

    @Override
    public void onBleScanResult(BluetoothDevice device) {
        mShowNotBleFragment.setDate(device);
    }

    @Override
    public void onWriteDataSucceed(byte[] bytes) {
        Logger.d("写入成功:" + Util.Bytes2HexString(bytes));
        Logger.d("onWriteDataSucceed:" + Util.isMainThread());
    }

    /**
     * 设置通知等成功 可以通信
     */
    @Override
    public void findServiceSucceed() {
        Logger.d("可以发送数据啦");
        Logger.d("findServiceSucceed:" + Util.isMainThread());
    }

    @Override
    public void getDisplayServices(BluetoothGatt bluetoothGatt) {
        Logger.d("服务已经查找到可以显示给用户选择了");
        /**
         * 连接成功后会自动发现服务 回调时会触发该方法 可以在这里显示给用户 但是需要经过处理
         */
        final List<String> bean = checkServices(bluetoothGatt.getServices());
        Logger.d("getDisplayServices:" + Util.isMainThread());
        ThreadUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mConnecdFragment.displayUuid(bean);//todo
            }
        });
    }

    /**
     * 对uuid进行筛选 判断出 读 写 服务
     *
     * @param
     * @param
     */
    private List<String> checkServices(List<BluetoothGattService> gattServices) {
        List<String> characteristic = new ArrayList<>();
        for (int i = 0; i < gattServices.size(); i++) {
            BluetoothGattService bluetoothGattService = gattServices.get(i);
            characteristic.add("service-" + bluetoothGattService.getUuid().toString());
            List<BluetoothGattCharacteristic> characteristics = bluetoothGattService.getCharacteristics();
            for (int i1 = 0; i1 < characteristics.size(); i1++) {
                String characteristicsName = "";
                int properties = characteristics.get(i1).getProperties();
                /**
                 * 0.每个服务的uuid确定为一个 但是有多个服务
                 * 1.服务下uuid的数量不应该是固定的
                 * 2.每个uuid的特征不应该是固定的
                 * 3.这个函数处理完应该是直接显示的  不应该再次处理
                 */

                //读
                if ((properties & BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                    characteristicsName += "read-";
                }
                //写
                if ((properties & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
                    characteristicsName += "write-";
                }
                //通
                if ((properties & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                    characteristicsName += "notify-";
                }
                if (TextUtils.isEmpty(characteristicsName)) {
                    characteristicsName += "write no response-";
                }
                characteristic.add(characteristicsName + characteristics.get(i1).getUuid());
            }
        }
        return characteristic;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mBLE != null) {
            mBLE.close();//一定要调用释放资源
        }
        SpUtil.saveString(this, "currentBleName", currentBleName);
        SpUtil.saveString(this, "currentBleAddress", currentBleAddress);
    }

    /**
     * 点击item连接蓝牙
     *
     * @param address
     * @param name
     */
    public void setPagerIndex(String address, String name) {
        currentBleAddress = address;
        currentBleName = name;
        connect();
    }

    public void connect() {
        if (currentBleAddress == null) {
            return;
        }
        mBLE.connect(currentBleAddress);//注意我这里的连接在库中已经做了停止扫描 断开上一个连接的前提操作 详见stopScanDevices函数
        showDialog();
    }

    public void setUUID(String serviceUuid, String notifyUuid, String writeUuid) {
        Logger.d("serviceUuid:" + serviceUuid);
        Logger.d("notifyUuid:" + notifyUuid);
        Logger.d("writeUuid:" + writeUuid);
        mBLE.displayGattServices(serviceUuid, notifyUuid, writeUuid);
    }

    public void write(byte[] data) {
        mBLE.writeCharacteristic(data);
    }
}
