package cn.chenanduo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import cn.chenanduo.util.Logger;

/**
 * Created by chen on 2018
 */

public class BaseActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_SETTING = 1;
    private ProgressDialog mDialog;

    /**
     * 6.0权限申请
     */
    protected void requestPermission(String[] permissions, int requestPermissionCode) {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionslist = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {
                //遍历所有的传过来的所有权限 判断是否已经申请 如果未申请添加进待申请的集合中
                if (ActivityCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    permissionslist.add(permissions[i]);
                }
            }
            if (permissionslist.size() != 0) {
                String[] permissionsArray = permissionslist.toArray(new String[permissionslist.size()]);
                ActivityCompat.requestPermissions(this, permissionsArray,
                        requestPermissionCode);
            }
        }
    }

    /**
     * 适用于已经请求过的危险权限 再次使用到该功能时 提示用户去权限界面手动开启权限
     *
     * @param permissions 需要判断的权限
     * @param text        如果没有该权限  那么弹窗显示的内容 比如:应用缺少定位权限\r\n\r\n这将导致蓝牙功能无法正常使用\r\n\r\n前往设置-权限管理-开启定位权限
     * @return
     */
    protected boolean oneMoreTimeRequestPermission(String permissions, String text) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, permissions) != PackageManager.PERMISSION_GRANTED) {
                //是6.0但是没有权限 避免没有弹窗提示开启权限 直接提示去设置界面自己开启
                requestPermissionDialog(text);
                Logger.d("是6.0但是没有权限 避免没有弹窗提示开启权限 直接提示去设置界面自己开启");
                return false;
            } else {
                //是6.0但是有权限
                Logger.d("是6.0但是有权限");
                return true;
            }
        } else {
            //小于6.0
            Logger.d("小于6.0");
            return true;
        }
    }

    /*
    * 弹窗提示用户去授权 主要为了防止部分机型不会第二次弹出系统的权限申请框
    * 所以我们直接让用户自己去开启定位权限
    */
    private void requestPermissionDialog(String text) {
        //builder.setMessage("应用缺少定位权限\r\n\r\n这将导致蓝牙功能无法正常使用\r\n\r\n前往设置-权限管理-开启定位权限");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("提示");
        builder.setMessage(text);
        //点击旁边不会消失
        builder.setCancelable(false);
        //设置确认按钮
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //这边启动到设置权限界面之后 无论用户是否给予定位权限 结果都是要点击返回 结果都是canceled
                //所以就不实现result方法判断结果了 再次扫描蓝牙时 如果用户没有给予定位权限 还是会执行此提示 如果给了定位权限则直接扫描
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                //这个针对小米手机 可以直接跳转到小米手机权限界面  但是不适配其他机型 比如华为 所以暂时使用上面的
                //Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
            }
        });
        //设置取消按钮
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    public void showDialog() {
        if (mDialog == null) {
            mDialog = new ProgressDialog(this);
            mDialog.setTitle("正在连接...");
        }
        mDialog.show();
    }

    public void hideDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
