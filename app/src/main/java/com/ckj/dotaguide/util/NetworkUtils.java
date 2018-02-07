package com.ckj.dotaguide.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by chenkaijian on 17-9-4.
 */

public class NetworkUtils {
    /**
     * Returns whether the network is available
     *
     * @param context
     *            Context
     * @return 网络是否可用
     * @see [类、类#方法、类#成员]
     */
    public static boolean isNetworkAvailable(Context context) {
        return getConnectedNetworkInfo(context) != null;
        // 模拟断网
        // return false;
    }

    public static NetworkInfo getConnectedNetworkInfo(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                // LogUtils.error("couldn't get connectivity manager");
            }
            else {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null) {
                    /*
                     * for (int i = 0; i < info.length; i++) { if
                     * (info[i].getState() == NetworkInfo.State.CONNECTED) {
                     * return info[i]; } }
                     */
                    return info;
                }
            }

        } catch (Exception e) {
            // LogUtils.error(e.toString(), e);
        }
        return null;
    }
}
