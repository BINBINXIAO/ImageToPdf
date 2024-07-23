package com.lanxin.testdemo.app;

import android.app.Application;
import android.content.Context;

/**
 * @author:张小兵
 * @e-mail:460116602@qq.com
 * @date:2024/01/19
 */
public class App extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        BContext.init(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
