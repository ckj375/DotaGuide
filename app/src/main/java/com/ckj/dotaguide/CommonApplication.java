package com.ckj.dotaguide;

import android.app.Application;
import android.content.Context;

/**
 * Created by chenkaijian on 17-8-31.
 */

public class CommonApplication extends Application {

    public static Context mContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static CommonApplication getInstance() {
        return (CommonApplication) mContext;
    }
}
