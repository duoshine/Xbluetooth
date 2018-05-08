package cn.chenanduo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import cn.chenanduo.util.ToastUtils;


/**
 * Created by chen on 2017
 */

public class BaseFragment extends Fragment {
    protected Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    protected void showHint(String text) {
        ToastUtils.showToast(mContext,text);
    }


}
