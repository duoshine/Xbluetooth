package cn.chenanduo.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class ThreadUtils {

    private static Executor mExecutor = Executors.newSingleThreadExecutor();
    private static Handler mHandler = new Handler(Looper.getMainLooper());
    public static void runOnSubThread(Runnable runnable) {
        mExecutor.execute(runnable);
    }

    public static void runOnUiThread(Runnable runnable) {
        mHandler.post(runnable);
    }
}
